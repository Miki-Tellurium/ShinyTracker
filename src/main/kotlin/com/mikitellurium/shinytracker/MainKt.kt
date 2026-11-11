package com.mikitellurium.shinytracker

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.stage.Stage

class MainKt {
    fun main() {
        Application.launch(HelloApp::class.java)
    }

    class HelloApp : Application() {
        override fun start(stage: Stage) {
            stage.scene = Scene(Label("Hello, JavaFX!"), 320.0, 200.0)
            stage.title = "Hello"
            stage.show()
        }
    }
}