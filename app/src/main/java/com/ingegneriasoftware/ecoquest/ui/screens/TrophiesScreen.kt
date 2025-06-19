package com.ingegneriasoftware.ecoquest.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ingegneriasoftware.ecoquest.R
import com.ingegneriasoftware.ecoquest.data.models.Trophy
import com.ingegneriasoftware.ecoquest.viewmodels.TrophyViewModel
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import com.ingegneriasoftware.ecoquest.data.models.AchievedTrophy

@Composable
fun TrophiesScreen(
    navController: NavController,
    viewModel: TrophyViewModel = hiltViewModel()
) {
    val achieved by viewModel.achievedTrophies.collectAsState()
    val toEarn by viewModel.trophies.collectAsState()


    var selectedAchievedTrophy by remember { mutableStateOf<AchievedTrophy?>(null) }
    var selectedUnarchivedTrophy by remember { mutableStateOf<Trophy?>(null) }

    LaunchedEffect(Unit) { viewModel.loadTrophies() }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(bottom = 6.dp)
        ) {
            // Header Achieved
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    "Trofei ottenuti",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Spacer(Modifier.height(8.dp))
            }
            if (achieved.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text("Nessun trofeo ottenuto", style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                items(achieved) { trophy ->
                    AchievedTrophyItem(trophy = trophy) {
                        selectedAchievedTrophy= trophy
                    }
                }
            }
            // Spacer
            item(span = { GridItemSpan(maxLineSpan) }) { Spacer(Modifier.height(16.dp)) }
            // Header To Earn
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    "Trofei ottenibili",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Spacer(Modifier.height(8.dp))
            }
            if (toEarn.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text("Nessun trofeo da ottenere", style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                items(toEarn) { trophy ->
                    NotAchievedTrophyItem(trophy) {
                        selectedUnarchivedTrophy = trophy
                    }
                }
            }
        }

        UnarchivedTrophyDialog(
            trophy = selectedUnarchivedTrophy,
            onDismiss = { selectedUnarchivedTrophy = null }
        )

        AchievedTrophyDialog(
            trophy = selectedAchievedTrophy,
            onDismiss = { selectedAchievedTrophy = null }
        )

    }
}

@Composable
fun UnarchivedTrophyDialog(
    trophy: Trophy?,
    onDismiss: () -> Unit
) {
    if (trophy != null) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = trophy.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        painter = rememberAsyncImagePainter(trophy.pictureUrl),
                        contentDescription = "Trophy Image",
                        contentScale = ContentScale.Crop,
                        colorFilter =  ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }),
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = trophy.description, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun  AchievedTrophyDialog(
    trophy: AchievedTrophy?,
    onDismiss: () -> Unit
) {
    if (trophy != null) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = trophy.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        painter = rememberAsyncImagePainter(trophy.pictureUrl),
                        contentDescription = "Trophy Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = trophy.description, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}



@Composable
fun AchievedTrophyItem(
    trophy: AchievedTrophy,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth() // occuperà tutta la larghezza disponibile
            .padding(horizontal = 16.dp, vertical = 8.dp) // padding esterno ridotto
            .clickable { onItemClick() },
        shape = RoundedCornerShape(15.dp),               // bordi meno arrotondati
        elevation = CardDefaults.cardElevation(4.dp),   // elevazione più bassa
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
        ) {
            if (!trophy.pictureUrl.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(trophy.pictureUrl),
                    contentDescription = "Mission Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(fraction = 1f)
                        .clip(RoundedCornerShape(7.dp))
                        .aspectRatio(1f)
                )
            } else {
                // Fallback se missionPic è null o vuota
                Image(
                    painter = painterResource(R.drawable.ic_placeholder),
                    contentDescription = "Trophy Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(fraction = 1f)
                        .clip(RoundedCornerShape(6.dp))
                        .aspectRatio(1f)
                )
            }
        }
    }
}


@Composable
fun NotAchievedTrophyItem(
    trophy: Trophy,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth() // occuperà tutta la larghezza disponibile
            .padding(horizontal = 16.dp, vertical = 8.dp) // padding esterno ridotto
            .clickable { onItemClick() },
        shape = RoundedCornerShape(15.dp),               // bordi meno arrotondati
        elevation = CardDefaults.cardElevation(4.dp),   // elevazione più bassa
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
        ) {
            if (!trophy.pictureUrl.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(trophy.pictureUrl),
                    contentDescription = "Mission Image",
                    contentScale = ContentScale.Crop,
                    colorFilter =  ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }),
                    modifier = Modifier
                        .fillMaxWidth(fraction = 1f)
                        .clip(RoundedCornerShape(7.dp))
                        .aspectRatio(1f)
                )
            } else {
                // Fallback se missionPic è null o vuota
                Image(
                    painter = painterResource(R.drawable.ic_placeholder),
                    contentDescription = "Trophy Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(fraction = 1f)
                        .clip(RoundedCornerShape(6.dp))
                        .aspectRatio(1f)
                )
            }
        }
    }
}