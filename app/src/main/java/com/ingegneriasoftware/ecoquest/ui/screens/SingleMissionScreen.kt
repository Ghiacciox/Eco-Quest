package com.ingegneriasoftware.ecoquest.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ingegneriasoftware.ecoquest.R
import com.ingegneriasoftware.ecoquest.data.models.Missions
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import com.ingegneriasoftware.ecoquest.ui.components.SectionHeader
import com.ingegneriasoftware.ecoquest.viewmodels.MissionsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleMissionScreen(
    navController: NavController,
    viewModel: MissionsViewModel = hiltViewModel()
) {
    // Get mission data from navigation arguments
    val (mission, isCompleted) = remember {
        navController.previousBackStackEntry?.savedStateHandle?.get<Pair<Missions, Boolean>>("mission")
            ?: (null to false)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (mission == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Mission not found", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Mission Image
                Image(
                    painter = rememberAsyncImagePainter(mission.missionPic ?: R.drawable.ic_placeholder),
                    contentDescription = "Mission Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LocalConfiguration.current.screenHeightDp.dp / 3)
                        .clip(MaterialTheme.shapes.medium)
                )
                Spacer(Modifier.height(32.dp))
                InfoCardMission(mission, isCompleted, navController, viewModel)
            }
        }
    }
}

@Composable
fun InfoCardMission(
    mission: Missions,
    isCompleted: Boolean,
    navController: NavController,
    viewModel: MissionsViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Mission Details
            SectionHeader(
                mission.title,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(Modifier.height(8.dp))
            Text(
                mission.description ?: "No description available",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(16.dp))
            Text("Points: ${mission.points}", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(16.dp))

            if (!isCompleted) {
                Button(
                    onClick = { //qua lanciata missione
                        if (mission.camera) {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "mission",
                                mission to false
                            )
                            navController.navigate("CameraMission")
                        }else if (mission.camera) {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "mission",
                                mission to false
                            )
                            navController.navigate("CameraMission")
                        }else {
                            if(mission.missionPrompt.isNullOrEmpty()) {
                                viewModel.completeMission(mission.id, mission.points.toInt())
                                navController.popBackStack()
                            }else{
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "mission",
                                    mission to true
                                )
                                navController.navigate("QuizMission")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.outline,
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 4.dp
                    )
                ) {
                    Text(
                        if (mission.camera) "Inizia la missione"
                        else "Completa la missione"
                    )
                }
            } else {
                Text(
                    "You already completed this mission",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}