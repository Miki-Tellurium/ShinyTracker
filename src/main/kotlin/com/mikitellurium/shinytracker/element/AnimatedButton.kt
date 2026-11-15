package com.mikitellurium.shinytracker.element

import javafx.animation.Animation
import javafx.animation.PauseTransition
import javafx.beans.property.SimpleObjectProperty
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundImage
import javafx.scene.layout.BackgroundPosition
import javafx.scene.layout.BackgroundRepeat
import javafx.scene.layout.BackgroundSize
import javafx.util.Duration


open class AnimatedButton(
    defaultSprite: Image,
    pressedSprite: Image,
    width: Double = defaultSprite.width,
    height: Double = defaultSprite.height,
    animationMillis: Int = 100
) : Button() {
    private val defaultBg = makeBg(defaultSprite, width, height)
    private val pressedBg = makeBg(pressedSprite, width, height)
    private val animationTime: PauseTransition

    init {
        setPrefSize(width, height)
        background = defaultBg
        animationTime = PauseTransition(Duration.millis(animationMillis.toDouble()))
        animationTime.onFinished = EventHandler { _ -> background = defaultBg }
        onMousePressed = EventHandler { _ ->
            background = pressedBg
            animationTime.playFromStart()
        }
        onMouseReleased = EventHandler { _ ->
            if (animationTime.status == Animation.Status.RUNNING) {
                animationTime.stop()
            }
            background = defaultBg
        }
    }

    companion object {
        private fun makeBg(img: Image, width: Double, height: Double): Background = Background(
                BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                    BackgroundSize(width, height, false, false, true, true)))
    }
}