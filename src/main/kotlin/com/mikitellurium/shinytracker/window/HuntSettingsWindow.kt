package com.mikitellurium.shinytracker.window

import com.mikitellurium.shinytracker.element.ExtensibleWidthButtonRenderer
import com.mikitellurium.shinytracker.element.IntField
import com.mikitellurium.shinytracker.element.SpriteButton
import com.mikitellurium.shinytracker.util.GENERIC_BLUE_BUTTON
import com.mikitellurium.shinytracker.util.GENERIC_RED_BUTTON
import com.mikitellurium.shinytracker.util.font
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.Scene
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import javafx.stage.Stage


class HuntSettingsWindow(parent: Stage) {
    val stage = Stage()
    val rootPane = GridPane().apply {
        padding = Insets(5.0)
    }

    private val pokemonName = Label().apply {
        font = font(FontWeight.BOLD, 18.0)
        GridPane.setConstraints(this, 0, 0)
        text = "Pokémon"
    }
    private val pokemonSearch = TextField().apply {
        font = font(FontWeight.BOLD, 14.0)
        GridPane.setConstraints(this, 0, 1)
        GridPane.setColumnSpan(this, 3)
        GridPane.setMargin(this, Insets(-4.0, 0.0, 0.0, 0.0))
        prefWidth = 180.0
        padding = Insets(2.0, 0.0, 2.0, 4.0)
        promptText = "Search"
        isFocusTraversable = false
    }
    private val rollsText = Label().apply {
        font = font(FontWeight.BOLD, 18.0)
        GridPane.setConstraints(this, 0, 2)
        text = "Rolls"
    }
    private val rollsField = IntField(maxValue = Short.MAX_VALUE.toInt()).apply {
        font = font(FontWeight.BOLD, 14.0)
        GridPane.setConstraints(this, 0, 3)
        GridPane.setMargin(this, Insets(-5.0, 0.0, 0.0, 0.0))
        prefWidth = 40.0
        padding = Insets(2.0, 0.0, 2.0, 4.0)
        promptText = "Rolls"
        text = "1"
        isFocusTraversable = false
        onInputMethodTextChanged
    }
    private val separator = Label().apply {
        font = font(FontWeight.EXTRA_BOLD, 22.0)
        GridPane.setConstraints(this, 1, 3)
        GridPane.setMargin(this, Insets(-8.0, 10.0, -2.0, 10.0))
        GridPane.setValignment(this, VPos.TOP)
        text = "/"
    }
    private val oddsText = Label().apply {
        font = font(FontWeight.BOLD, 18.0)
        GridPane.setConstraints(this, 2, 2)
        text = "Base Odds"
    }
    private val oddsSelector = ComboBox<Int>().apply {
        prefWidth = 90.0
        GridPane.setConstraints(this, 2, 3)
        GridPane.setMargin(this, Insets(-5.0, 0.0, 0.0, 0.0))
        items.addAll(4096, 8192)
        value = 4096
        cellFactory = {
            object : ListCell<Int>() {
                override fun updateItem(item: Int?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (empty  || item == null) {
                        text = null
                        graphic = null
                    } else {
                        text = item.toString()
                    }
                }
            }.apply {
                font = font(FontWeight.BOLD, 14.0)
            }
        }
        buttonCell = object : ListCell<Int>() {
            override fun updateItem(item: Int?, empty: Boolean) {
                super.updateItem(item, empty)
                if (empty  || item == null) {
                    text = null
                    graphic = null
                } else {
                    text = item.toString()
                }
            }
        }.apply {
            font = font(FontWeight.BOLD, 14.0)
        }
    }
    private val buttonBox = HBox(40.0).apply {
        GridPane.setConstraints(this, 0, 4)
        GridPane.setColumnSpan(this, 3)
        GridPane.setMargin(this, Insets(5.0, 0.0, 0.0, 0.0))
        alignment = Pos.CENTER
    }
    private val confirmButton = SpriteButton(GENERIC_BLUE_BUTTON, ExtensibleWidthButtonRenderer(3), 80.0, 30.0).apply {
        font = font(FontWeight.EXTRA_BOLD, 16.0)
        text = "Confirm"
        textFill = Color.WHITE
    }
    private val closeButton = SpriteButton(GENERIC_RED_BUTTON, ExtensibleWidthButtonRenderer(3), 80.0, 30.0).apply {
        font = font(FontWeight.EXTRA_BOLD, 16.0)
        text = "Close"
        textFill = Color.WHITE
    }

    init {
        stage.isResizable = false
        buttonBox.children.addAll(confirmButton, closeButton)
        rootPane.children.addAll(pokemonName, pokemonSearch, rollsText, rollsField, separator, oddsText, oddsSelector,
            buttonBox)
        stage.scene = Scene(rootPane)
        stage.show()
    }
}