package com.mikitellurium.shinytracker.element

import com.mikitellurium.shinytracker.data.SavedHuntData
import com.mikitellurium.shinytracker.util.font
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.ContentDisplay
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.OverrunStyle
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.text.FontWeight

class PausedHuntCell : ListCell<SavedHuntData>() {
    private val pokemonName = Label().apply {
        font = font(FontWeight.BOLD, 14.0)
        minWidth = 0.0
        textOverrun = OverrunStyle.ELLIPSIS
    }
    private val attempts = Label().apply { font = font(FontWeight.BOLD, 20.0) }
    private val spacer = Region().apply { HBox.setHgrow(this, Priority.ALWAYS) }
    private val rootBox = HBox(8.0, pokemonName, spacer, attempts).apply {
        alignment = Pos.CENTER_LEFT
    }

    init {
        contentDisplay = ContentDisplay.GRAPHIC_ONLY
        padding = Insets(0.0, 4.0, 0.0, 4.0)
        rootBox.prefWidthProperty().bind(widthProperty().subtract(padding.left + padding.right + 2.0))
    }

    override fun updateItem(item: SavedHuntData?, empty: Boolean) {
        super.updateItem(item, empty)
        if (empty || item == null) {
            graphic = null
            return
        }
        pokemonName.text = item.pokemon.displayName()
        attempts.text = item.attempts.toString()
        graphic = rootBox
    }
}