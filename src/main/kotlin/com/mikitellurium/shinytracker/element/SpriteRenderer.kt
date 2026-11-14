package com.mikitellurium.shinytracker.element

import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image

interface SpriteRenderer {
    /**
     * @param g The graphics context of the button canvas
     * @param img The image to be rendered
     * @param w The canvas width
     * @param h The canvas height
     */
    fun render(g: GraphicsContext, img: Image, w: Double, h: Double)
}