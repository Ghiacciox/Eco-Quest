package com.ingegneriasoftware.ecoquest.ui.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ingegneriasoftware.ecoquest.R
import com.ingegneriasoftware.ecoquest.navigation.Screen

/**
 * Schermata di onboarding che guida l'utente attraverso una serie di slide introduttive.
 * @param navController Controller di navigazione per gestire la transizione tra schermate.
 * @param onFinish Callback eseguito al termine dell'onboarding, di default naviga alla schermata Home.
 */
@Composable
fun OnboardingScreen(
    navController: NavController,
    onFinish: () -> Unit = { navController.navigate(Screen.Home.route) }
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Lista delle pagine per l'onboarding
        val pages = listOf(
            Slide(
                title = "La Tua Guida a ECO-Quest: Inizia Ora!",
                content = "Benvenuto/a in ECO-Quest! La tua avventura per un futuro più verde inizia qui. Partecipa a missioni ambientali quotidiane e ricevi ricompense per il tuo impegno.",
                imageRes = R.drawable.splash_icon
            ),
            Slide(
                title = "Cos'è ECO-Quest?",
                imageRes = R.drawable.guida
            ),
            Slide(
                title = "Ecco le funzioni di eco Quest!",
                imageRes = R.drawable.guida2
            ),
            Slide(
                title = "Inizia la tua avventura! Prova subito le Quest!",
                imageRes = R.drawable.inizia
            )
        )

        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f,
            pageCount = { 4 }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Pager per lo scorrimento delle slide
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                PageContent(
                    slide = pages[page],
                    pagerState = pagerState,
                    onFinish = onFinish
                )
            }


            // Indicatori della pagina corrente
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pagerState.pageCount) { page ->
                    val isSelected = pagerState.currentPage == page
                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 12.dp else 8.dp)
                            .padding(horizontal = 4.dp)
                            .background(
                                color = if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                                shape = MaterialTheme.shapes.small
                            )
                    )
                }
            }
        }
    }
}


/**
 * Composable che rappresenta il contenuto di una singola pagina dell'onboarding.
 * @param slide Oggetto `Slide` che contiene i dati della pagina (titolo, immagine, ecc.).
 * @param pagerState Stato del pager per determinare la pagina corrente.
 * @param onFinish Callback eseguito quando l'utente termina l'onboarding.
 */
@Composable
fun PageContent(slide: Slide, pagerState: PagerState, onFinish: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp)) // Sposta il titolo più in basso

        Text(
            text = slide.title,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        slide.imageRes?.let { res ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = res),
                    contentDescription = slide.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }
        }

        if (pagerState.currentPage == 3) {
            Button(
                onClick = onFinish,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Inizia",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Classe dati che rappresenta una singola slide dell'onboarding.
 * @param title Titolo della slide.
 * @param content Contenuto testuale opzionale della slide.
 * @param bulletPoints Elenco puntato opzionale per descrivere dettagli della slide.
 * @param imageRes Risorsa immagine opzionale associata alla slide.
 */
data class Slide(
    val title: String,
    val content: String? = null,
    val bulletPoints: List<String>? = null,
    val imageRes: Int? = null        // Aggiunta risorsa immagine opzionale
)

