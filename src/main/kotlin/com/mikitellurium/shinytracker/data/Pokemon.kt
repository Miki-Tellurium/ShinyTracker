package com.mikitellurium.shinytracker.data

import kotlinx.serialization.Serializable

@Serializable
data class Pokemon(val name: String, val generation: String, val type: Type)
@Serializable
data class Type(val primary: String, val secondary: String? = null)