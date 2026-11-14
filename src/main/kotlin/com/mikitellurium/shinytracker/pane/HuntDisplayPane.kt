package com.mikitellurium.shinytracker.pane

import com.mikitellurium.shinytracker.element.AnimatedButton
import com.mikitellurium.shinytracker.util.FONT_DEFAULT
import com.mikitellurium.shinytracker.util.INCREASE_BUTTON_PRESSED_SPRITE
import com.mikitellurium.shinytracker.util.INCREASE_BUTTON_SPRITE
import com.mikitellurium.shinytracker.window.MainWindow
import javafx.beans.binding.Bindings
import javafx.scene.control.Button
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
        layoutY = 0.0
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
    private val counterCenterY = renderBounds.layoutYProperty().add(renderBounds.heightProperty()).add(35)
    private val counter = Label().apply {
        layoutXProperty().bind(this@HuntDisplayPane.widthProperty().subtract(widthProperty()).divide(2))
        layoutYProperty().bind(counterCenterY.subtract(heightProperty().divide(2)))
        textProperty().addListener { _, _, _ ->
            val size = 70.0 - (text.length * 4)
            font = Font.font(FONT_DEFAULT, FontWeight.EXTRA_BOLD, size)
        }
    }

    private val increaseButton = object : AnimatedButton(INCREASE_BUTTON_SPRITE, INCREASE_BUTTON_PRESSED_SPRITE, 150.0, 75.0) {
        private var counter = 1
        override fun onMousePressed() {
            counter++
            this@HuntDisplayPane.counter.text = "0".repeat(counter)
        }
    }.apply {
        layoutXProperty().bind(this@HuntDisplayPane.widthProperty().subtract(widthProperty()).divide(2))
        layoutYProperty().bind(counterCenterY.add(35))
    }

    private val shinyOddsTitle = Label().apply {
        prefWidth = 120.0
        text = "Odds"
        font = Font.font(FONT_DEFAULT, FontWeight.BOLD, 20.0)
        styleClass.add("info-label-header")
        layoutYProperty().bind(increaseButton.layoutYProperty().add(increaseButton.heightProperty()).add(10))
    }
    private val shinyOdds = Label().apply {
        prefWidth = 180.0
        font = Font.font(FONT_DEFAULT, FontWeight.BOLD, 20.0)
        styleClass.add("info-label")
        layoutYProperty().bind(increaseButton.layoutYProperty().add(increaseButton.heightProperty()).add(10))
    }

    private val dryStreakTitle = Label().apply {
        prefWidth = 120.0
        text = "Dry Streak"
        font = Font.font(FONT_DEFAULT, FontWeight.BOLD, 20.0)
        styleClass.add("info-label-header")
        layoutYProperty().bind(shinyOddsTitle.layoutYProperty().add(shinyOdds.heightProperty()).subtract(1))
    }

    private val dryStreak = Label().apply {
        prefWidth = 180.0
        font = Font.font(FONT_DEFAULT, FontWeight.BOLD, 20.0)
        styleClass.add("info-label")
        layoutYProperty().bind(shinyOddsTitle.layoutYProperty().add(shinyOdds.heightProperty()).subtract(1))
    }
    private val finishButton = Button("Finish").apply {
        font = Font.font(FONT_DEFAULT, FontWeight.EXTRA_BOLD, 14.0)
        layoutX = 300.0
        layoutYProperty().bind(this@HuntDisplayPane.heightProperty().subtract(heightProperty()).subtract(7))
    }
    private val pauseButton = Button("Pause").apply {
        font = Font.font(FONT_DEFAULT, FontWeight.EXTRA_BOLD, 14.0)
        layoutX = 370.0
        layoutYProperty().bind(this@HuntDisplayPane.heightProperty().subtract(heightProperty()).subtract(7))
    }
    private val editButton = Button("Edit").apply {
        font = Font.font(FONT_DEFAULT, FontWeight.EXTRA_BOLD, 14.0)
        layoutX = 450.0
        layoutYProperty().bind(this@HuntDisplayPane.heightProperty().subtract(heightProperty()).subtract(7))
    }
    val testButton = Button("Test Button").apply {
        font = Font.font(FONT_DEFAULT, FontWeight.BOLD, 16.0)
        styleClass.add("sprite-button")
        layoutX = 20.0
        layoutY = 50.0
    }

    init {
        shinyOddsTitle.layoutXProperty().bind(widthProperty().subtract(shinyOddsTitle.widthProperty().add(shinyOdds.widthProperty())).divide(2))
        shinyOdds.layoutXProperty().bind(shinyOddsTitle.layoutXProperty().add(shinyOddsTitle.widthProperty()))
        dryStreakTitle.layoutXProperty().bind(widthProperty().subtract(dryStreakTitle.widthProperty().add(dryStreak.widthProperty())).divide(2))
        dryStreak.layoutXProperty().bind(dryStreakTitle.layoutXProperty().add(dryStreakTitle.widthProperty()))

        val pokemon = "Froslass"
        pokemonName.text = pokemon
        val spriteUrl = "/sprites/pokemon/${pokemon.lowercase()}.gif"
        pokemonRender.image = Image(spriteUrl)
        counter.text = "0"
        shinyOdds.text = "1/4096"
        dryStreak.text = "100%"
        children.addAll(pokemonName, renderBounds, pokemonRender, counter, increaseButton, shinyOddsTitle, shinyOdds,
            dryStreakTitle, dryStreak, finishButton, pauseButton, editButton, testButton)
    }
}