package com.mikitellurium.shinytracker.data

import com.mikitellurium.shinytracker.util.POKEMON_DATA
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import kotlinx.serialization.json.Json

object PokemonData {
    var LIST = run {
        val data = object {}.javaClass.getResourceAsStream(POKEMON_DATA)!!
            .bufferedReader()
            .use { it.readText() }
        Json.decodeFromString<List<Pokemon>>(data)
    }

    fun gather(str: String): ObservableList<Pokemon> {
        val list = LIST.filter { it.name.contains(str.lowercase()) }.toList()
        return FXCollections.observableList(list)
    }

    fun initialize() = Unit
}