package com.mikitellurium.shinytracker.display

import com.mikitellurium.shinytracker.LOGGER
import com.mikitellurium.shinytracker.data.HuntData
import com.mikitellurium.shinytracker.data.NONE
import com.mikitellurium.shinytracker.data.Pokemon
import com.mikitellurium.shinytracker.data.ShinyHunt
import com.mikitellurium.shinytracker.element.AnimatedButton
import com.mikitellurium.shinytracker.element.ExtensibleWidthButtonRenderer
import com.mikitellurium.shinytracker.element.SpriteButton
import com.mikitellurium.shinytracker.util.*
import com.mikitellurium.shinytracker.window.ConfirmationWindow
import com.mikitellurium.shinytracker.window.HuntSettingsWindow
import com.mikitellurium.shinytracker.window.MainWindow
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.FontWeight
import java.util.*
import kotlin.IllegalArgumentException
import kotlin.apply
import kotlin.text.format


class HuntDisplayPane(val parent: MainWindow, initialHunt: ShinyHunt): Pane() {
    private val huntProperty = SimpleObjectProperty(initialHunt)
    var shinyHunt: ShinyHunt
        get() = huntProperty.get()
        set(value) = huntProperty.set(value)
    // Re-targets automatically whenever the hunt is swapped
    private val counterObservable: ObservableValue<Number> = huntProperty.flatMap { it.counterProperty }

    private val pokemonName = Label().apply {
        font = font(FontWeight.BOLD, 40.0)
        layoutXProperty().bind(this@HuntDisplayPane.widthProperty().subtract(widthProperty()).divide(2))
        textProperty().bind(huntProperty.map { it.pokemon.displayName() })
        layoutY = 0.0
        textFill = Color.web("#F5F5F5")
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
        textFill = Color.web("#F5F5F5")
    }
    private val increaseButton = AnimatedButton(INCREASE_BUTTON, 150.0, 75.0).apply {
        layoutXProperty().bind(this@HuntDisplayPane.widthProperty().subtract(widthProperty()).divide(2))
        layoutYProperty().bind(counterCenterY.add(40))
        onAction = { _ -> shinyHunt.increaseAttempts() }
    }
    private val shinyOddsTitle = Label().apply {
        prefWidth = 120.0
        text = "Odds"
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
    private val dryStreakTitle = Label().apply {
        prefWidth = 120.0
        text = "Dry Streak"
        font = font(FontWeight.BOLD, 20.0)
        styleClass.add("info-label-header")
        layoutYProperty().bind(shinyOddsTitle.layoutYProperty().add(shinyOddsTitle.heightProperty()).subtract(1))
    }
    private val dryStreak = Label().apply {
        prefWidth = 180.0
        font = font(FontWeight.BOLD, 20.0)
        styleClass.add("info-label")
        layoutYProperty().bind(shinyOddsTitle.layoutYProperty().add(shinyOdds.heightProperty()).subtract(1))
        textProperty().bind(Bindings.createStringBinding({
            val p = shinyHunt.geometricDistribution()
            "${"%.3f".format(Locale.ROOT, p)}%"
        }, counterObservable))
    }
    private val finishButton = SpriteButton(GENERIC_BLUE_BUTTON, ExtensibleWidthButtonRenderer(3), 70.0, 28.0).apply {
        font = font(FontWeight.EXTRA_BOLD, 14.0)
        layoutXProperty().bind(this@HuntDisplayPane.widthProperty().subtract(widthProperty()).divide(2).subtract(widthProperty().add(40)))
        layoutYProperty().bind(dryStreakTitle.layoutYProperty().add(dryStreakTitle.heightProperty().add(12)))
        text = "Finish"
        textFill = Color.web("#F5F5F5")
        onAction = { _ ->
            ConfirmationWindow(this@HuntDisplayPane.parent.mainStage, "Are you sure you want to end this shiny hunt? The data will be saved.") {
                HuntData.FINISHED.add(shinyHunt.toSavedData())
                HuntData.save()
                this@HuntDisplayPane.parent.clearShinyHunt()
            }
        }
    }
    private val pauseButton = SpriteButton(GENERIC_BLUE_BUTTON, ExtensibleWidthButtonRenderer(3), 70.0, 28.0).apply {
        font = font(FontWeight.EXTRA_BOLD, 14.0)
        layoutXProperty().bind(this@HuntDisplayPane.widthProperty().subtract(widthProperty()).divide(2))
        layoutYProperty().bind(dryStreakTitle.layoutYProperty().add(dryStreakTitle.heightProperty().add(12)))
        text = "Pause"
        textFill = Color.web("#F5F5F5")
        onAction = { _ -> }
    }
    private val editButton = SpriteButton(GENERIC_BLUE_BUTTON, ExtensibleWidthButtonRenderer(3), 70.0, 28.0).apply {
        font = font(FontWeight.EXTRA_BOLD, 14.0)
        layoutXProperty().bind(this@HuntDisplayPane.widthProperty().subtract(widthProperty()).divide(2).add(widthProperty().add(40)))
        layoutYProperty().bind(dryStreakTitle.layoutYProperty().add(dryStreakTitle.heightProperty().add(12)))
        text = "Edit"
        textFill = Color.web("#F5F5F5")
        onAction = { _ ->
            HuntSettingsWindow(this@HuntDisplayPane.parent.mainStage, true) {
                this@HuntDisplayPane.shinyHunt = it
            }.apply { set(shinyHunt.pokemon, shinyHunt.rolls, shinyHunt.baseOdds, shinyHunt.attempts) }
        }
    }

    init {
        counterObservable.addListener { _, _, _ -> }
        huntProperty.subscribe { hunt -> updateSprite(hunt.pokemon) }
        shinyOddsTitle.layoutXProperty().bind(widthProperty().subtract(shinyOddsTitle.widthProperty().add(shinyOdds.widthProperty())).divide(2))
        shinyOdds.layoutXProperty().bind(shinyOddsTitle.layoutXProperty().add(shinyOddsTitle.widthProperty()))
        dryStreakTitle.layoutXProperty().bind(widthProperty().subtract(dryStreakTitle.widthProperty().add(dryStreak.widthProperty())).divide(2))
        dryStreak.layoutXProperty().bind(dryStreakTitle.layoutXProperty().add(dryStreakTitle.widthProperty()))
        children.addAll(backgroundFade, pokemonName, renderBounds, pokemonRender, counter, increaseButton, shinyOddsTitle, shinyOdds,
            dryStreakTitle, dryStreak, finishButton, pauseButton, editButton)
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