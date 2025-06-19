package com.ingegneriasoftware.ecoquest.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ingegneriasoftware.ecoquest.R
import com.ingegneriasoftware.ecoquest.data.models.Missions
import com.ingegneriasoftware.ecoquest.ui.components.CameraFunctions
import com.ingegneriasoftware.ecoquest.data.repositories.GeminiApi
import com.ingegneriasoftware.ecoquest.navigation.Screen
import com.ingegneriasoftware.ecoquest.viewmodels.MissionsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraMission(
    navController: NavController,
    viewModel: MissionsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val (mission, _) = remember {
        navController.previousBackStackEntry?.savedStateHandle?.get<Pair<Missions, Boolean>>("mission")
            ?: (null to false)
    }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showCamera by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }
    var verificationResult by remember { mutableStateOf<String?>(null) }
    var verificationSuccess by remember { mutableStateOf(false) }

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
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (showCamera) {
                CameraFunctions.CameraView(
                    onImageCaptured = {
                        imageUri = it
                        showCamera = false
                    }
                )
            }else{
                imageUri?.let { uri ->
                    LaunchedEffect(uri) {
                        if (!verificationSuccess) {
                            isLoading = true
                            val geminiApi = GeminiApi()
                            verificationResult = mission?.missionPrompt?.let { prompt ->
                                geminiApi.geminiAnalizer(context, uri, prompt)
                            }
                            isLoading = false

                            verificationResult?.let { result ->
                                verificationSuccess = result.contains("yes", ignoreCase = true) ||
                                        result.contains("success", ignoreCase = true)

                                if (verificationSuccess) {
                                    mission?.let {
                                        viewModel.completeMission(it.id, it.points.toInt())
                                    }
                                }
                            }
                        }
                    }
                    val bitmap = remember {
                        BitmapFactory.decodeFile(uri.path)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                    BackgroundFoto(bitmap)
                        Box( modifier = Modifier
                            .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .fillMaxHeight(0.7f),
                                elevation = CardDefaults.cardElevation(6.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            ){
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ){
                                    if (isLoading) {
                                        //spinner di caricamento
                                        CircularProgressIndicator(
                                            modifier = Modifier.fillMaxSize(0.4f),
                                            color = MaterialTheme.colorScheme.primary,
                                            trackColor = MaterialTheme.colorScheme.outline,
                                        )
                                    } else {
                                        //schermata dpo verifica
                                        verificationResult?.let {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp),
                                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                MissionResultCard(
                                                    verificationSuccess = verificationSuccess,
                                                    navController = navController,
                                                    onRetakeMission = {
                                                        imageUri = null
                                                        showCamera = true
                                                        verificationResult = null
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
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BackgroundFoto(bitmap: Bitmap) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(MaterialTheme.shapes.large),
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
        ) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Immagine catturata",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp)
            )
        }
    }
}



@Composable
fun MissionResultCard(
    verificationSuccess: Boolean = false,
    navController: NavController,
    onRetakeMission: () -> Unit,
    onExitMission: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (verificationSuccess) "Quest Completata!" else "Quest Fallita!",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(8.dp)
        )

        Image(
            painter = painterResource(
                id = if (verificationSuccess) R.drawable.successquest else R.drawable.failedmission
            ),
            contentDescription = if (verificationSuccess) "Quest completata!" else "Quest Fallita!",
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Fit
        )

        Text(
            text = if (verificationSuccess)
                "Complimenti hai completato la tua Quest!"
            else
                "Mi dispiace non hai completato la Quest! Riprova!.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )

        if (verificationSuccess) {
            Button(
                onClick = { navController.navigate(Screen.Home.route) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Chiudi",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    onClick = onRetakeMission,
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Riprova",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                IconButton(
                    onClick = onExitMission,
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            color = MaterialTheme.colorScheme.error,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Esci",
                        tint = MaterialTheme.colorScheme.onError,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}
