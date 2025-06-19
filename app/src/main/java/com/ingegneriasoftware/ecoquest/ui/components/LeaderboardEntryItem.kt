package com.ingegneriasoftware.ecoquest.ui.components

// Import delle librerie necessarie
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ingegneriasoftware.ecoquest.data.models.LeaderboardEntry

@Composable
fun LeaderboardEntryItem(
    user: LeaderboardEntry, // Dati dell'utente da visualizzare
) {
    // Definizione dei colori per i bordi delle medaglie
    val goldColorBorder = Color(0xFFFFD700)
    val silverColorBorder = Color(0xFFC0C0C0)
    val bronzeColorBorder = Color(0xFFCD7F32)

    // Card che rappresenta un elemento della classifica
    Card(
        shape = RoundedCornerShape(15.dp), // Forma arrotondata
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp), // Margini
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Elevazione
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.primaryContainer // Colore di sfondo
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(), // Disposizione orizzontale
            verticalAlignment = Alignment.CenterVertically, // Allineamento verticale
        ) {
            // Immagine del profilo con bordo colorato in base al rango
            ProfilePic(
                user.profilePic,
                modifier = Modifier
                    .weight(1f) // Occupa 1 parte dello spazio disponibile
                    .aspectRatio(1f), // Forma quadrata
                shape = CircleShape, // Forma circolare
                borderColor = when (user.rank) { // Colore del bordo in base al rango
                    1 -> goldColorBorder
                    2 -> silverColorBorder
                    3 -> bronzeColorBorder
                    else -> MaterialTheme.colorScheme.primary
                }
            )
            Spacer(Modifier.width(32.dp)) // Spazio tra immagine e testo
            Column(
                modifier = Modifier
                    .weight(3f), // Occupa 3 parti dello spazio disponibile
                verticalArrangement = Arrangement.Center, // Centra verticalmente
                horizontalAlignment = Alignment.Start // Allinea a sinistra
            ) {
                // Nome utente
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.outline,
                    maxLines = 1, // Mostra solo una riga
                    overflow = TextOverflow.Ellipsis // Troncamento se troppo lungo
                )
                Spacer(modifier = Modifier.height(4.dp)) // Spazio tra nome e punti
                // Punti utente
                Text(
                    text = "${user.points} pnt",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(8.dp)) // Spazio tra testo e indicatore
            // Indicatore della medaglia
            Box(
                modifier = Modifier
                    .weight(1f) // Occupa 1 parte dello spazio disponibile
                    .aspectRatio(1f), // Forma quadrata
                contentAlignment = Alignment.Center // Centra il contenuto
            ) {
                MedalIndicator(user.rank) // Mostra la medaglia in base al rango
            }
        }
    }
}

@Composable
fun Tabs_ladder(
    gLadder: LeaderboardEntry?, // Classifica giornaliera
    sLadder: LeaderboardEntry?, // Classifica settimanale
    mLadder: LeaderboardEntry?, // Classifica mensile
) {
    Column {
        // Mostra un messaggio se le classifiche non sono disponibili
        if (gLadder == null || sLadder == null || mLadder == null ) {
            Text("Classifica non disponbile", style = MaterialTheme.typography.bodyMedium)
        } else {
            // Nomi delle classifiche
            val laddersNames = listOf("Giornaliera", "Settimanale", "Mensile")
            val ladderList = listOf(gLadder, sLadder, mLadder)

            // Schermata con le classifiche
            Tabs_ladder_screen(
                ladders = ladderList,
                tabs = laddersNames,
                modifier = Modifier
                    .height((LocalConfiguration.current.screenHeightDp * 0.2).dp) // Altezza dinamica
            )
        }
    }
}

