package com.mikitellurium.shinytracker

import com.mikitellurium.shinytracker.MainKt.ShinyTracker
import com.mikitellurium.shinytracker.data.HuntData
import com.mikitellurium.shinytracker.data.PokemonData
import com.mikitellurium.shinytracker.window.ErrorWindow
import com.mikitellurium.shinytracker.window.MainWindow
import javafx.application.Application
import javafx.application.Platform
import javafx.stage.Stage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicBoolean

val LOGGER: Logger = LoggerFactory.getLogger(ShinyTracker::class.java)

// todo red highlight on hunt setting window when pokemon is invalid
// todo add paused and finished list window view
// todo add new saved hunts at index 0 so most recent hunt is at start of list
// todo add pokemon menu sprites for decorations
// todo restore temp file on startup if it exists

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
                PokemonData.initialize()
                HuntData.reload()
                MainWindow(stage)
            } catch (t: Throwable) {
                handler.uncaughtException(Thread.currentThread(), t)
            }
        }

        private companion object {
            val handling = AtomicBoolean(false)
        }
    }
}