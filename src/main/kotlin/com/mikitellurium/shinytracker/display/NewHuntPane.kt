package com.mikitellurium.shinytracker.display

import com.mikitellurium.shinytracker.data.HuntData
import com.mikitellurium.shinytracker.data.SavedHuntData
import com.mikitellurium.shinytracker.element.AnimatedButton
import com.mikitellurium.shinytracker.element.PausedHuntCell
import com.mikitellurium.shinytracker.util.APP_VERSION
import com.mikitellurium.shinytracker.util.NEW_HUNT_BUTTON
import com.mikitellurium.shinytracker.util.font
import com.mikitellurium.shinytracker.window.HuntSettingsWindow
import com.mikitellurium.shinytracker.window.MainWindow
import javafx.beans.binding.Bindings
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.layout.*
import javafx.scene.text.FontWeight


class NewHuntPane(private val parent: MainWindow): StackPane() {
    private val credits = Label("v$APP_VERSION by Miki Tellurium").apply {
        setAlignment(this, Pos.TOP_RIGHT)
        font = font(FontWeight.BOLD, 14.0)
        translateX = -3.0
    }

    private val grid = GridPane()
    private val pausedTitle = Label("Paused hunts:").apply {
        GridPane.setConstraints(this, 0, 0)
        font = font(FontWeight.BOLD, 18.0)
    }
    val cellHeight = 36.0
    private val pausedList = ListView<SavedHuntData>().apply {
        styleClass.add("paused-hunt-list")
        GridPane.setConstraints(this, 0, 1)
        maxWidth = 200.0
        fixedCellSize = cellHeight
        cellFactory = {
            PausedHuntCell {
                HuntData.removePaused(it)
                this@NewHuntPane.parent.setShinyHunt(it.toHunt())
            }
        }
    }
    private val buttonBox = VBox().apply {
        GridPane.setConstraints(this, 1, 0)
        GridPane.setRowSpan(this, 3)
        alignment = Pos.CENTER
    }

    private val newHunt = Label("New Shiny Hunt").apply {
        font = font(FontWeight.BOLD, 32.0)
    }
    private val newHuntButton = AnimatedButton(NEW_HUNT_BUTTON).apply{
        setPrefSize(180.0, 180.0)
        onAction = {
            HuntSettingsWindow(this@NewHuntPane.parent.mainStage) {
                this@NewHuntPane.parent.setShinyHunt(it)
            }
        }
    }

    init {
        padding = Insets(0.0)

        // Column 0: paused list, hard-capped. Column 1: button box, takes the rest.
        grid.columnConstraints.addAll(
            ColumnConstraints().apply {
                minWidth = 0.0
                maxWidth = 200.0
                hgrow = Priority.NEVER
            },
            ColumnConstraints().apply { hgrow = Priority.ALWAYS }
        )
        grid.rowConstraints.addAll(
            RowConstraints().apply { vgrow = Priority.NEVER },
            RowConstraints().apply { vgrow = Priority.NEVER },
            RowConstraints().apply { vgrow = Priority.ALWAYS }
        )

        val listHeight = Bindings.size(pausedList.items).multiply(cellHeight).add(2.0)
        pausedList.prefHeightProperty().bind(listHeight)
        pausedList.minHeightProperty().bind(listHeight)
        pausedList.maxHeightProperty().bind(listHeight)

        val entries = HuntData.getPaused { it.take(10) }
        if (entries.isNotEmpty()) {
            pausedList.items.addAll(entries)
            grid.children.addAll(pausedTitle, pausedList)
        }
        buttonBox.children.addAll(newHunt, newHuntButton)
        grid.children.add(buttonBox)
        this.children.addAll(grid, credits)
    }
}