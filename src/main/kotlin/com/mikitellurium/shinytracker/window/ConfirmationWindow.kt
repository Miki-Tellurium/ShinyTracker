package com.mikitellurium.shinytracker.window

import com.mikitellurium.shinytracker.element.ExtensibleWidthButtonRenderer
import com.mikitellurium.shinytracker.element.SpriteButton
import com.mikitellurium.shinytracker.util.GENERIC_BLUE_BUTTON
import com.mikitellurium.shinytracker.util.GENERIC_RED_BUTTON
import com.mikitellurium.shinytracker.util.font
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import javafx.scene.text.TextAlignment
import javafx.stage.Modality
import javafx.stage.Screen
import javafx.stage.Stage


class ConfirmationWindow(parent: Stage, private val message: String, private val onConfirm: () -> Unit) {
    private val stage = Stage()
    private val rootPane = VBox(15.0).apply {
        padding = Insets(10.0)
        alignment = Pos.TOP_CENTER
    }
    private val buttonBox = HBox(25.0).apply {
        alignment = Pos.CENTER
    }
    private val messageLabel = Label().apply {
        font = font(FontWeight.BOLD, 16.0)
        isWrapText = true
        textAlignment = TextAlignment.CENTER
        maxWidth = Double.MAX_VALUE
        text = message
    }
    private val confirmButton = SpriteButton(GENERIC_BLUE_BUTTON, ExtensibleWidthButtonRenderer(3), 80.0, 30.0).apply {
        font = font(FontWeight.EXTRA_BOLD, 16.0)
        text = "Confirm"
        textFill = Color.WHITE
    }
    private val closeButton = SpriteButton(GENERIC_RED_BUTTON, ExtensibleWidthButtonRenderer(3), 80.0, 30.0).apply {
        font = font(FontWeight.EXTRA_BOLD, 16.0)
        text = "Close"
        textFill = Color.WHITE
    }

    init {
        stage.initOwner(parent)
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.isAlwaysOnTop = true
        stage.title = "Warning!"
        stage.isResizable = false
        val screenBounds = Screen.getPrimary().bounds
        stage.onShown = {
            stage.x = (screenBounds.width - stage.width) / 2
            stage.y = (screenBounds.height - stage.height) / 2
        }
        stage.onCloseRequest = { _ -> closeButton.fire() }

        confirmButton.onAction = { _ ->
            onConfirm()
            stage.close()
        }
        closeButton.onAction = { _ -> stage.close() }

        buttonBox.children.addAll(confirmButton, closeButton)
        rootPane.children.addAll(messageLabel, buttonBox)
        stage.scene = Scene(rootPane, 350.0, -1.0)
        stage.sizeToScene()
        stage.show()
    }
}