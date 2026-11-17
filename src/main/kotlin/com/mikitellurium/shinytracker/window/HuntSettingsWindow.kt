package com.mikitellurium.shinytracker.window

import com.mikitellurium.shinytracker.util.font
import javafx.geometry.Insets
import javafx.geometry.VPos
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
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
        font = font(FontWeight.BOLD, 16.0)
        GridPane.setConstraints(this, 0, 1)
        GridPane.setColumnSpan(this, 3)
        GridPane.setMargin(this, Insets(-4.0, 0.0, 0.0, 0.0))
        prefWidth = 200.0
        padding = Insets(2.0, 0.0, 2.0, 4.0)
        promptText = "Search"
        isFocusTraversable = false
    }
    private val rollsText = Label().apply {
        font = font(FontWeight.BOLD, 18.0)
        GridPane.setConstraints(this, 0, 2)
        text = "Rolls"
    }
    private val rollsField = TextField().apply {
        font = font(FontWeight.BOLD, 16.0)
        GridPane.setConstraints(this, 0, 3)
        GridPane.setMargin(this, Insets(-5.0, 0.0, 0.0, 0.0))
        prefWidth = 50.0
        padding = Insets(2.0, 0.0, 2.0, 4.0)
        promptText = "Rolls"
        isFocusTraversable = false
    }
    private val separator = Label().apply {
        font = font(FontWeight.EXTRA_BOLD, 26.0)
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
    private val oddsField = TextField().apply {
        font = font(FontWeight.BOLD, 16.0)
        GridPane.setConstraints(this, 2, 3)
        GridPane.setMargin(this, Insets(-5.0, 0.0, 0.0, 0.0))
        prefWidth = 50.0
        padding = Insets(2.0, 0.0, 2.0, 4.0)
        promptText = "4096"
        isFocusTraversable = false
    }

    init {
        stage.isResizable = false
        rootPane.children.addAll(pokemonName, pokemonSearch, rollsText, rollsField, separator, oddsText, oddsField)
        stage.scene = Scene(rootPane)
        stage.show()
    }
}