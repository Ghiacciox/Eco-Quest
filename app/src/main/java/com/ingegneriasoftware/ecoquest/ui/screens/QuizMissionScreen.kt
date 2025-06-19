package com.ingegneriasoftware.ecoquest.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.ingegneriasoftware.ecoquest.data.models.Missions
import com.ingegneriasoftware.ecoquest.navigation.Screen
import com.ingegneriasoftware.ecoquest.viewmodels.MissionsViewModel
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizMission(
    navController: NavController,
    viewModel: MissionsViewModel = hiltViewModel()
) {
    // Recupera la missione dalla back stack
    val (mission, _) = remember {
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<Pair<Missions, Boolean>>("mission")
            ?: (null to false)
    }
    val listDomande = mission?.missionPrompt?.split("\n") ?: emptyList()

    // Stato per tracciare se l'utente ha risposto e se la risposta è corretta
    var answerResult by remember { mutableStateOf<Boolean?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(mission?.title ?: "Inizia la Quest!") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Sfondo
            val imagePainter = rememberAsyncImagePainter(
                "https://mnqyxftyucsmmnvuoyuw.supabase.co/storage/v1/object/public/mission.pics/no_quests.jpeg"
            )
            BackgroundFotoQuiz(imagePainter)

            // Contenitore centrale
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(0.7f),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                // Mostra quiz o risultato in base allo stato
                if (answerResult == null) {
                    // Quiz vero e proprio
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = mission?.description ?: "Domanda non disponibile",
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.padding(bottom = 24.dp),
                            color = MaterialTheme.colorScheme.outline,
                            textAlign = TextAlign.Center
                        )
                        QuizDomande(
                            listDomande = listDomande,
                            onAnswerSelected = { isCorrect ->
                                answerResult = isCorrect
                                if(answerResult as Boolean){
                                        mission?.let {
                                            viewModel.completeMission(it.id, it.points.toInt())
                                        }
                                }
                            }
                        )
                    }
                } else {
                    // Scheda di risultato
                    MissionResultCard(
                        verificationSuccess = answerResult!!,
                        navController = navController,
                        onRetakeMission = {
                            answerResult = null // reset per rifare il quiz
                        },
                        onExitMission = {
                            navController.navigate(Screen.Home.route)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun QuizDomande(
    listDomande: List<String>,
    modifier: Modifier = Modifier,
    onAnswerSelected: (isCorrect: Boolean) -> Unit
) {
    // Il primo elemento è la risposta corretta
    val correctAnswer = listDomande.firstOrNull()
        ?: error("La lista delle domande non può essere vuota")

    // Seleziona 3 distrattori e mescola
    val answerOptions = remember(listDomande) {
        val distractors = listDomande.drop(1).shuffled().take(3)
        (distractors + correctAnswer).shuffled()
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        answerOptions.forEach { option ->
            Button(
                onClick = { onAnswerSelected(option == correctAnswer) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun BackgroundFotoQuiz(imgPainter: AsyncImagePainter) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(MaterialTheme.shapes.large)
    ) {
        Image(
            painter = imgPainter,
            contentDescription = "Immagine Sfondo Quiz",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
