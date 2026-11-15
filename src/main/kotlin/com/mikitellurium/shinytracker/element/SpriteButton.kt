package com.mikitellurium.shinytracker.element

import javafx.animation.Animation
import javafx.animation.PauseTransition
import javafx.beans.property.SimpleObjectProperty
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.Skin
import javafx.scene.image.Image
import javafx.scene.layout.Background
import javafx.scene.layout.Border
import javafx.util.Duration

abstract class SpriteButton(
    val defaultSprite: Image,
    val pressedSprite: Image,
    width: Double = defaultSprite.width,
    height: Double = defaultSprite.height,
    animationMillis: Int = 100
) : Button() {
    private val animationTime: PauseTransition
    val spriteProperty = SimpleObjectProperty<Image>()

    init {
        setPrefSize(width, height)
        setMinSize(width, height)
        setMaxSize(width, height)
        background = Background.EMPTY
        border = Border.EMPTY
        padding = Insets.EMPTY
        isFocusTraversable = false
        spriteProperty.set(defaultSprite)
        animationTime = PauseTransition(Duration.millis(animationMillis.toDouble()))
        animationTime.onFinished = EventHandler { _ -> spriteProperty.set(defaultSprite) }
        onMousePressed = EventHandler { _ ->
            spriteProperty.set(pressedSprite)
            animationTime.playFromStart()
        }
        onMouseReleased = EventHandler { _ ->
            if (animationTime.status == Animation.Status.RUNNING) {
                animationTime.stop()
            }
            spriteProperty.set(defaultSprite)
        }
    }

    abstract fun spriteRenderer(): SpriteRenderer

    override fun createDefaultSkin(): Skin<*> = SpriteButtonSkin(this)
}