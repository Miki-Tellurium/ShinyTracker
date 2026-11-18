package com.mikitellurium.shinytracker.element

import javafx.beans.InvalidationListener
import javafx.scene.canvas.Canvas
import javafx.scene.control.skin.ButtonSkin

class SpriteButtonSkin(private val btn: SpriteButton): ButtonSkin(btn) {
    private val canvas = Canvas().apply {
        isManaged = false
        isMouseTransparent = true
    }
    private val redraw = InvalidationListener { draw() }

    init {
        canvas.graphicsContext2D.isImageSmoothing = false
        children.add(0, canvas) // behind the label
        btn.spriteProperty.addListener(redraw) // state change -> redraw
    }

    override fun layoutChildren(x: Double, y: Double, w: Double, h: Double) {
        val bw = btn.width
        val bh = btn.height
        if (canvas.width != bw || canvas.height != bh) {
            canvas.width = bw
            canvas.height = bh
            draw()
        }
        super.layoutChildren(x, y, w, h)
    }

    private fun draw() {
        val w = canvas.width
        val h = canvas.height
        if (w <= 0 || h <= 0) return

        val g = canvas.graphicsContext2D
        g.clearRect(0.0, 0.0, w, h)

        val img = btn.spriteProperty.value

        btn.renderer.render(g, img, w, h, btn.sprite.width, btn.sprite.height)
    }

    override fun dispose() {
        btn.spriteProperty.removeListener(redraw)
        super.dispose()
    }
}