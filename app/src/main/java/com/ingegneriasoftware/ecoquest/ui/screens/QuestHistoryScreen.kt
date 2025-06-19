package com.ingegneriasoftware.ecoquest.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ingegneriasoftware.ecoquest.ui.components.HistoryMissionItem
import com.ingegneriasoftware.ecoquest.viewmodels.QuestHistoryViewModel
import com.ingegneriasoftware.ecoquest.ui.components.parseDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestHistoryScreen(
    navController: NavController,
    viewModel: QuestHistoryViewModel = hiltViewModel()
) {
    val groupedMissions by viewModel.groupedMissions.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Storico Quest",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                },
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
        Column(Modifier.padding(innerPadding)) {
            if (groupedMissions.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No mission history yet")
                }
            } else {
                LazyColumn(Modifier.padding(horizontal = 16.dp)) {
                    groupedMissions.forEach { (date, missions) ->
                        item {
                            Text(
                                text = parseDate(date),
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(vertical = 8.dp),
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.tertiary,
                                thickness = 1.dp,
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(40.dp))
                        }
                        items(missions) { mission ->
                            HistoryMissionItem(mission) // Using the new non-clickable component
                            Spacer(Modifier.height(8.dp))
                        }
                        item{
                            Spacer(Modifier.height(150.dp))
                        }
                    }
                }
            }
        }
    }
}


