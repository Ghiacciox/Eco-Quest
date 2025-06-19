package com.ingegneriasoftware.ecoquest.data.repositories

import com.ingegneriasoftware.ecoquest.SupabaseClient
import com.ingegneriasoftware.ecoquest.data.models.Profile
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Indica che questa classe è un singleton, utile per repository condivisi.
class ProfileRepository @Inject constructor(
    private val supabaseClient: SupabaseClient // Inietta il client Supabase per interagire con il backend.
) {

    /**
     * Verifica se il profilo dell'utente esiste.
     * @return `true` se il profilo esiste, altrimenti `false`.
     */
    suspend fun checkProfile(): Boolean {
        return try {
            supabaseClient.client
                .from("profiles")
                .select()
                .decodeSingleOrNull<Profile>() != null
        } catch (_: Exception) {
            false // Gestione degli errori con un fallback sicuro.
        }
    }

    /**
     * Recupera il profilo dell'utente corrente.
     * @return Un oggetto `Profile` contenente i dati del profilo.
     */
    suspend fun getProfile(): Profile {
        return supabaseClient.client.from("profiles").select().decodeSingle<Profile>()
    }

    /**
     * Modello per salvare i dati del profilo.
     */
    @Suppress("PropertyName")
    @Serializable
    data class SaveProfile(
        val user_id: String, // Usa String invece di UUID per facilitare la serializzazione.
        val username: String,
        val first_name: String,
        val last_name: String,
        val profile_picture: String? = null
    )

    /**
     * Salva le informazioni del profilo durante il completamento.
     * @param username Nome utente.
     * @param firstName Nome.
     * @param lastName Cognome.
     * @param profilePicture URL dell'immagine del profilo (opzionale).
     * @return Un `Result` che indica il successo o il fallimento.
     */
    suspend fun saveProfileCompletion(
        username: String,
        firstName: String,
        lastName: String,
        profilePicture: String? = null
    ): Result<Unit> {
        val userIdString = supabaseClient.client.auth.currentUserOrNull()?.id
            ?: return Result.failure(Exception("User not logged in"))

        val newProfile = SaveProfile(
            user_id = userIdString,
            username = username,
            first_name = firstName,
            last_name = lastName,
            profile_picture = profilePicture
        )

        return try {
            supabaseClient.client.from("profiles").insert(newProfile)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Carica l'immagine del profilo su Supabase Storage.
     * @param imageBytes L'immagine in formato ByteArray.
     * @return Un `Result` contenente l'URL pubblico se l'upload ha successo, oppure un errore.
     */
    suspend fun uploadProfileImage(imageBytes: ByteArray): Result<String> {
        val bucketName = "profile_picture"
        val userIdString = supabaseClient.client.auth.currentUserOrNull()?.id
        val userId = UUID.fromString(userIdString)

        val timestamp = System.currentTimeMillis()
        val newFilename = "$userId/$timestamp.png"

        val bucket = supabaseClient.client.storage.from(bucketName)

        return try {
            // Lista tutti i file nella cartella dell'utente e rimuove quelli vecchi.
            val listResponse = bucket.list(prefix = "$userId/")
            val toDelete = listResponse
                .map { "$userId/${it.name}" }
                .filter { it != newFilename }
            if (toDelete.isNotEmpty()) {
                bucket.delete(*toDelete.toTypedArray())
            }

            // Carica la nuova immagine.
            bucket.upload(newFilename, imageBytes) { upsert = false }

            // Aggiorna il record in Postgres con il nuovo path.
            val rpcParams = buildJsonObject {
                put("p_user_id", JsonPrimitive(userId.toString()))
                put("pro_pic", JsonPrimitive(newFilename))
            }
            supabaseClient.client.postgrest.rpc("add_profile_pic", rpcParams)

            Result.success("https://mnqyxftyucsmmnvuoyuw.supabase.co/storage/v1/object/public/$bucketName/$userId/$newFilename")
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Verifica se un nome utente è disponibile.
     * @param username Il nome utente da verificare.
     * @return Un `Result` contenente `true` se disponibile, altrimenti `false`.
     */
    suspend fun isUsernameAvailable(username: String): Result<Boolean> {
        return try {
            val rpcParams = buildJsonObject {
                put("p_username", JsonPrimitive(username))
            }

            val result = supabaseClient.client.postgrest
                .rpc("check_username_available", rpcParams)
                .decodeAs<Boolean>()

            Result.success(result)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to check username: ${e.message}"))
        }
    }

    /**
     * Aggiorna le informazioni del profilo.
     * @param username Nome utente.
     * @param firstName Nome.
     * @param lastName Cognome.
     * @return Un `Result` che indica il successo o il fallimento.
     */
    suspend fun updateProfile(
        username: String,
        firstName: String,
        lastName: String,
    ): Result<Unit> {
        return try {
            val userId = supabaseClient.client.auth.currentUserOrNull()?.id
                ?: return Result.failure(Exception("User not logged in"))

            val params = buildJsonObject {
                put("p_user_id", JsonPrimitive(userId))
                put("p_username", JsonPrimitive(username))
                put("p_first_name", JsonPrimitive(firstName))
                put("p_last_name", JsonPrimitive(lastName))
            }

            // Chiamata alla funzione RPC per aggiornare il profilo.
            supabaseClient.client.postgrest.rpc("update_profile_info", params)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to update profile: ${e.message}"))
        }
    }
}