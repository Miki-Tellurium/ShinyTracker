package com.mikitellurium.shinytracker.window

import com.mikitellurium.shinytracker.element.ExtensibleWidthButtonRenderer
import com.mikitellurium.shinytracker.element.SpriteButton
import com.mikitellurium.shinytracker.element.SpriteRenderer
import com.mikitellurium.shinytracker.util.GENERIC_RED_BUTTON_PRESSED_SPRITE
import com.mikitellurium.shinytracker.util.GENERIC_RED_BUTTON_SPRITE
import com.mikitellurium.shinytracker.util.font
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import javafx.scene.text.TextAlignment
import javafx.stage.Modality
import javafx.stage.Stage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.awt.Toolkit
import java.io.FileWriter
import java.io.PrintWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ErrorWindow(parent: Stage?, private val exception: Throwable, private val onDismiss: Runnable = Runnable {}) {
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
        textFill = Color.RED
        isWrapText = true
        textAlignment = TextAlignment.CENTER
        maxWidth = Double.MAX_VALUE
        text = "${exception.javaClass.name}: ${exception.message}"
    }
    private val exportButton = object: SpriteButton(GENERIC_RED_BUTTON_SPRITE, GENERIC_RED_BUTTON_PRESSED_SPRITE, 150.0, 30.0) {
        override fun spriteRenderer(): SpriteRenderer = ExtensibleWidthButtonRenderer(7, 16, 3)
    }.apply {
        font = font(FontWeight.EXTRA_BOLD, 16.0)
        text = "Export Crash Log"
        textFill = Color.WHITE
    }
    private val closeButton = object: SpriteButton(GENERIC_RED_BUTTON_SPRITE, GENERIC_RED_BUTTON_PRESSED_SPRITE, 80.0, 30.0) {
        override fun spriteRenderer(): SpriteRenderer = ExtensibleWidthButtonRenderer(7, 16, 3)
    }.apply {
        font = font(FontWeight.EXTRA_BOLD, 16.0)
        text = "Close"
        textFill = Color.WHITE
    }

    init {
        if (parent != null && parent.isShowing) {
            stage.initOwner(parent)
            stage.initModality(Modality.APPLICATION_MODAL)
            stage.isAlwaysOnTop = true
        }
        stage.title = "ERROR"
        stage.isResizable = false
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        stage.onShown = {
            stage.x = (screenSize.width - stage.width) / 2
            stage.y = (screenSize.height - stage.height) / 2
        }
        stage.onCloseRequest = { _ -> closeButton.fire() }

        exportButton.onAction = { _ ->
            runCatching { logException(exception) }.onFailure { LOGGER.error("Failed to write crash log", it) }
            closeButton.fire()
        }
        closeButton.onAction = { _ ->
            onDismiss.run()
            stage.close()
        }

        buttonBox.children.addAll(exportButton, closeButton)
        rootPane.children.addAll(messageLabel, buttonBox)
        stage.scene = Scene(rootPane, 350.0, -1.0)
        stage.sizeToScene()
        stage.show()
    }

    private fun logException(throwable: Throwable) {
        val now = LocalDateTime.now()
        FileWriter("crash_log_${now.format(FILE_NAME_FORMAT)}.txt", Charsets.UTF_8).use { fw ->
            PrintWriter(fw).use { pw ->
                val timestamp = now.format(TIMESTAMP_FORMAT)

                pw.println("----- Crash Log -----")
                pw.println("Date/Time: $timestamp")
                pw.println("Stack Trace:")
                throwable.printStackTrace(pw)
                pw.println()
            }
        }
    }

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger("ErrorWindow")
        private val FILE_NAME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss")
        private val TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
    }
}