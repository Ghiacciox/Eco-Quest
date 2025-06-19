package com.ingegneriasoftware.ecoquest.ui.components

// Import delle librerie necessarie
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ingegneriasoftware.ecoquest.R
import com.ingegneriasoftware.ecoquest.data.models.AchievedTrophy
import kotlinx.datetime.Instant
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TrophySection(trophies: List<AchievedTrophy>, navController: NavController) {
    val trophyToShow = 3 // Numero massimo di trofei da mostrare
    Box(
        modifier = Modifier.fillMaxWidth() // Occupa tutta la larghezza disponibile
    ) {
        val currenTrophy = trophies.take(trophyToShow) // Prendi i primi trofei da mostrare
        val pageCount = if (currenTrophy.isEmpty()) 1 else currenTrophy.size // Numero di pagine
        // Creazione dello stato del pager
        val pagerState = rememberPagerState(
            initialPage = 0,
            pageCount = { pageCount } // Numero di pagine
        )
        // Slider orizzontale per i trofei
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { trophy ->
            if (trophies.isEmpty()) {
                // Mostra un trofeo di esempio se non ci sono trofei disponibili
                TrophyItem(
                    trophy = AchievedTrophy(
                        name = "Non hai nessn trofeo!",
                        description = "Ottieni il tuo primo trofeo completando una missione!",
                        pictureUrl = "https://mnqyxftyucsmmnvuoyuw.supabase.co/storage/v1/object/public/trophy.pics//notrop.png",
                        earnedAt = Instant.parse("2023-01-01T00:00:00Z"),
                        trophyId = -1,
                        condition = "no condizioni"
                    ),
                    onItemClick = {}
                )
            } else if (trophy < currenTrophy.size) {
                // Mostra il trofeo corrente
                TrophyItem(
                    trophy = currenTrophy[trophy],
                    onItemClick = {}
                )
            }
        }
    }
}

@Composable
fun TrophyItem(
    trophy: AchievedTrophy, // Dati del trofeo
    onItemClick: () -> Unit // Callback per il click sul trofeo
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
            // Mostra l'immagine del trofeo
            if (!trophy.pictureUrl.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(trophy.pictureUrl),
                    contentDescription = "Trophy Image",
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
                    contentDescription = "Trophy Image",
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
                // Nome del trofeo
                Text(
                    text = trophy.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Data di completamento del trofeo
                Text(
                    text = "Completata: ${parseDate(trophy.earnedAt.toString())}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Descrizione del trofeo
                Text(
                    text = trophy.description,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
            }
        }
    }
}

// Funzione per formattare una data ISO in un formato leggibile
fun parseDate(isoDate: String): String {
    return try {
        // Prendi solo la parte "YYYY-MM-DD"
        val dateOnly = isoDate.substringBefore('T')
        val parts = dateOnly.split("-") // ["2025", "04", "10"]
        val day = parts[2]
        val monthName = SimpleDateFormat("MMM", Locale("it", "IT"))
            .format(SimpleDateFormat("MM", Locale.US).parse(parts[1])!!)
        val year = parts[0]
        "$day $monthName $year" // Restituisce la data formattata
    } catch (_: Exception) {
        isoDate // Restituisce la data originale in caso di errore
    }
}

