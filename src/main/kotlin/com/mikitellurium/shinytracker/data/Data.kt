package com.mikitellurium.shinytracker.data

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.encodeToStream
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import java.io.FileNotFoundException

const val HEADER = "api/v2/"
val ROOT = File(System.getProperty("user.home"), "/Projects/api-data/data/$HEADER")

fun main() {
    val format = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    val list = mutableListOf<Pokemon>()

    val speciesDir = fetchFile("pokemon-species", false)
    val fileList = speciesDir.listFiles()!!.apply {
        sortWith { file, file1 ->
            try {
                val x = file.name.toInt()
                val y = file1.name.toInt()
                if (x > y) 1
                else if (x < y) -1
                else 0
            } catch (e: NumberFormatException) {
                0
            }
        }
    }

    for (file in fileList) {
        if (!file.isDirectory) continue
        val speciesFile = fetchFile("pokemon-species/${file.name}")
        try {
            fetchPokemonData(format, speciesFile).forEach(list::add)
        } catch (e: Exception) {
            System.err.println("Exception while fetching $speciesFile: ${e.message}")
            e.printStackTrace()
        }
    }

    savePokemon(format, list.toList())
    println("Saved ${list.size} Pokemons.")
}

@OptIn(ExperimentalSerializationApi::class)
fun savePokemon(format: Json, list: List<Pokemon>) {
    File("out/pokemons.json").outputStream().use { out ->
        format.encodeToStream(list, out)
    }
}

fun fetchFile(endpoint: String, index: Boolean = true): File {
    val file = ROOT.resolve(endpoint.substringAfter(HEADER))
        .let { if (index) it.resolve("index.json") else it }
    return if (file.exists()) file else throw FileNotFoundException("File not found: ${file.absolutePath}")
}

fun fetchPokemonData(format: Json, file: File): List<Pokemon> {
    val list = mutableListOf<Pokemon>()

    val data = file.readText()
    val species = format.parseToJsonElement(data).jsonObject
    val varieties = species["varieties"]!!.jsonArray
    varieties.map { it.jsonObject }.forEach { variety ->
        val url = variety["pokemon"]!!.jsonObject["url"]!!.jsonPrimitive.content
        val file1 = fetchFile(url)
        val pokemon = format.parseToJsonElement(file1.readText()).jsonObject
        val forms = pokemon["forms"]!!.jsonArray
        forms.map { it.jsonObject }.forEach { formJson ->
            val url = formJson["url"]!!.jsonPrimitive.content
            val file2 = fetchFile(url)
            val form = format.parseToJsonElement(file2.readText()).jsonObject
            if (!form["is_battle_only"]!!.jsonPrimitive.boolean) {
                val types = form["types"]!!.jsonArray
                var type1 = ""
                var type2: String? = null
                types.map { it.jsonObject }.forEach { typeJson ->
                    val slot = typeJson["slot"]!!.jsonPrimitive.int
                    val type = typeJson["type"]!!.jsonObject["name"]!!.jsonPrimitive.content
                    if (slot == 1) type1 = type
                    else type2 = type
                }
                val gen = species["generation"]!!.jsonObject["name"]!!.jsonPrimitive.content
                val name = form["name"]!!.jsonPrimitive.content
                val type = Type(type1, type2)
                with (Pokemon(name, gen, type)) {
                    list.add(this)
                    println("Fetched $name")
                }
            }
        }
    }
    return list
}
