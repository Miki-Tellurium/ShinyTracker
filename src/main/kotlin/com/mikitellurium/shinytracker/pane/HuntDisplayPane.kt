package com.mikitellurium.shinytracker.pane

import com.mikitellurium.shinytracker.element.AnimatedButton
import com.mikitellurium.shinytracker.util.FONT_DEFAULT
import com.mikitellurium.shinytracker.util.INCREASE_BUTTON_BACKGROUND
import com.mikitellurium.shinytracker.util.INCREASE_BUTTON_PRESSED_BACKGROUND
import com.mikitellurium.shinytracker.window.MainWindow
import javafx.beans.binding.Bindings
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.scene.text.FontWeight


class HuntDisplayPane(val parent: MainWindow): Pane() {
    private val pokemonName = Label().apply {
        font = Font.font(FONT_DEFAULT, FontWeight.BOLD, 40.0)
        layoutXProperty().bind(this@HuntDisplayPane.widthProperty().subtract(widthProperty()).divide(2))
        layoutY = 4.0
    }
    private val renderBounds = Rectangle().apply {
        layoutY = 55.0
        width = 150.0
        height = 148.0
        fill = Color.LIGHTBLUE
        stroke = Color.BLACK
        layoutXProperty().bind(this@HuntDisplayPane.widthProperty().subtract(widthProperty()).divide(2))
    }
    private val pokemonRender = ImageView().apply {
        isPreserveRatio = true
        // Flatmap re-evaluates width/height when the image changes
        val imageWidth = imageProperty().flatMap { it.widthProperty() }
        val imageHeight = imageProperty().flatMap { it.heightProperty() }

        fitWidthProperty().bind(Bindings.createDoubleBinding(
            { Math.clamp(imageWidth.getValue()?.toDouble() ?: 0.0, 60.0, renderBounds.width) },
            imageWidth, renderBounds.widthProperty()
        ))
        fitHeightProperty().bind(Bindings.createDoubleBinding(
            { Math.clamp(imageHeight.getValue()?.toDouble() ?: 0.0, 60.0, renderBounds.height) },
            imageHeight, renderBounds.heightProperty()
        ))

        // Center inside renderBounds
        layoutXProperty().bind(Bindings.createDoubleBinding(
            { renderBounds.layoutX + (renderBounds.width - layoutBounds.width) / 2 },
            renderBounds.layoutXProperty(), renderBounds.widthProperty(), layoutBoundsProperty()
        ))
        layoutYProperty().bind(Bindings.createDoubleBinding(
            { renderBounds.layoutY + (renderBounds.height - layoutBounds.height) / 2 },
            renderBounds.layoutYProperty(), renderBounds.heightProperty(), layoutBoundsProperty()
        ))
    }
    private val counter = Label().apply {
        layoutXProperty().bind(this@HuntDisplayPane.widthProperty().subtract(widthProperty()).divide(2))
        // Fixed vertical center
        val centerY = renderBounds.layoutYProperty().add(renderBounds.heightProperty()).add(37)
        layoutYProperty().bind(centerY.subtract(heightProperty().divide(2)))
        textProperty().addListener { _, _, _ ->
            val size = 70.0 - (text.length * 4)
            font = Font.font(FONT_DEFAULT, FontWeight.EXTRA_BOLD, size)
        }
    }
    private val increaseButton = object : AnimatedButton(INCREASE_BUTTON_BACKGROUND, INCREASE_BUTTON_PRESSED_BACKGROUND) {
        private var counter = 0
        override fun onMousePressed() {
            counter++
            this@HuntDisplayPane.counter.text = "0".repeat(counter)
        }
    }.apply {
        setPrefSize(150.0, 75.0)
        layoutXProperty().bind(this@HuntDisplayPane.widthProperty().subtract(widthProperty()).divide(2))
        layoutY = 280.0
    }

    init {
        pokemonName.text = "Pokèmon"
        val pokemon = "genesect"
        val spriteUrl = "/sprites/pokemons/$pokemon.gif"
        pokemonRender.image = Image(spriteUrl)
        counter.text = "1"
        children.addAll(pokemonName, renderBounds, pokemonRender, counter, increaseButton)
    }
}