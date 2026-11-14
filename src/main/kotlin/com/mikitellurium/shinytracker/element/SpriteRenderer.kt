package com.mikitellurium.shinytracker.element

import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image

interface SpriteRenderer {
    /**
     * @param g The graphics context of the button canvas
     * @param img The image to be rendered
     * @param w The canvas width
     * @param h The canvas height
     * @param sw The sprite width in pixels
     * @param sh The sprite height in pixels
     */
    fun render(g: GraphicsContext, img: Image, w: Double, h: Double, sw: Int, sh: Int)
}