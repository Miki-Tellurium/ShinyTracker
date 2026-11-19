package com.mikitellurium.shinytracker.data

import kotlinx.serialization.Serializable

@Serializable
data class Pokemon(val id: Int, val name: String, val variant: String? = null, val generation: String, val type: Type) {
    fun sprite(): String = name.let { if (variant != null) "${it}_$variant" else it }
}
@Serializable
data class Type(val primary: String, val secondary: String? = null)