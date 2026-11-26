package com.mikitellurium.shinytracker.data

import com.mikitellurium.shinytracker.LOGGER
import com.mikitellurium.shinytracker.util.DATABASE_DIR
import com.mikitellurium.shinytracker.util.FINISHED_HUNTS
import com.mikitellurium.shinytracker.util.HOME_DIR
import com.mikitellurium.shinytracker.util.PAUSED_HUNTS
import kotlinx.serialization.json.Json
import java.io.File

object HuntData {
    var FINISHED = mutableListOf<SavedHuntData>()
    var PAUSED = mutableListOf<SavedHuntData>()

    fun save() {
        val json = Json { prettyPrint = true }
        val array = arrayListOf(
            Pair(FINISHED, File(HOME_DIR).resolve(DATABASE_DIR).resolve(FINISHED_HUNTS)),
            Pair(PAUSED, File(HOME_DIR).resolve(DATABASE_DIR).resolve(PAUSED_HUNTS))
        )
        array.forEach { (list, file) ->
            if (!file.exists()) file.parentFile.mkdirs()
            file.outputStream().bufferedWriter().use {
                it.write(json.encodeToString(list))
            }
        }
        LOGGER.info("Saved hunt data.")
    }

    fun reload() {
        FINISHED = load(FINISHED_HUNTS).toMutableList()
        PAUSED = load(PAUSED_HUNTS).toMutableList()
        LOGGER.info("Loaded hunt data.")
    }

    private fun load(endpoint: String): List<SavedHuntData> {
        val file = File(HOME_DIR).resolve(DATABASE_DIR).resolve(endpoint)
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.writeText("[]")
            return emptyList()
        }
        val data = file.inputStream().bufferedReader().use { it.readText() }
        return Json.decodeFromString<List<SavedHuntData>>(data)
    }
}