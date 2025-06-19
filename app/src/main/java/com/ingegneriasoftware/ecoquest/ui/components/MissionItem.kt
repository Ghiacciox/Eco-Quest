package com.ingegneriasoftware.ecoquest.ui.components

// Import delle librerie necessarie
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ingegneriasoftware.ecoquest.R
import com.ingegneriasoftware.ecoquest.data.models.Missions
import com.ingegneriasoftware.ecoquest.navigation.Screen

@Composable
fun Tabs_missions(
    gMissions: List<Missions>, // Missioni giornaliere
    sMissions: List<Missions>, // Missioni settimanali
    mMissions: List<Missions>, // Missioni mensili
    navController: NavController // Controller per la navigazione
) {
    val titleMission = listOf("Giornaliera", "Settimanale", "Mensile") // Titoli delle schede
    val missionList = listOf(gMissions, sMissions, mMissions) // Lista delle missioni

    // Schermata con le schede delle missioni
    TabsMissionScreen(
        activeMissions = missionList,
        tabs = titleMission,
        modifier = Modifier
            .height((LocalConfiguration.current.screenHeightDp * 0.28f).dp), // Altezza dinamica
        navController
    )
}

@Composable
fun TabsMissionScreen(
    activeMissions: List<List<Missions>>, // Lista delle missioni attive
    tabs: List<String>, // Nomi delle schede
    modifier: Modifier = Modifier,
    navController: NavController // Controller per la navigazione
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) } // Indice della scheda selezionata
    val missionsToShow = 3 // Numero massimo di missioni da mostrare per scheda

    Column(modifier = modifier.fillMaxSize()) {
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
            val currentMissions = activeMissions[selectedTabIndex].take(missionsToShow) // Missioni da mostrare

            // Creazione dello stato del pager
            val pageCount = if (currentMissions.isEmpty()) 1 else currentMissions.size
            val pagerState = rememberPagerState(
                initialPage = 0,
                pageCount = { pageCount } // Numero di pagine
            )

            // Log delle missioni per debug
            currentMissions.forEach { mission ->
                println("Mission: ${mission.title}, Points: ${mission.points}, Description: ${mission.description}")
            }
            Log.d("MissionsViewModel", "size: ${currentMissions.size}")

            // Slider orizzontale per le missioni
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                if (currentMissions.isEmpty()) {
                    // Mostra un messaggio se non ci sono missioni disponibili
                    MissionItemSlider(
                        mission = Missions(
                            title = "Nessuna missione disponibile",
                            description = "Torna più tardi per nuove missioni!",
                            points = 0,
                            missionPic = "https://mnqyxftyucsmmnvuoyuw.supabase.co/storage/v1/object/public/mission.pics//no_quests.jpeg",
                            id = -1, // ID predefinito
                            type = "placeholder", // Tipo predefinito
                            missionPrompt = "Nessuna missione", // Prompt predefinito
                            camera = false // Valore booleano predefinito
                        ),
                        onItemClick = {}
                    )
                } else if (page < currentMissions.size) {
                    // Mostra la missione corrente
                    MissionItemSlider(
                        mission = currentMissions[page],
                        onItemClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "mission",
                                currentMissions.getOrNull(page) to false
                            )
                            navController.navigate(Screen.SingleMission.route)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CustomTabRow(
    tabs: List<String>, // Nomi delle schede
    selectedTabIndex: Int, // Indice della scheda selezionata
    onTabSelected: (Int) -> Unit, // Callback per cambiare scheda
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = Color.Transparent, // Sfondo trasparente
        divider = {}, // Nessun divisore
        indicator = {}, // Nessun indicatore
        modifier = Modifier.fillMaxWidth()
    ) {
        tabs.forEachIndexed { index, title ->
            // Crea una scheda personalizzata per ogni titolo
            CustomTab(
                title = title,
                isSelected = selectedTabIndex == index,
                onClick = { onTabSelected(index) }
            )
        }
    }
}

@Composable
fun CustomTab(title: String, isSelected: Boolean, onClick: () -> Unit) {
    // Animazione per il colore del testo
    val textColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.tertiary,
        animationSpec = tween(durationMillis = 300),
        label = "textColor"
    )

    // Animazione per il colore del contenuto
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(durationMillis = 300),
        label = "contentColor"
    )

    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium // Peso del font

    Tab(
        selected = isSelected,
        onClick = onClick,
        selectedContentColor = contentColor,
        unselectedContentColor = contentColor
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.background,
                    shape = CircleShape
                )
                .padding(horizontal = 16.dp, vertical = 2.dp)
        ) {
            Text(
                text = title,
                color = textColor,
                fontWeight = fontWeight,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MissionItemSlider(
    mission: Missions, // Dati della missione
    onItemClick: () -> Unit // Callback per il click sulla missione
) {
    Card(
        modifier = Modifier
            .fillMaxWidth() // Occupa tutta la larghezza disponibile
            .padding(horizontal = 16.dp, vertical = 8.dp) // Margini esterni
            .clickable { onItemClick() }, // Abilita il click
        shape = RoundedCornerShape(15.dp), // Angoli arrotondati
        elevation = CardDefaults.cardElevation(4.dp), // Elevazione
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Mostra l'immagine della missione
            if (!mission.missionPic.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(mission.missionPic),
                    contentDescription = "Mission Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.40f)
                        .clip(RoundedCornerShape(7.dp))
                        .aspectRatio(1f)
                )
            } else {
                // Fallback se l'immagine è null o vuota
                Image(
                    painter = painterResource(R.drawable.ic_placeholder),
                    contentDescription = "Mission Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(fraction = 1 / 3f)
                        .clip(RoundedCornerShape(6.dp))
                        .aspectRatio(1f)
                )
            }

            Spacer(modifier = Modifier.width(12.dp)) // Spazio tra immagine e testo

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                // Titolo della missione
                Text(
                    text = mission.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Punti della missione
                Text(
                    text = "Punti: ${mission.points}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Descrizione della missione
                Text(
                    text = mission.description ?: "No description available",
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
fun Points_card(ecoPoints: Int?, profilePic: String?, username: String?) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp), //distanza bordo esterno
        shape = RoundedCornerShape(40.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.tertiary, // verde chiaro
                            MaterialTheme.colorScheme.secondary, // verde medio
                            MaterialTheme.colorScheme.primaryContainer // verde scuro
                        ),
                        center = Offset.Zero,
                        radius = 700f
                    )
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                // Utilizza SpaceAround o SpaceBetween
                modifier = Modifier.fillMaxSize()
            ) {
                ProfilePic(
                    profilePic = profilePic,
                    shape = RoundedCornerShape(50),
                    borderColor = colorResource(id = R.color.white),
                    borderWidth = 2.dp,
                    modifier = Modifier.size(LocalConfiguration.current.screenWidthDp.dp * 0.2f)
                )
                // Testi
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f) // Occupare lo spazio rimanente
                ) {

                    Text(
                        "Ciao ${username ?: ""}!",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        "ECO-POINTS totali",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        "${ecoPoints ?: 0}",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}



