package com.ingegneriasoftware.ecoquest.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ShortText
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SortByAlpha
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ingegneriasoftware.ecoquest.ui.components.SectionHeader
import com.ingegneriasoftware.ecoquest.viewmodels.ProfileCompletionViewModel
import com.ingegneriasoftware.ecoquest.viewmodels.ProfileViewModel
import com.ingegneriasoftware.ecoquest.ui.components.CustomTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileChangeScreen(
    navController: NavController,
    viewModel: ProfileCompletionViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    val username by viewModel.username.collectAsState()
    val firstName by viewModel.firstName.collectAsState()
    val lastName by viewModel.lastName.collectAsState()
    val isUsernameAvailable by viewModel.isUsernameAvailable.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) { profileViewModel.loadProfile() }
    val profile by profileViewModel.profile.collectAsState()

    LaunchedEffect(profile) {
        profile?.let {
            viewModel.setUsername(it.username)
            viewModel.setFirstName(it.firstName ?: "")
            viewModel.setLastName(it.lastName ?: "")
        }
    }

    if (profile == null) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
        return
    }
    val currentProfile = profile

    // Determine if username has changed
    val isUsernameChanged = username != currentProfile?.username

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
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(Modifier.height(32.dp))

                // Header
                SectionHeader("Modifica profilo")

                Spacer(Modifier.height(32.dp))

                // Profile Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Username Field with availability indicator
                        Column {
                            CustomTextField(
                                value = username,
                                onValueChange = { viewModel.setUsername(it) },
                                label = "Username",
                                placeholder = profile!!.username,
                                leadingIcon = Icons.Outlined.Person,
                                trailingIcon = {
                                    when {
                                        !isUsernameChanged -> Icon(
                                            Icons.Default.Check,
                                            contentDescription = "Username attuale",
                                            tint = Color.Green
                                        )
                                        isUsernameAvailable == true -> Icon(
                                            Icons.Default.Check,
                                            contentDescription = "Username disponibile",
                                            tint = Color.Green
                                        )
                                        isUsernameAvailable == false -> Icon(
                                            Icons.Default.Close,
                                            contentDescription = "Username non disponibile",
                                            tint = Color.Red
                                        )
                                    }
                                }
                            )

                            if (isUsernameChanged && isUsernameAvailable == false) {
                                Text(
                                    text = "Username non disponibile",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }

                        // First Name Field
                        CustomTextField(
                            value = firstName,
                            onValueChange = { viewModel.setFirstName(it) },
                            label = "Nome",
                            placeholder = profile!!.firstName ?: "",
                            leadingIcon = Icons.AutoMirrored.Outlined.ShortText
                        )

                        // Last Name Field
                        CustomTextField(
                            value = lastName,
                            onValueChange = { viewModel.setLastName(it) },
                            label = "Cognome",
                            placeholder = profile!!.lastName ?: "",
                            leadingIcon = Icons.Outlined.SortByAlpha
                        )
                    }
                }
                Spacer(Modifier.height(32.dp))

                // Save Button
                Button(
                    onClick = {
                        viewModel.updateProfile(
                            onSuccess = {
                                profileViewModel.loadProfile()
                                navController.popBackStack()
                            },
                            onError = { error ->
                                viewModel.setErrorMessage(error)
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 4.dp
                    ),
                    enabled = (
                            (username == currentProfile?.username || isUsernameAvailable == true) &&
                                    (username != currentProfile?.username ||
                                            firstName != (currentProfile.firstName ?: "") ||
                                            lastName != (currentProfile.lastName ?: ""))
                            )
                ) {
                    Icon(Icons.Filled.Save, contentDescription = null)
                    Spacer(Modifier.width(12.dp))
                    Text("Salva Modifiche", fontWeight = FontWeight.Bold)
                }

                // Error Message
                errorMessage?.let {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.errorContainer,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}