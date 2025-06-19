package com.ingegneriasoftware.ecoquest.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ingegneriasoftware.ecoquest.viewmodels.ProfileCompletionViewModel
import com.ingegneriasoftware.ecoquest.navigation.Screen
import com.ingegneriasoftware.ecoquest.ui.components.CustomTextField
import com.ingegneriasoftware.ecoquest.ui.components.SectionHeader

@Composable
fun ProfileCompletionScreen(
    navController: NavController,
    viewModel: ProfileCompletionViewModel = hiltViewModel()
) {
    val username by viewModel.username.collectAsState()
    val firstName by viewModel.firstName.collectAsState()
    val lastName by viewModel.lastName.collectAsState()
    val isUsernameAvailable by viewModel.isUsernameAvailable.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(32.dp))

        // Header
        SectionHeader("Inserisci i tuoi dati!")

        Spacer(Modifier.height(32.dp))

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
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                CustomTextField(
                    value = username,
                    onValueChange = { viewModel.setUsername(it) },
                    label = "Username",
                    placeholder = "Username",
                    leadingIcon = Icons.Outlined.Email
                )
                if (isUsernameAvailable == false) {
                    Text(
                        text = "Username non disponibile",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                CustomTextField(
                    value = firstName,
                    onValueChange = { viewModel.setFirstName(it) },
                    label = "Nome",
                    placeholder = "Nome",
                    leadingIcon = Icons.Outlined.Password,
                )


                CustomTextField(
                    value = lastName,
                    onValueChange = { viewModel.setLastName(it) },
                    label = "Cognome",
                    placeholder = "Cognome",
                    leadingIcon = Icons.Outlined.Password,
                )
                Spacer(modifier = Modifier.height(12.dp))

                    // Save Button
                Button(
                    onClick = {
                        viewModel.saveProfile(
                            onSuccess = {
                                navController.navigate(Screen.Onboarding.route) {
                                    popUpTo(Screen.ProfileCompletion.route) { inclusive = true }
                                }
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
                        disabledContainerColor = MaterialTheme.colorScheme.inversePrimary,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    enabled = (
                            username.isNotBlank() &&
                            firstName.isNotBlank() &&
                            lastName.isNotBlank()
                            )
                    ) {
                        Text("Salva Profilo", fontWeight = FontWeight.Bold)
                    }
                Spacer(modifier = Modifier.height(16.dp))
                }
        }
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}










