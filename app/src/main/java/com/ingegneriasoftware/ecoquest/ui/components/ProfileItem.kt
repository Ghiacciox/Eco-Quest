package com.ingegneriasoftware.ecoquest.ui.components

// Import delle librerie necessarie
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ingegneriasoftware.ecoquest.data.models.Profile
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import com.ingegneriasoftware.ecoquest.data.models.AchievedTrophy
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

// Immagine utente circolare
@Composable
fun ProfilePic(
    profilePic: String?, // URL dell'immagine del profilo
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape, // Forma dell'immagine (default: cerchio)
    borderColor: Color = MaterialTheme.colorScheme.primary, // Colore del bordo
    borderWidth: Dp = 4.dp // Spessore del bordo
) {
    Box(
        modifier = modifier
            .clip(shape) // Applica la forma
            .border(borderWidth, borderColor, shape) // Aggiunge il bordo
            .background(MaterialTheme.colorScheme.surface), // Sfondo
        contentAlignment = Alignment.Center // Centra il contenuto
    ) {
        if (!profilePic.isNullOrEmpty()) {
            // Mostra l'immagine del profilo
            Image(
                painter = rememberAsyncImagePainter(profilePic),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape),
                contentScale = ContentScale.Crop
            )
        } else {
            // Mostra un'icona di default se l'immagine non è disponibile
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Default profile",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape),
            )
        }
    }
}

// Immagine blurrata come sfondo
@Composable
fun ImageCard(profile: Profile) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp) // Elevazione della card
    ) {
        Image(
            painter = rememberAsyncImagePainter(profile.profilePic),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .blur(
                    radiusX = 10.dp, // Raggio di sfocatura orizzontale
                    radiusY = 10.dp, // Raggio di sfocatura verticale
                    edgeTreatment = BlurredEdgeTreatment(RoundedCornerShape(8.dp)) // Trattamento dei bordi
                )
        )
    }
}

// Riepilogo personale con statistiche
@Composable
fun PersonalRecap(profile: Profile) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp), // Angoli arrotondati
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer // Colore di sfondo
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly, // Spaziatura uniforme
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) // Padding interno
        ) {
            // Mostra le statistiche settimanali, mensili, giornaliere e totali
            StatItem(value = "${profile.weeklyPoints}", label = "Settimana")
            StatItem(value = "${profile.monthlyPoints}", label = "Mese")
            StatItem(value = "${profile.dailyPoints}", label = "Giornalieri")
            StatItem(value = "${profile.points}", label = "Totali")
        }
    }
}

// Elemento per mostrare un valore e una label
@Composable
fun StatItem(value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally // Centra il contenuto
    ) {
        Text(
            text = value,
            fontSize = 20.sp, // Dimensione del testo
            fontWeight = FontWeight.Bold, // Grassetto
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

// Riepilogo dei trofei ottenuti
@Composable
fun TrophyRecap(trophies: List<AchievedTrophy>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp), // Margine verticale
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Padding interno
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp) // Spaziatura verticale
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(), // Larghezza completa
                horizontalArrangement = Arrangement.spacedBy(
                    space = 12.dp,
                    alignment = Alignment.CenterHorizontally // Centra gli elementi
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(trophies.size) { index ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.size(56.dp) // Dimensione del trofeo
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(trophies[index].pictureUrl),
                                contentDescription = "Trophy Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(7.dp))
                                    .aspectRatio(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Intestazione di una sezione
@Composable
fun SectionHeader(
    title: String, // Titolo della sezione
    color: Color = MaterialTheme.colorScheme.tertiary // Colore del testo
) {
    Text(
        text = title,
        style = MaterialTheme.typography.displaySmall,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = Modifier.padding(vertical = 8.dp) // Margine verticale
    )
}

// Campo di testo personalizzato
@Composable
fun CustomTextField(
    value: String, // Valore del campo
    onValueChange: (String) -> Unit, // Callback per il cambiamento del valore
    label: String, // Etichetta del campo
    placeholder: String, // Testo segnaposto
    leadingIcon: ImageVector, // Icona iniziale
    isPassword: Boolean = false, // Indica se è un campo password
    trailingIcon: (@Composable () -> Unit)? = null // Icona finale opzionale
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    var passwordVisible by remember { mutableStateOf(false) } // Stato per mostrare/nascondere la password

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )

        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = if (isFocused)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            },
            trailingIcon = when {
                isPassword -> {
                    {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            val icon = if (passwordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff
                            val desc = if (passwordVisible) "Nascondi password" else "Mostra password"
                            Icon(imageVector = icon, contentDescription = desc)
                        }
                    }
                }
                trailingIcon != null -> trailingIcon // Usa l'icona finale personalizzata
                else -> null
            },
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            singleLine = true,
            interactionSource = interactionSource,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            visualTransformation = if (isPassword && !passwordVisible)
                PasswordVisualTransformation()
            else
                VisualTransformation.None
        )
    }
}






