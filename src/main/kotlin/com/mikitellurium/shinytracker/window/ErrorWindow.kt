package com.mikitellurium.shinytracker.window

import com.mikitellurium.shinytracker.util.font
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.Pane
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
    private val rootPane = Pane()

    private val messageLabel = Label().apply {
        font = font(FontWeight.BOLD, 14.0)
        textFill = Color.RED
        isWrapText = true
        textAlignment = TextAlignment.CENTER
        prefWidth = 260.0
        layoutXProperty().bind(rootPane.widthProperty().subtract(widthProperty()).divide(2))
        layoutY = 10.0
        text = "${exception.javaClass.name}: ${exception.message}"
    }
    private val crashLogButton = Button("Make Crash Log").apply {
        font = font(FontWeight.EXTRA_BOLD, 16.0)
        layoutXProperty().bind(rootPane.layoutXProperty().add(20))
        layoutYProperty().bind(messageLabel.heightProperty().add(heightProperty().divide(2)))
    }
    private val exitButton = Button("Exit").apply {
        font = font(FontWeight.EXTRA_BOLD, 16.0)
        layoutXProperty().bind(crashLogButton.layoutXProperty().add(crashLogButton.widthProperty()).add(25))
        layoutYProperty().bind(messageLabel.heightProperty().add(heightProperty().divide(2)))
    }

    init {
        if (parent != null && parent.isShowing) {
            stage.initOwner(parent)
            stage.initModality(Modality.APPLICATION_MODAL)
            stage.isAlwaysOnTop = true
        }
        stage.title = "ERROR"
        stage.isResizable = false
        stage.width = 280.0
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        stage.x = (screenSize.getWidth() - stage.width) / 2
        val mainScene = Scene(rootPane)
        stage.onShown = { _ ->
            val height = messageLabel.layoutBounds.height + exitButton.layoutBounds.height
            val stageInsets = stage.height - mainScene.height
            stage.height = height + stageInsets + 50
            stage.y = (screenSize.height - stage.height) / 2
        }
        stage.onCloseRequest = { _ -> onDismiss.run() }
        stage.scene = mainScene


        crashLogButton.onAction = { _ ->
            runCatching { logException(exception) }.onFailure { LOGGER.error("Failed to write crash log", it) }
            exitButton.fire()
        }
        exitButton.onAction = { _ ->
            onDismiss.run()
            stage.close()
        }

        rootPane.children.addAll(messageLabel, crashLogButton, exitButton)
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
        private val FILE_NAME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss-SSS")
        private val TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
    }
}