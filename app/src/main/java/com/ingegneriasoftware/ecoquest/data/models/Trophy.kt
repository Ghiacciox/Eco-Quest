package com.ingegneriasoftware.ecoquest.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Modello per un trofeo disponibile nel gioco.
 *
 * @property condition Condizione per sbloccare il trofeo (nullable).
 * @property pictureUrl URL dell'icona del trofeo (nullable con default esplicito).
 */
@Serializable
data class Trophy(
    @SerialName("trophy_id") val trophyId: Int,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("condition") val condition: String? = null,
    @SerialName("picture_url") val pictureUrl: String? = null
)