package com.mikitellurium.shinytracker

import com.mikitellurium.shinytracker.window.ErrorWindow
import com.mikitellurium.shinytracker.window.MainWindow
import javafx.application.Application
import javafx.application.Platform
import javafx.stage.Stage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicBoolean

class MainKt {
    fun main() {
        Application.launch(ShinyTracker::class.java)
    }

    class ShinyTracker : Application() {
        override fun start(stage: Stage) {
            val handler = Thread.UncaughtExceptionHandler { thread, throwable ->
                LOGGER.error("Exception on ${thread.name}:", throwable)
                if (!handling.compareAndSet(false, true)) return@UncaughtExceptionHandler
                val show = {
                    try {
                        ErrorWindow(stage, throwable) {
                            handling.set(false);
                            Platform.exit()
                        }
                    } catch (t: Throwable) {
                        LOGGER.error("ErrorWindow failed", t)
                        Platform.exit()
                    }
                }
                if (Platform.isFxApplicationThread()) show() else Platform.runLater{ show() }
            }
            Thread.setDefaultUncaughtExceptionHandler(handler)
            Thread.currentThread().uncaughtExceptionHandler = handler

            try {
                MainWindow(stage)
            } catch (t: Throwable) {
                handler.uncaughtException(Thread.currentThread(), t)
            }
        }

        private companion object {
            val LOGGER: Logger = LoggerFactory.getLogger(ShinyTracker::class.java)
            val handling = AtomicBoolean(false)
        }
    }
}