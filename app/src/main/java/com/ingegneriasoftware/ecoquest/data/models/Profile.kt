package com.ingegneriasoftware.ecoquest.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant

/**
 * Dettagli del profilo utente.
 * Include informazioni personali, punti accumulati e data di creazione.
 *
 * @property userId ID univoco dell'utente.
 * @property dailyPoints Punti giornalieri.
 * @property createdAt Data di registrazione (nullable).
 */
@Serializable
data class Profile(
    @SerialName("user_id") val userId: String,
    @SerialName("username") val username: String,
    @SerialName("first_name") val firstName: String?,
    @SerialName("last_name") val lastName: String?,
    @SerialName("profile_pic") val profilePic: String?,
    @SerialName("daily_points") val dailyPoints: Long,
    @SerialName("weekly_points") val weeklyPoints: Long,
    @SerialName("monthly_points") val monthlyPoints: Long,
    @SerialName("points") val points: Long,
    @SerialName("created_at") val createdAt: Instant?
)