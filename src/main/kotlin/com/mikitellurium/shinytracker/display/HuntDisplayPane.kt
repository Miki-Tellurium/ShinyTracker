package com.mikitellurium.shinytracker.display

import com.mikitellurium.shinytracker.LOGGER
import com.mikitellurium.shinytracker.data.HuntData
import com.mikitellurium.shinytracker.data.NONE
import com.mikitellurium.shinytracker.data.Pokemon
import com.mikitellurium.shinytracker.data.ShinyHunt
import com.mikitellurium.shinytracker.data.TempFile
import com.mikitellurium.shinytracker.element.AnimatedButton
import com.mikitellurium.shinytracker.element.ExtensibleWidthButtonRenderer
import com.mikitellurium.shinytracker.element.SpriteButton
import com.mikitellurium.shinytracker.util.*
import com.mikitellurium.shinytracker.window.ConfirmationWindow
import com.mikitellurium.shinytracker.window.HuntSettingsWindow
import com.mikitellurium.shinytracker.window.MainWindow
import javafx.animation.PauseTransition
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.event.EventHandler
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.FontWeight
import javafx.util.Duration
import java.util.*


class HuntDisplayPane(val parent: MainWindow, initialHunt: ShinyHunt): Pane() {
    private val huntProperty = SimpleObjectProperty(initialHunt)
    var shinyHunt: ShinyHunt
        get() = huntProperty.get()
        set(value) = huntProperty.set(value)
    // Re-targets automatically whenever the hunt is swapped
    private val counterObservable: ObservableValue<Number> = huntProperty.flatMap { it.counterProperty }
    private val tempFileTimer = PauseTransition(Duration.seconds(60.0))

    private val pokemonName = Label().apply {
        font = font(FontWeight.BOLD, 40.0)
        layoutXProperty().bind(this@HuntDisplayPane.widthProperty().subtract(widthProperty()).divide(2))
        textProperty().bind(huntProperty.map { it.pokemon.displayName() })
        layoutY = 0.0
    }
    private val renderBounds = Rectangle().apply {
        layoutY = 55.0
        width = 150.0
        height = 148.0
        fill = Color.TRANSPARENT
        stroke = Color.TRANSPARENT
        layoutXProperty().bind(this@HuntDisplayPane.widthProperty().subtract(widthProperty()).divide(2))
    }
    private val backgroundFade = ImageView(POKEMON_BACKGROUND_FADE).apply{
        isPreserveRatio = true
        fitWidth = 220.0
        fitHeight = 220.0
        layoutXProperty().bind(Bindings.createDoubleBinding(
            { renderBounds.layoutX + (renderBounds.width - layoutBounds.width) / 2 },
            renderBounds.layoutXProperty(), renderBounds.widthProperty(), layoutBoundsProperty()
        ))
        layoutYProperty().bind(Bindings.createDoubleBinding(
            { renderBounds.layoutY + (renderBounds.height - layoutBounds.height) / 2 },
            renderBounds.layoutYProperty(), renderBounds.heightProperty(), layoutBoundsProperty()
        ))
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
        font = font(FontWeight.EXTRA_BOLD, 66.0)
        layoutXProperty().bind(this@HuntDisplayPane.widthProperty().subtract(widthProperty()).divide(2))
        layoutYProperty().bind(counterCenterY.subtract(heightProperty().divide(2)))
        textProperty().bind(Bindings.createStringBinding(
            { counterObservable.value.toString() }, counterObservable
        ))
        textProperty().addListener { _, _, _ ->
            val size = 70.0 - (text.length * 4)
            font = font(FontWeight.EXTRA_BOLD, size)
        }
    }
    private val increaseButton = AnimatedButton(INCREASE_BUTTON, 150.0, 75.0).apply {
        layoutXProperty().bind(this@HuntDisplayPane.widthProperty().subtract(widthProperty()).divide(2))
        layoutYProperty().bind(counterCenterY.add(40))
        onAction = {
            shinyHunt.increaseAttempts()
            LOGGER.info("Increased counter. Current attempts: ${shinyHunt.attempts}")
        }
    }
    private val shinyOddsTitle = Label("Odds").apply {
        prefWidth = 125.0
        font = font(FontWeight.BOLD, 20.0)
        styleClass.add("info-label-header")
        layoutYProperty().bind(increaseButton.layoutYProperty().add(increaseButton.heightProperty()).add(10))
    }
    private val shinyOdds = Label().apply {
        prefWidth = 180.0
        font = font(FontWeight.BOLD, 20.0)
        styleClass.add("info-label")
        layoutYProperty().bind(increaseButton.layoutYProperty().add(increaseButton.heightProperty()).add(10))
        textProperty().bind(Bindings.createStringBinding(
            { "1/${shinyHunt.chance()}" }, counterObservable
        ))
    }
    private val endChanceTitle = Label("End Chance").apply {
        prefWidth = 125.0
        styleClass.add("info-label-header")
        font = font(FontWeight.BOLD, 20.0)
        layoutYProperty().bind(shinyOddsTitle.layoutYProperty().add(shinyOddsTitle.heightProperty()).subtract(1))
    }
    private val endChance = Label().apply {
        styleClass.add("info-label")
        prefWidth = 180.0
        font = font(FontWeight.BOLD, 20.0)
        layoutYProperty().bind(shinyOddsTitle.layoutYProperty().add(shinyOdds.heightProperty()).subtract(1))
        textProperty().bind(Bindings.createStringBinding({
            val p = shinyHunt.geometricDistribution()
            "${"%.3f".format(Locale.ROOT, p)}%"
        }, counterObservable))
    }
    private val finishButton = SpriteButton(GENERIC_BLUE_BUTTON, ExtensibleWidthButtonRenderer(3), 70.0, 28.0).apply {
        styleClass.add("sprite-button")
        font = font(FontWeight.EXTRA_BOLD, 14.0)
        layoutXProperty().bind(this@HuntDisplayPane.widthProperty().subtract(widthProperty()).divide(2).subtract(widthProperty().add(40)))
        layoutYProperty().bind(endChanceTitle.layoutYProperty().add(endChanceTitle.heightProperty().add(12)))
        text = "Finish"
        onAction = { _ ->
            ConfirmationWindow(this@HuntDisplayPane.parent.mainStage, "Are you sure you want to end this shiny hunt? The data will be saved.") {
                HuntData.addFinished(shinyHunt.toSavedData())
                HuntData.save()
                this@HuntDisplayPane.parent.clearShinyHunt()
                TempFile.delete()
                LOGGER.info("Finished hunt: ${shinyHunt.pokemon.displayName()}")
            }
        }
    }
    private val pauseButton = SpriteButton(GENERIC_BLUE_BUTTON, ExtensibleWidthButtonRenderer(3), 70.0, 28.0).apply {
        styleClass.add("sprite-button")
        font = font(FontWeight.EXTRA_BOLD, 14.0)
        layoutXProperty().bind(this@HuntDisplayPane.widthProperty().subtract(widthProperty()).divide(2))
        layoutYProperty().bind(endChanceTitle.layoutYProperty().add(endChanceTitle.heightProperty().add(12)))
        text = "Pause"
        onAction = { _ ->
            HuntData.addPaused(shinyHunt.toSavedData())
            HuntData.save()
            this@HuntDisplayPane.parent.clearShinyHunt()
            TempFile.delete()
            LOGGER.info("Paused hunt: ${shinyHunt.pokemon.displayName()}")
        }
    }
    private val editButton = SpriteButton(GENERIC_BLUE_BUTTON, ExtensibleWidthButtonRenderer(3), 70.0, 28.0).apply {
        styleClass.add("sprite-button")
        font = font(FontWeight.EXTRA_BOLD, 14.0)
        layoutXProperty().bind(this@HuntDisplayPane.widthProperty().subtract(widthProperty()).divide(2).add(widthProperty().add(40)))
        layoutYProperty().bind(endChanceTitle.layoutYProperty().add(endChanceTitle.heightProperty().add(12)))
        text = "Edit"
        onAction = { _ ->
            HuntSettingsWindow(this@HuntDisplayPane.parent.mainStage, true) {
                this@HuntDisplayPane.shinyHunt = it
                LOGGER.info("Edited hunt: ${shinyHunt.pokemon.displayName()}")
            }.apply { set(shinyHunt.pokemon, shinyHunt.rolls, shinyHunt.baseOdds, shinyHunt.attempts) }
        }
    }

