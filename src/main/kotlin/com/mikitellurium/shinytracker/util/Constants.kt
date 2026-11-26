package com.mikitellurium.shinytracker.util

import javafx.scene.image.Image


// General
const val APP_NAME = "Shiny Tracker"
const val APP_VERSION = "0.1-beta"
const val CSS_STYLE = "style.css"

// Paths
val HOME_DIR: String = System.getProperty("user.home")
const val DATABASE_DIR = ".local/share/shinytracker"
const val FINISHED_HUNTS = "finished_hunts.json"
const val PAUSED_HUNTS = "paused_hunts.json"
const val TEMP_DATA = "temp.json"

// Sprites
const val WINDOW_ICON = "/sprites/pokeball_logo.png"
val NEW_HUNT_BUTTON = sprite(
    "/sprites/button/new_hunt_button.png",
    "/sprites/button/new_hunt_button_pressed.png",
    32, 32
)

val INCREASE_BUTTON = sprite(
    "/sprites/button/increase_button.png",
    "/sprites/button/increase_button_pressed.png",
    64, 32
)

val GENERIC_BLUE_BUTTON = sprite(
    "/sprites/button/generic_blue_button.png",
    "/sprites/button/generic_blue_button_pressed.png",
    7, 16
)

val GENERIC_RED_BUTTON = sprite(
    "/sprites/button/generic_red_button.png",
    "/sprites/button/generic_red_button_pressed.png",
    7, 16
)
val MISSING_NO_SPRITE = Image("/sprites/missing_no.png")
val POKEMON_BACKGROUND_FADE = Image("/sprites/pokemon_background_fade.png", 512.0, 512.0, true, false)

// Data
const val POKEMON_DATA = "/data/pokemons.json"
// Debug
val TEST_BUTTON_SPRITE = Image("/sprites/button/test_button.png", 70.0, 160.0, true, false)