package com.mikitellurium.shinytracker.data

import com.mikitellurium.shinytracker.util.DATABASE_DIR
import com.mikitellurium.shinytracker.util.HOME_DIR
import com.mikitellurium.shinytracker.util.TEMP_DATA
import kotlinx.serialization.json.Json
import java.io.File

/** Used to temporary store information on current hunt, avoiding too much progress
 *  loss in case of program crash. The file is deleted if the program stops regularly.
 */
object TempFile {
    private val FILE = File(HOME_DIR).resolve(DATABASE_DIR).resolve(TEMP_DATA)

    fun write(shinyHunt: SavedHuntData) {
        val json = Json { prettyPrint = true }
        FILE.writeText(json.encodeToString(shinyHunt))
    }

    fun delete() = FILE.delete()
}