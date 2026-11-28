package com.mikitellurium.shinytracker.data

import com.mikitellurium.shinytracker.LOGGER
import com.mikitellurium.shinytracker.util.DATABASE_DIR
import com.mikitellurium.shinytracker.util.FINISHED_HUNTS
import com.mikitellurium.shinytracker.util.HOME_DIR
import com.mikitellurium.shinytracker.util.PAUSED_HUNTS
import kotlinx.serialization.json.Json
import java.io.File

object HuntData {
    private var finished = mutableListOf<SavedHuntData>()
    private var paused = mutableListOf<SavedHuntData>()

    fun getPaused(filter: (List<SavedHuntData>) -> List<SavedHuntData> = { l -> l }): List<SavedHuntData> = filter(paused.toList())
    fun getFinished(filter: (List<SavedHuntData>) -> List<SavedHuntData> = { l -> l }): List<SavedHuntData> = filter(finished.toList())
    fun addPaused(data: SavedHuntData) = paused.add(0, data)
    fun addFinished(data: SavedHuntData) = finished.add(0, data)
    fun removePaused(data: SavedHuntData) = paused.remove(data)
    fun removeFinished(data: SavedHuntData) = finished.remove(data)

    fun save() {
        val json = Json { prettyPrint = true }
        val array = arrayListOf(
            Pair(finished, File(HOME_DIR).resolve(DATABASE_DIR).resolve(FINISHED_HUNTS)),
            Pair(paused, File(HOME_DIR).resolve(DATABASE_DIR).resolve(PAUSED_HUNTS))
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
        finished = load(FINISHED_HUNTS).toMutableList()
        paused = load(PAUSED_HUNTS).toMutableList()
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