package com.ingegneriasoftware.ecoquest.data.repositories

import com.ingegneriasoftware.ecoquest.SupabaseClient
import com.ingegneriasoftware.ecoquest.data.models.CompletedMission
import com.ingegneriasoftware.ecoquest.data.models.Missions
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Indica che questa classe è un singleton (utile per repository condivisi).
class MissionsRepository @Inject constructor(
    private val supabaseClient: SupabaseClient // Inietta il client Supabase per interagire con il backend.
) {

    /**************************************************************
     * Funzione che ritorna le missioni non completate dall'utente *
     *************************************************************/
    suspend fun getUncompletedMissions(): List<Missions> {
        // Ottiene l'ID dell'utente corrente come stringa.
        val userIdString = supabaseClient.client.auth.currentUserOrNull()?.id ?: return emptyList()

        // Converte l'ID utente in un oggetto UUID.
        val userId = UUID.fromString(userIdString)

        // Crea i parametri per la chiamata RPC.
        val rpcParams = buildJsonObject {
            put("p_user_id", JsonPrimitive(userId.toString())) // Aggiunge l'ID utente ai parametri RPC.
        }

        return try {
            // Esegue la funzione RPC "get_uncompleted_missions" sul backend Supabase.
            val response = supabaseClient.client.postgrest.rpc("get_uncompleted_missions", rpcParams)

            // Decodifica la risposta in una lista di oggetti `Missions`.
            response.decodeList<Missions>()
        } catch (e: Exception) {
            // Gestisce eventuali eccezioni durante la chiamata RPC.
            e.printStackTrace()
            emptyList() // Restituisce una lista vuota in caso di errore.
        }
    }

    /*********************************************************
     * Funzione che ritorna le missioni completate dall'utente *
     ********************************************************/
    suspend fun getCompletedMissions(): List<CompletedMission> {
        // Ottiene l'ID dell'utente corrente come stringa.
        val userIdString = supabaseClient.client.auth.currentUserOrNull()?.id ?: return emptyList()

        // Converte l'ID utente in un oggetto UUID.
        val userId = UUID.fromString(userIdString)

        // Crea i parametri per la chiamata RPC.
        val rpcParams = buildJsonObject {
            put("p_user_id", JsonPrimitive(userId.toString())) // Aggiunge l'ID utente ai parametri RPC.
        }

        return try {
            // Esegue la funzione RPC "get_completed_missions" sul backend Supabase.
            val response = supabaseClient.client.postgrest.rpc("get_completed_missions", rpcParams)

            // Decodifica la risposta in una lista di oggetti `CompletedMission`.
            response.decodeList<CompletedMission>()
        } catch (e: Exception) {
            // Gestisce eventuali eccezioni durante la chiamata RPC.
            e.printStackTrace()
            emptyList() // Restituisce una lista vuota in caso di errore.
        }
    }

    /*************************************************
     * Funzione che permette di completare una missione *
     ************************************************/
    @Suppress("PropertyName")
    @Serializable
    data class UserMission(
        val user_id: String, // Usa String invece di UUID per facilitare la serializzazione.
        val mission_id: Int
    )

    suspend fun completeMission(missionId: Int, point: Int) {
        // Ottiene l'ID dell'utente corrente.
        val userIdString = supabaseClient.client.auth.currentUserOrNull()?.id
        val userId = UUID.fromString(userIdString)

        // Prepara i dati da inserire.
        val newUserMission = UserMission(
            user_id = userIdString.toString(), // Usa l'ID utente come String.
            mission_id = missionId
        )

        // Inserisce la nuova missione completata nella tabella `user_missions`.
        supabaseClient.client.postgrest
            .from("user_missions")
            .insert(newUserMission)

        // Aggiunge i punti al profilo dell'utente.
        insertPoint(point, userId)
    }

    // Funzione privata per aggiungere punti al profilo dell'utente.
    private suspend fun insertPoint(point: Int, userId: UUID) {
        // Crea i parametri per la chiamata RPC.
        val rpcParams = buildJsonObject {
            put("input_user_id", JsonPrimitive(userId.toString())) // Aggiunge l'ID utente ai parametri RPC.
            put("input_points", JsonPrimitive(point)) // Aggiunge i punti ai parametri RPC.
        }

        try {
            // Esegue la funzione RPC "add_points_to_profile" sul backend Supabase.
            supabaseClient.client.postgrest.rpc("add_points_to_profile", rpcParams)
        } catch (e: Exception) {
            // Gestisce eventuali eccezioni durante la chiamata RPC.
            e.printStackTrace()
        }
    }

    /*********************************************************
     * Funzione per ottenere la cronologia delle missioni completate *
     ********************************************************/
    suspend fun getMissionHistory(): List<CompletedMission> {
        return try {
            // Esegue la funzione RPC "get_my_mission_history" sul backend Supabase.
            supabaseClient.client.postgrest
                .rpc("get_my_mission_history")
                .decodeList<CompletedMission>()
        } catch (e: Exception) {
            // Gestisce eventuali eccezioni durante la chiamata RPC.
            e.printStackTrace()
            emptyList() // Restituisce una lista vuota in caso di errore.
        }
    }
}