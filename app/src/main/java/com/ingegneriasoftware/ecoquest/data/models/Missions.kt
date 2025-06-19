package com.ingegneriasoftware.ecoquest.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Rappresenta una missione disponibile nel gioco.
 *
 * @property id ID univoco della missione.
 * @property title Titolo della missione.
 * @property description Descrizione dettagliata (nullable).
 * @property points Punti ricompensa.
 * @property type Tipo di missione (daily, weekly, monthly).
 * @property missionPic URL dell'immagine associata (nullable).
 * @property missionPrompt Testo guida per la missione (nullable).
 * @property camera Indica se la missione richiede l'uso della fotocamera.
 */
@Serializable
data class Missions(
    @SerialName("mission_id") val id: Int,
    val title: String,
    val description: String?,
    val points: Long,
    val type: String,
    @SerialName("mission_pic") val missionPic: String?,
    @SerialName("mission_prompt") val missionPrompt: String?,
    @SerialName("camera") val camera: Boolean
)