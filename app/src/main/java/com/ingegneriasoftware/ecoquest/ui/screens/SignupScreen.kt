package com.ingegneriasoftware.ecoquest.ui.screens

import android.content.Intent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ingegneriasoftware.ecoquest.navigation.Screen
import com.ingegneriasoftware.ecoquest.viewmodels.SignupViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Password
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.ingegneriasoftware.ecoquest.ui.components.CustomTextField
import com.ingegneriasoftware.ecoquest.ui.components.SectionHeader
import androidx.core.net.toUri

@Composable
fun SignupScreen(
    navController: NavController,
    viewModel: SignupViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var privacyAccepted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(32.dp))

        // Header
        SectionHeader("Ti diamo il benvenuto su EcoQuest")

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


                CustomTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Conferma Password",
                    placeholder = "Conferma Password",
                    leadingIcon = Icons.Outlined.Password,
                    isPassword = true
                )

                Row{
                    Checkbox(
                        checked = privacyAccepted,
                        onCheckedChange = { privacyAccepted = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.inversePrimary
                        )
                    )

                    val context = LocalContext.current
                    TextButton(
                        onClick = {
                            val urlIntent = Intent(Intent.ACTION_VIEW, "https://ecoquest9.wordpress.com/about/".toUri())
                            context.startActivity(urlIntent)
                                },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            "Clicca per vedere  l'informativa sulla privacy",
                            style = MaterialTheme.typography.labelLarge
                                .copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    }

                Button(
                    onClick = {
                        if (password != confirmPassword) {
                            errorMessage = "Le password non coincidono"
                        } else {
                            viewModel.signup(
                                email = email,
                                password = password,
                                onSuccess = {
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(Screen.Signup.route) { inclusive = true }
                                    }
                                },
                                onError = { error ->
                                    errorMessage = error
                                }
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = MaterialTheme.colorScheme.inversePrimary,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    enabled = (
                            email.isNotBlank() &&
                            password.isNotBlank() &&
                            privacyAccepted
                            )
                ) {
                    Text(
                        "Registrati !",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                TextButton(
                    onClick = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Signup.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Hai già un account? Accedi!",
                    style = MaterialTheme.typography.labelLarge
                        .copy(fontWeight = FontWeight.Bold)
                    )
                }

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