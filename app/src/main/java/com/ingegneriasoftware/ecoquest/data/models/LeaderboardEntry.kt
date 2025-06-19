package com.ingegneriasoftware.ecoquest.data.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Rappresenta una voce nella classifica degli utenti.
 * Include username, punti, posizione in classifica e altre informazioni.
 *
 * @property username Nome dell'utente.
 * @property points Punti totali accumulati.
 * @property rank Posizione in classifica.
 * @property updatedAt Data/Ora dell'ultimo aggiornamento (nullable).
 * @property profilePic URL dell'immagine del profilo (opzionale).
 */
@Serializable
data class LeaderboardEntry(
    val username: String,
    val points: Long,
    val rank: Int,
    @SerialName("updated_at") val updatedAt: Instant?,
    @SerialName("profile_pic") val profilePic: String? = null
)