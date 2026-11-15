package com.mikitellurium.shinytracker.pane

import com.mikitellurium.shinytracker.element.AnimatedButton
import com.mikitellurium.shinytracker.util.APP_VERSION
import com.mikitellurium.shinytracker.util.NEW_HUNT_BUTTON_PRESSED_SPRITE
import com.mikitellurium.shinytracker.util.NEW_HUNT_BUTTON_SPRITE
import com.mikitellurium.shinytracker.util.font
import com.mikitellurium.shinytracker.window.MainWindow
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import javafx.scene.text.Text


class NewHuntPane(private val parent: MainWindow): Pane() {
    private val credits = Text().apply {
        layoutY = 12.0
        text = "v$APP_VERSION by Miki Tellurium"
        font = font(FontWeight.BOLD, 14.0)
        fill = Color.web("#F5F5F5")
    }
    private val newHunt = Label().apply {
        font = font(FontWeight.BOLD, 40.0)
        text = "New Shiny Hunt"
        layoutXProperty().bind(this@NewHuntPane.widthProperty().subtract(widthProperty()).divide(2))
        layoutY = 70.0
        textFill = Color.web("#F5F5F5")
    }

    private val newHuntButton = AnimatedButton(NEW_HUNT_BUTTON_SPRITE, NEW_HUNT_BUTTON_PRESSED_SPRITE).apply{
        setPrefSize(180.0, 180.0)
        layoutXProperty().bind(this@NewHuntPane.widthProperty().subtract(widthProperty()).divide(2))
        layoutY = 140.0
    }

    init {
        this.children.addAll(credits, newHunt, newHuntButton)
    }
}