package com.ingegneriasoftware.ecoquest.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ingegneriasoftware.ecoquest.R
import com.ingegneriasoftware.ecoquest.data.models.AchievedTrophy


//DA CANCELLARE SE OKK

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleTrophyScreen(
    navController: NavController
) {
    // Get the achieved trophy from navigation arguments
    //val trophy = navController.previousBackStackEntry?.savedStateHandle?.get<AchievedTrophy>("trophy")
    val trophy = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<AchievedTrophy>("trophy")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(trophy?.name ?: "Trophy Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (trophy == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Trophy not found", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Trophy Image
                Image(
                    painter = rememberAsyncImagePainter(trophy.pictureUrl ?: R.drawable.ic_placeholder),
                    contentDescription = "Trophy Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(MaterialTheme.shapes.medium)
                )

                Spacer(Modifier.height(16.dp))

                // Trophy Details
                Text(trophy.name, style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(8.dp))
                Text(trophy.description, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(16.dp))
                Text("Earned on: ${trophy.earnedAt}", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}