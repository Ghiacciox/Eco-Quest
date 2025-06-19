package com.ingegneriasoftware.ecoquest.data.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Rappresenta una missione completata da un utente.
 * Estende le informazioni di base della missione con la data di completamento.
 *
 * @property id ID della missione completata.
 * @property completedAt Data/Ora del completamento.
 */
@Serializable
data class CompletedMission(
    @SerialName("mission_id") val id: Int,
    val title: String,
    val description: String?,
    val points: Long,
    val type: String,
    @SerialName("mission_pic") val missionPic: String?,
    @SerialName("completed_at") val completedAt: Instant
)