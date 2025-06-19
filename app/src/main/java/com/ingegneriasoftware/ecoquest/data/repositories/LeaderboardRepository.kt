package com.ingegneriasoftware.ecoquest.data.repositories

import com.ingegneriasoftware.ecoquest.SupabaseClient
import com.ingegneriasoftware.ecoquest.data.models.LeaderboardEntry
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Indica che questa classe è un singleton (utile per repository condivisi).
class LeaderboardRepository @Inject constructor(
    private val supabaseClient: SupabaseClient // Inietta il client Supabase per interagire con il backend.
) {

    /**
     * Recupera i primi 5 utenti per un determinato periodo.
     * @param period Il periodo per cui recuperare i dati (es. "daily", "weekly").
     * @return Una lista di oggetti `LeaderboardEntry` rappresentanti i migliori utenti.
     */
    suspend fun getTopUsers(period: String): List<LeaderboardEntry> {
        // Crea i parametri per la chiamata RPC.
        val rpcParams = buildJsonObject {
            put("input_period", JsonPrimitive(period)) // Aggiunge il periodo ai parametri RPC.
        }

        return try {
            // Esegue la funzione RPC "get_top_users" sul backend Supabase.
            val response = supabaseClient.client.postgrest.rpc("get_top_users", rpcParams)

            // Decodifica la risposta in una lista di oggetti `LeaderboardEntry`.
            response.decodeList<LeaderboardEntry>()
        } catch (e: Exception) {
            // Gestisce eventuali eccezioni durante la chiamata RPC.
            e.printStackTrace()
            emptyList() // Restituisce una lista vuota in caso di errore.
        }
    }

    /**
     * Recupera gli utenti circostanti (2 sopra e 2 sotto) rispetto all'utente corrente per un determinato periodo.
     * @param period Il periodo per cui recuperare i dati (es. "daily", "weekly").
     * @return Una lista di oggetti `LeaderboardEntry` rappresentanti gli utenti circostanti.
     */
    suspend fun getSurroundingUsers(period: String): List<LeaderboardEntry> {
        // Ottiene l'ID dell'utente corrente come stringa.
        val userIdString = supabaseClient.client.auth.currentUserOrNull()?.id ?: return emptyList()

        // Converte l'ID utente in un oggetto UUID.
        val userId = UUID.fromString(userIdString)

        // Crea i parametri per la chiamata RPC.
        val rpcParams = buildJsonObject {
            put("p_user_id", JsonPrimitive(userId.toString())) // Aggiunge l'ID utente ai parametri RPC.
            put("p_period", JsonPrimitive(period)) // Aggiunge il periodo ai parametri RPC.
        }

        return try {
            // Esegue la funzione RPC "get_surrounding_users" sul backend Supabase.
            val response = supabaseClient.client.postgrest.rpc("get_surrounding_users", rpcParams)

            // Decodifica la risposta in una lista di oggetti `LeaderboardEntry`.
            response.decodeList<LeaderboardEntry>()
        } catch (e: Exception) {
            // Gestisce eventuali eccezioni durante la chiamata RPC.
            e.printStackTrace()
            emptyList() // Restituisce una lista vuota in caso di errore.
        }
    }
}