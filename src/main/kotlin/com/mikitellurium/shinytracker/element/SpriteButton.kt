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

class SpriteButton(
    val sprite: BiStateSprite,
    val renderer: SpriteRenderer,
    width: Double = sprite.width.toDouble(),
    height: Double = sprite.height.toDouble(),
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
        spriteProperty.set(sprite.default)
        animationTime = PauseTransition(Duration.millis(animationMillis.toDouble()))
        animationTime.onFinished = EventHandler { _ -> spriteProperty.set(sprite.default) }
        onMousePressed = EventHandler { _ ->
            spriteProperty.set(sprite.pressed)
            animationTime.playFromStart()
        }
        onMouseReleased = EventHandler { _ ->
            if (animationTime.status == Animation.Status.RUNNING) {
                animationTime.stop()
            }
            spriteProperty.set(sprite.default)
        }
    }

    override fun createDefaultSkin(): Skin<*> = SpriteButtonSkin(this)
}