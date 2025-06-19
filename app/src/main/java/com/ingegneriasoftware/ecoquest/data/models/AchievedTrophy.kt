package com.ingegneriasoftware.ecoquest.data.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

/**
 * Rappresenta un trofeo sbloccato da un utente.
 *
 * Nota: Implementa `java.io.Serializable` per compatibilità con SavedStateHandle
 * (necessario per la navigazione in Android nonostante la serializzazione Kotlin).
 *
 * @property earnedAt Data/Ora di sblocco.
 * @property pictureUrl URL dell'immagine del trofeo (nullable).
 */
@Serializable
data class AchievedTrophy(
    @SerialName("trophy_id") val trophyId: Int,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("condition") val condition: String?,
    @SerialName("earned_at") val earnedAt: Instant,
    @SerialName("picture_url") val pictureUrl: String?
) : java.io.Serializable