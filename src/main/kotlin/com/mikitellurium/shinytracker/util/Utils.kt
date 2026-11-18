package com.mikitellurium.shinytracker.util

import com.mikitellurium.shinytracker.element.BiStateSprite
import javafx.scene.image.Image
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

fun font(weight: FontWeight, size: Double): Font = Font.font(Font.getDefault().family, weight, size)

fun sprite(default: String, pressed: String, width: Int, height: Int,
           requestedWidth: Double = width * 16.0, requestedHeight: Double = height * 16.0): BiStateSprite = BiStateSprite(
    Image(default, requestedWidth, requestedHeight, true, false),
    Image(pressed, requestedWidth, requestedHeight, true, false),
    width, height)
