package com.mikitellurium.shinytracker.data

import com.mikitellurium.shinytracker.util.POKEMON_DATA
import kotlinx.serialization.json.Json

object PokemonData {
    var LIST = run {
        val data = object {}.javaClass.getResourceAsStream(POKEMON_DATA)!!
            .bufferedReader()
            .use { it.readText() }
        Json.decodeFromString<List<Pokemon>>(data)
    }

    fun initialize() = Unit
}