@Composable
fun Tabs_ladder_screen(
    ladders: List<LeaderboardEntry?>, // Lista delle classifiche
    tabs: List<String>, // Nomi delle schede
    modifier: Modifier = Modifier,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) } // Indice della scheda selezionata
    val maxLadder = 5 // Numero massimo di elementi da mostrare

    Column(modifier = modifier.fillMaxSize() ) {
        // Riga delle schede personalizzate
        CustomTabRow(
            tabs = tabs,
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it } // Cambia scheda selezionata
        )
        // Contenitore per il pager
        Box(
            modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
        ) {
            val currentLadder = ladders[selectedTabIndex]?.let { listOf(it) }?.take(maxLadder) ?: emptyList()
            val pagerState = rememberPagerState(
                initialPage = 0,
                pageCount = { currentLadder.size } // Numero di pagine
            )
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ){ index ->
                if(index < currentLadder.size) {
                    LeaderboardEntryItem(user = currentLadder[index]) // Mostra l'elemento della classifica
                }
            }
        }
    }
}

data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
fun MedalIndicator(
    rank: Int, // Rango dell'utente
    modifier: Modifier = Modifier,
    size: Dp = 48.dp // Dimensione totale dell'indicatore
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center // Centra il testo sopra il Canvas
    ) {
        when (rank) {
            rank -> {
                // Seleziona i colori giusti
                val (baseColor, highlight, textColor) = when (rank) {
                    1 -> Quadruple(Color(0xFFFFD700), Color(0xFFFFE127), Color(0xffb18202), Color(0xFFFFD700))
                    2 -> Quadruple(Color(0xFFC0C0C0), Color(0xFFD3D3D3), Color(0xff858484), Color(0xFFC0C0C0))
                    3 -> Quadruple(Color(0xFFCD7F32), Color(0xFFD2B48C), Color(0xFF8B4513), Color(0xFFCD7F32))
                    else -> Quadruple(Color.Gray, Color.LightGray, Color.DarkGray, Color.Gray)
                }

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val canvasWidth = this.size.width
                    val canvasHeight = this.size.height

                    // --- Calcoli Dimensioni Relative ---
                    val medalDiameter = canvasWidth * 0.75f // Diametro cerchio medaglia
                    val medalRadius = medalDiameter / 2f

                    // Centro del cerchio (leggermente più in alto per far spazio al nastro sotto)
                    val medalCenter = Offset(canvasWidth / 2f, canvasHeight * 0.45f)

                    // Gradiente radiale per effetto metallico
                    val medalGradient = Brush.radialGradient(
                        colors = listOf(highlight, baseColor), // Highlight al centro, base verso i bordi
                        center = medalCenter.copy(y = medalCenter.y - medalRadius * 0.1f), // Centro gradiente leggermente alzato
                        radius = medalRadius * 3f // Raggio gradiente leggermente più grande
                    )

                    // Cerchio con gradiente
                    drawCircle(
                        brush = medalGradient,
                        radius = medalRadius,
                        center = medalCenter
                    )

                    // Bordo scuro leggermente interno per dare profondità
                    drawCircle(
                        color = Color.Black.copy(alpha = 0.15f), // Ombra scura trasparente
                        radius = medalRadius,
                        center = medalCenter.copy(y = medalCenter.y + 0.5.dp.toPx()), // Leggermente spostato sotto
                        style = Stroke(width = 4.dp.toPx()) // Spessore bordo/ombra
                    )

                    // Bordo più chiaro sulla parte superiore (simula luce) - Opzionale
                    if(rank == 1 || rank == 2 || rank == 3) {
                        drawArc(
                            color = highlight.copy(alpha = 0.7f),
                            startAngle = -160f,
                            sweepAngle = 140f,
                            useCenter = false,
                            topLeft = Offset(medalCenter.x - medalRadius, medalCenter.y - medalRadius),
                            size = Size(medalDiameter, medalDiameter),
                            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }

                }
                // Disegna il numero del rango sopra la medaglia
                Text(
                    text = "$rank",
                    color = textColor,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = (size.value * 0.55f / LocalDensity.current.fontScale).sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center) // Centra nel Box
                        .offset(y = (-size.value * 0.05).dp) // Leggero offset verticale
                )
            }
        }
    }
}

