package com.mikitellurium.shinytracker.window

import com.mikitellurium.shinytracker.data.NONE
import com.mikitellurium.shinytracker.data.Pokemon
import com.mikitellurium.shinytracker.data.PokemonData
import com.mikitellurium.shinytracker.data.ShinyHunt
import com.mikitellurium.shinytracker.element.ExtensibleWidthButtonRenderer
import com.mikitellurium.shinytracker.element.IntField
import com.mikitellurium.shinytracker.element.SpriteButton
import com.mikitellurium.shinytracker.util.GENERIC_BLUE_BUTTON
import com.mikitellurium.shinytracker.util.GENERIC_RED_BUTTON
import com.mikitellurium.shinytracker.util.font
import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import javafx.stage.Modality
import javafx.stage.Popup
import javafx.stage.Screen
import javafx.stage.Stage
import kotlin.math.min

/**
 * @param onClose Function passed by the parent to read the values: 1) Pokémon 2) rolls 3) base odds
 */
class HuntSettingsWindow(parent: Stage, onClose: (ShinyHunt) -> Unit) {
    val stage = Stage()
    val rootPane = GridPane().apply {
        padding = Insets(5.0)
    }
    var pokemon: Pokemon = NONE
    var firstEdit = false

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
    private val suggestionList = ListView<Pokemon>().apply {
        cellFactory = {
            object : ListCell<Pokemon>() {
                protected override fun updateItem(item: Pokemon?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (empty || item == null) {
                        setText(null)
                        setGraphic(null)
                    } else {
                        text = item.displayName()
                    }
                }
            }.apply {
                font = font(FontWeight.BOLD, 14.0)
            }
        }
    }
    private val suggestionsPopup = Popup().apply {
        isAutoHide = true
        content.add(suggestionList)
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
        onAction = {
            onClose.invoke(ShinyHunt(pokemon, rollsField.value(), oddsSelector.value))
            stage.close()
        }
    }
    private val closeButton = SpriteButton(GENERIC_RED_BUTTON, ExtensibleWidthButtonRenderer(3), 80.0, 30.0).apply {
        font = font(FontWeight.EXTRA_BOLD, 16.0)
        text = "Close"
        textFill = Color.WHITE
        onAction = { stage.close() }
    }

    init {
        stage.isResizable = false
        stage.initOwner(parent)
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.isAlwaysOnTop = true
        val screenBounds = Screen.getPrimary().bounds
        stage.x = (screenBounds.width - stage.width) / 2
        stage.y = (screenBounds.height - stage.height) / 2

        pokemonSearch.textProperty().addListener { _, _, newValue ->
            if (firstEdit) {
                firstEdit = false
                return@addListener
            }
            pokemon = NONE
            if (newValue.isEmpty() || newValue.isBlank()) {
                suggestionsPopup.hide()
            } else {
                val filtered = PokemonData.gather(newValue)
                FXCollections.sort(filtered) { p1, p2 -> p1.fullName.compareTo(p2.fullName, true) }
                suggestionList.items = filtered

                val h = min(filtered.size, 10) * 30.0
                suggestionList.prefHeight = h

                suggestionsPopup.show(
                    stage,
                    pokemonSearch.localToScreen(pokemonSearch.boundsInLocal).minX,
                    pokemonSearch.localToScreen(pokemonSearch.boundsInLocal).maxY
                )
            }
        }
        val setText = {
            val selected = suggestionList.getSelectionModel().getSelectedItem()
            if (selected != null) {
                pokemonSearch.text = selected.displayName()
                pokemon = selected
                suggestionsPopup.hide()
            }
        }
        suggestionList.onMouseClicked = { setText() }
        suggestionList.onKeyPressed = { event -> if (event.code == KeyCode.ENTER) setText() }

        buttonBox.children.addAll(confirmButton, closeButton)
        rootPane.children.addAll(pokemonName, pokemonSearch, rollsText, rollsField, separator,
            oddsText, oddsSelector, buttonBox)
        stage.scene = Scene(rootPane)
        stage.show()
    }

    fun set(mon: Pokemon, rolls: Int = 1, baseOdds: Int = 4096) {
        firstEdit = true
        pokemon = mon
        pokemonSearch.text = pokemon.displayName()
        rollsField.setValue(rolls)
        if (baseOdds == 4096  || baseOdds == 8192) {
            oddsSelector.value = baseOdds
        } else throw IllegalArgumentException("Invalid base odds value, only 4096 or 8192 allowed")
    }
}