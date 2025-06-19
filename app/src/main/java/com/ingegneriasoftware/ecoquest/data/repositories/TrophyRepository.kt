package com.ingegneriasoftware.ecoquest.data.repositories

import com.ingegneriasoftware.ecoquest.SupabaseClient
import com.ingegneriasoftware.ecoquest.data.models.AchievedTrophy
import com.ingegneriasoftware.ecoquest.data.models.Trophy
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Indica che questa classe è un singleton, utile per repository condivisi.
class TrophyRepository @Inject constructor(
    private val supabaseClient: SupabaseClient // Inietta il client Supabase per interagire con il backend.
) {

    /****************************************************
     * Recupera la lista dei trofei ottenuti dall'utente *
     ***************************************************/
    suspend fun getAchievedTrophies(): List<AchievedTrophy> {
        // Ottiene l'ID dell'utente corrente come stringa.
        val userIdString = supabaseClient.client.auth.currentUserOrNull()?.id

        // Converte l'ID utente in un oggetto UUID.
        val userId = UUID.fromString(userIdString)

        // Crea i parametri per la chiamata RPC.
        val rpcParams = buildJsonObject {
            put("p_user_id", JsonPrimitive(userId.toString())) // Aggiunge l'ID utente ai parametri RPC.
        }

        return try {
            // Esegue la funzione RPC "get_achieved_trophies" sul backend Supabase.
            val response = supabaseClient.client.postgrest.rpc("get_achieved_trophies", rpcParams)
            // Decodifica la risposta in una lista di oggetti `AchievedTrophy`.
            response.decodeList<AchievedTrophy>()
        } catch (e: Exception) {
            // Gestisce eventuali eccezioni durante la chiamata RPC.
            e.printStackTrace()
            emptyList() // Restituisce una lista vuota in caso di errore.
        }
    }

    /************************************************************
     * Recupera la lista dei trofei non ancora ricevuti dall'utente *
     ***********************************************************/
    suspend fun getTrophies(): List<Trophy> {
        // Ottiene l'ID dell'utente corrente come stringa.
        val userIdString = supabaseClient.client.auth.currentUserOrNull()?.id

        // Converte l'ID utente in un oggetto UUID.
        val userId = UUID.fromString(userIdString)

        // Crea i parametri per la chiamata RPC.
        val rpcParams = buildJsonObject {
            put("p_user_id", JsonPrimitive(userId.toString())) // Aggiunge l'ID utente ai parametri RPC.
        }

        return try {
            // Esegue la funzione RPC "get_not_received_trophies" sul backend Supabase.
            val response = supabaseClient.client.postgrest.rpc("get_not_received_trophies", rpcParams)
            // Decodifica la risposta in una lista di oggetti `Trophy`.
            response.decodeList<Trophy>()
        } catch (e: Exception) {
            // Gestisce eventuali eccezioni durante la chiamata RPC.
            e.printStackTrace()
            emptyList() // Restituisce una lista vuota in caso di errore.
        }
    }
}