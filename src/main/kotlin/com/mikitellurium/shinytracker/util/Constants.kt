package com.mikitellurium.shinytracker.util

import javafx.scene.image.Image
import javafx.scene.layout.*


// General
const val APP_NAME = "Shiny Tracker"
const val APP_VERSION = "0.1-beta"
const val WINDOW_ICON = "/sprites/pokeball_logo.png"
const val FONT_DEFAULT = "Arial"
const val CSS_STYLE = "style.css"

// Elements
val INCREASE_BUTTON_SPRITE: Image = Image("/sprites/button/increase_button.png", 640.0, 320.0, true, true)
val INCREASE_BUTTON_BACKGROUND: Background = Background(BackgroundImage(INCREASE_BUTTON_SPRITE, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize(150.0, 75.0, false, false, true, true)))
val INCREASE_BUTTON_PRESSED_SPRITE = Image("/sprites/button/increase_button_pressed.png", 640.0, 320.0, true, true)
val INCREASE_BUTTON_PRESSED_BACKGROUND = Background(BackgroundImage(INCREASE_BUTTON_PRESSED_SPRITE, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize(150.0, 75.0, false, false, true, true)))

// Sprites
val MISSING_NO_SPRITE = Image("/sprites/missing_no.png")
