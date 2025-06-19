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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ingegneriasoftware.ecoquest.navigation.Screen
import com.ingegneriasoftware.ecoquest.ui.components.CustomTextField
import com.ingegneriasoftware.ecoquest.ui.components.SectionHeader
import com.ingegneriasoftware.ecoquest.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(32.dp))

        // Header
        SectionHeader("EcoQuest")

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
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    placeholder = "Email",
                    leadingIcon = Icons.Outlined.Email
                )

                CustomTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    placeholder = "Password",
                    leadingIcon = Icons.Outlined.Password,
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        viewModel.login(
                            email = email,
                            password = password,
                            onSuccess = {
                                // No need to navigate here; navigation is handled in the ViewModel
                            },
                            onError = { error ->
                                errorMessage = error
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = MaterialTheme.colorScheme.inversePrimary,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    enabled = (
                            email.isNotBlank() && password.isNotBlank()
                            )
                ) {
                    Text(
                        "Login",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                TextButton(
                    onClick = {
                        // Navigate back to the Login screen
                        navController.navigate(Screen.Signup.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Non hai un account? Registrati!",
                        style = MaterialTheme.typography.labelLarge
                            .copy(fontWeight = FontWeight.Bold),
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        errorMessage?.let {
            Text(
                text = if (
                    it.startsWith("invalid_credentials")
                    ) "Credenziali non valide. Riprova." else it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}