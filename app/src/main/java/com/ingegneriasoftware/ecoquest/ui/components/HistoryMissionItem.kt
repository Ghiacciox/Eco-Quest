package com.ingegneriasoftware.ecoquest.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import coil.compose.rememberAsyncImagePainter
import com.ingegneriasoftware.ecoquest.R
import com.ingegneriasoftware.ecoquest.data.models.CompletedMission

// Questo componente è stato creato per visualizzare le missioni completate in modo non cliccabile.
// È diverso dal componente utilizzato in MissionScreen, che era cliccabile.

@Composable
fun HistoryMissionItem(completedMission: CompletedMission) {
    // Card che rappresenta una missione completata.
    Card(
        modifier = Modifier
            .fillMaxWidth() // La card occupa tutta la larghezza disponibile.
            .padding(horizontal = 16.dp, vertical = 8.dp), // Padding esterno.
        shape = RoundedCornerShape(15.dp), // Angoli arrotondati della card.
        elevation = CardDefaults.cardElevation(4.dp), // Elevazione della card.
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer // Colore di sfondo della card.
        )
    ) {
        // Layout orizzontale per contenere immagine e dettagli della missione.
        Row(
            modifier = Modifier.padding(12.dp), // Padding interno della card.
            verticalAlignment = Alignment.CenterVertically, // Allineamento verticale al centro.
            horizontalArrangement = Arrangement.Start // Allineamento orizzontale a sinistra.
        ) {
            // Mostra l'immagine della missione se disponibile.
            if (!completedMission.missionPic.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(completedMission.missionPic), // Carica l'immagine da un URL.
                    contentDescription = "Mission Image", // Descrizione per l'accessibilità.
                    contentScale = ContentScale.Crop, // Ritaglia l'immagine per riempire lo spazio.
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.20f) // L'immagine occupa il 20% della larghezza.
                        .clip(RoundedCornerShape(7.dp)) // Angoli arrotondati dell'immagine.
                        .aspectRatio(1f) // Mantiene un rapporto di aspetto 1:1.
                )
            } else {
                // Mostra un'immagine di placeholder se `missionPic` è null o vuota.
                Image(
                    painter = painterResource(R.drawable.ic_placeholder), // Immagine di default.
                    contentDescription = "Mission Image", // Descrizione per l'accessibilità.
                    contentScale = ContentScale.Crop, // Ritaglia l'immagine per riempire lo spazio.
                    modifier = Modifier
                        .fillMaxWidth(fraction = 1 / 3f) // L'immagine occupa un terzo della larghezza.
                        .clip(RoundedCornerShape(6.dp)) // Angoli arrotondati dell'immagine.
                        .aspectRatio(1f) // Mantiene un rapporto di aspetto 1:1.
                )
            }

            Spacer(modifier = Modifier.width(12.dp)) // Spazio tra l'immagine e i dettagli.

            // Colonna per i dettagli della missione.
            Column(
                verticalArrangement = Arrangement.Center, // Allineamento verticale al centro.
                horizontalAlignment = Alignment.Start // Allineamento orizzontale a sinistra.
            ) {
                // Titolo della missione.
                Text(
                    text = completedMission.title, // Titolo della missione.
                    style = MaterialTheme.typography.titleMedium, // Stile del testo.
                    fontWeight = FontWeight.Bold, // Testo in grassetto.
                    color = MaterialTheme.colorScheme.outline // Colore del testo.
                )
                Spacer(modifier = Modifier.height(4.dp)) // Spazio tra il titolo e i punti.

                // Punti assegnati per la missione.
                Text(
                    text = "Punti: ${completedMission.points}", // Testo che mostra i punti.
                    style = MaterialTheme.typography.bodySmall, // Stile del testo.
                    fontWeight = FontWeight.Bold, // Testo in grassetto.
                    color = MaterialTheme.colorScheme.outlineVariant // Colore del testo.
                )
                Spacer(modifier = Modifier.height(4.dp)) // Spazio tra i punti e la descrizione.

                // Descrizione della missione.
                Text(
                    text = completedMission.description ?: "No description available", // Mostra la descrizione o un messaggio di default.
                    style = MaterialTheme.typography.bodyMedium, // Stile del testo.
                    overflow = TextOverflow.Ellipsis, // Tronca il testo se troppo lungo.
                    maxLines = 2 // Limita la descrizione a due righe.
                )

                // Data di completamento della missione.
                Text(
                    text = "Completed on: ${parseDate(completedMission.completedAt.toString())}", // Mostra la data di completamento.
                    style = MaterialTheme.typography.bodyMedium, // Stile del testo.
                    overflow = TextOverflow.Ellipsis, // Tronca il testo se troppo lungo.
                    maxLines = 1 // Limita la data a una riga.
                )
            }
        }
    }
}