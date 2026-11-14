package com.mikitellurium.shinytracker.element

import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import kotlin.math.ceil
import kotlin.math.floor

/**
 * Render a button with variable width. Left and right button caps are drawn
 * at the button edges and the center is filled for the whole button length.
 * @param capSz The size in pixel of the edge caps
 */
class ExtensibleWidthButtonRenderer(private val capSz: Int): SpriteRenderer {
    override fun render(g: GraphicsContext, img: Image, w: Double, h: Double, sw: Int, sh: Int) {
        val iw = img.width
        val ih = img.height

        val frac = 1.0 / sw
        val srcPx = iw * frac  // width of one sprite pixel in the source image
        val dstPx = h / sh     // width of one sprite pixel on the canvas

        val pxW = ceil(w / dstPx).toInt()
        val len = pxW - (capSz * 2)

        // Left cap
        g.drawImage(img, 0.0, 0.0, srcPx * capSz, ih, 0.0, 0.0, dstPx * capSz, h)

        // Center
        for (i in 1..len) {
            val xPos = (dstPx * capSz) + (dstPx * (i - 1))
            g.drawImage(img, srcPx * capSz, 0.0, srcPx, ih, xPos, 0.0, dstPx, h)
        }
        // Right cap
        g.drawImage(img, iw - (srcPx * capSz), 0.0, srcPx * capSz, ih, w - (dstPx * capSz), 0.0, dstPx * capSz, h)
    }
}