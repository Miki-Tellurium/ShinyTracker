package com.mikitellurium.shinytracker.element

import javafx.scene.image.Image

data class BiStateSprite(
    val default: Image,
    val pressed: Image,
    val width: Int = default.width.toInt(),
    val height: Int = default.width.toInt()
)
