package com.ingegneriasoftware.ecoquest.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ingegneriasoftware.ecoquest.viewmodels.ProfileViewModel
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.ingegneriasoftware.ecoquest.ui.components.PersonalRecap
import com.ingegneriasoftware.ecoquest.ui.components.ProfilePic
import com.ingegneriasoftware.ecoquest.ui.components.parseDate
import com.ingegneriasoftware.ecoquest.ui.components.TrophyRecap
import com.ingegneriasoftware.ecoquest.viewmodels.TrophyViewModel
import kotlinx.coroutines.launch
import com.ingegneriasoftware.ecoquest.ui.components.SectionHeader
import kotlinx.io.IOException



@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
    trophyViewModel: TrophyViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> imageUri = uri }

    LaunchedEffect(Unit) { viewModel.loadProfile() }

    val profile by viewModel.profile.collectAsState()
    val achieved by trophyViewModel.achievedTrophies.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val picSize = screenWidth / 5

    if (profile == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return // Evita di continuare a eseguire il resto del composable
    }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SectionHeader("Profilo")
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = {
                                pickImageLauncher.launch("image/*")

                                },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AddAPhoto,
                                contentDescription = "Cambia immagine",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        imageUri?.let { uri ->
                            LaunchedEffect(uri) {
                                val byteArray: ByteArray? = try {
                                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
                                        inputStream.readBytes()
                                    }
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                    null
                                }
                                byteArray?.let {
                                    viewModel.uploadProfileImage(it)
                                }
                            }
                        }

                        IconButton(
                            onClick = { navController.navigate(
                                com.ingegneriasoftware.ecoquest.navigation.Screen.ProfileChange.route
                            ) },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ManageAccounts,
                                contentDescription = "Modifica dati",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        IconButton(
                            onClick = { coroutineScope.launch { viewModel.logout(navController) } },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Logout",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        ProfilePic(
                            profilePic = profile!!.profilePic,
                            modifier = Modifier
                                .size(picSize)
                                .clip(CircleShape)
                                .border(
                                    BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                                    CircleShape
                                )
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = profile!!.username,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.outline,
                            )
                            Text(
                                text = "${profile!!.firstName} ${profile!!.lastName}",
                                fontSize = 16.sp,
                            )
                            Text(
                                text = parseDate(profile!!.createdAt.toString()),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            // Personal recap
            item {
                SectionHeader(title = "Recap Punti")
                PersonalRecap(profile!!)
            }

            // Trophies
            item {
                SectionHeader(title = "Trofei")
                TrophyRecap(achieved)
            }
        }
    }

