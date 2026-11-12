package com.mikitellurium.shinytracker.element

import javafx.animation.Animation
import javafx.animation.PauseTransition
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.layout.Background
import javafx.util.Duration


abstract class AnimatedButton(defaultBackground: Background, pressedBackground: Background, animationMillis: Int = 100) : Button() {
    private val animationTime: PauseTransition

    init {
        background = defaultBackground
        animationTime = PauseTransition(Duration.millis(animationMillis.toDouble()))
        animationTime.onFinished = EventHandler { _ -> background = defaultBackground }
        onMousePressed = EventHandler { _ ->
            this.onMousePressed()
            background = pressedBackground
            animationTime.playFromStart()
        }
        onMouseReleased = EventHandler { _ ->
            if (animationTime.status == Animation.Status.RUNNING) {
                animationTime.stop()
            }
            background = defaultBackground
        }
    }

    open fun onMousePressed() {}
}