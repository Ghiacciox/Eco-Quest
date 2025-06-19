package com.ingegneriasoftware.ecoquest.data.repositories

import android.content.Context
import com.ingegneriasoftware.ecoquest.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

// Repository per la gestione dell'autenticazione degli utenti.
// Utilizza Supabase per le operazioni di login, registrazione e logout.
// Gestisce anche lo stato di autenticazione tramite SharedPreferences e StateFlow.
class AuthRepository @Inject constructor(
    private val supabaseClient: SupabaseClient, // Client Supabase per interagire con il backend.
    private val context: Context // Contesto Android per accedere a SharedPreferences.
) {

    // Stato interno che indica se l'utente è autenticato.
    private val _isLoggedIn = MutableStateFlow(false)
    // Stato pubblico esposto come StateFlow per osservare i cambiamenti.
    val isLoggedIn: StateFlow<Boolean> get() = _isLoggedIn

    init {
        // Controlla lo stato di autenticazione al momento della creazione del repository.
        checkLoggedInState()
    }

    // Metodo per registrare un nuovo utente con email e password.
    suspend fun signup(email: String, password: String): Result<Unit> {
        return try {
            // Utilizza Supabase per registrare l'utente.
            supabaseClient.client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            Result.success(Unit) // Operazione riuscita.
        } catch (e: Exception) {
            Result.failure(e) // Gestisce eventuali errori.
        }
    }

    // Metodo per effettuare il login di un utente con email e password.
    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            // Utilizza Supabase per effettuare il login.
            supabaseClient.client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            // Salva il token di autenticazione nella memoria locale.
            saveAuthToken(supabaseClient.client.auth.currentAccessTokenOrNull())
            _isLoggedIn.value = true // Aggiorna lo stato di autenticazione.
            Result.success(Unit) // Operazione riuscita.
        } catch (e: Exception) {
            Result.failure(e) // Gestisce eventuali errori.
        }
    }

    // Metodo per effettuare il logout dell'utente.
    suspend fun logout() {
        // Effettua il logout tramite Supabase.
        supabaseClient.client.auth.signOut()
        // Rimuove il token di autenticazione dalla memoria locale.
        clearAuthToken()
        _isLoggedIn.value = false // Aggiorna lo stato di autenticazione.
    }

    // Salva il token di autenticazione nelle SharedPreferences.
    private fun saveAuthToken(token: String?) {
        val sharedPref = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("auth_token", token)
            apply()
        }
    }

    // Rimuove il token di autenticazione dalle SharedPreferences.
    private fun clearAuthToken() {
        val sharedPref = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove("auth_token")
            apply()
        }
    }

    // Controlla se l'utente è già autenticato leggendo il token dalle SharedPreferences.
    private fun checkLoggedInState() {
        val sharedPref = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = sharedPref.getString("auth_token", null)
        _isLoggedIn.value = token != null // Aggiorna lo stato in base alla presenza del token.
    }
}