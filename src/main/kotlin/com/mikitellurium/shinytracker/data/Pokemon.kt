package com.mikitellurium.shinytracker.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pokemon(
    val id: Int,
    val name: String,
    @SerialName("full_name") val fullName: String,
    val variant: String? = null,
    val generation: Generation,
    val type: Type
) {
    fun sprite(): String = name.let { if (variant != null) "${it}_$variant" else it }
    fun displayName(): String =
        variant?.split("_")
            ?.joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }
            ?.let { "$fullName ($it)" }
            ?: fullName
}
@Serializable
data class Type(val primary: String, val secondary: String? = null)
@Serializable
enum class Generation {
    @SerialName("generation-i") GEN_I,
    @SerialName("generation-ii") GEN_II,
    @SerialName("generation-iii") GEN_III,
    @SerialName("generation-iv") GEN_IV,
    @SerialName("generation-v") GEN_V,
    @SerialName("generation-vi") GEN_VI,
    @SerialName("generation-vii") GEN_VII,
    @SerialName("generation-viii") GEN_VIII,
    @SerialName("generation-ix") GEN_IX,
}