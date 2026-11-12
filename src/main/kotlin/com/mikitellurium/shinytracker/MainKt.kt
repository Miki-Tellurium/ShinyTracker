package com.mikitellurium.shinytracker

import com.mikitellurium.shinytracker.window.MainWindow
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
            MainWindow(stage)
        }
    }
}