    init {
        LOGGER.info("Setup hunt: ${shinyHunt.pokemon.displayName()}")
        counterObservable.addListener { _, _, _ -> }
        tempFileTimer.onFinished = EventHandler { _ ->
            TempFile.write(shinyHunt.toSavedData())
            tempFileTimer.playFromStart()
        }
        huntProperty.subscribe { hunt -> updateSprite(hunt.pokemon) }
        shinyOddsTitle.layoutXProperty().bind(widthProperty().subtract(shinyOddsTitle.widthProperty().add(shinyOdds.widthProperty())).divide(2))
        shinyOdds.layoutXProperty().bind(shinyOddsTitle.layoutXProperty().add(shinyOddsTitle.widthProperty()))
        endChanceTitle.layoutXProperty().bind(widthProperty().subtract(endChanceTitle.widthProperty().add(endChance.widthProperty())).divide(2))
        endChance.layoutXProperty().bind(endChanceTitle.layoutXProperty().add(endChanceTitle.widthProperty()))
        children.addAll(backgroundFade, pokemonName, renderBounds, pokemonRender, counter, increaseButton, shinyOddsTitle, shinyOdds,
            endChanceTitle, endChance, finishButton, pauseButton, editButton)
        tempFileTimer.play()
    }

    fun updateSprite(pokemon: Pokemon) {
        if (pokemon == NONE) pokemonRender.image = MISSING_NO_SPRITE
        else {
            val url = String.format("/sprites/pokemon/${pokemon.spriteId()}.gif")
            try {
                pokemonRender.image = Image(url)
            } catch (_: IllegalArgumentException) {
                // Sprite is not found
                pokemonRender.image = MISSING_NO_SPRITE
                LOGGER.warn("Missing sprite: $url")
            }
        }
    }
}