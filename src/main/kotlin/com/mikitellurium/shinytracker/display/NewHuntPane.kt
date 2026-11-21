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
import javafx.geometry.VPos
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.control.OverrunStyle
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.RowConstraints
import javafx.scene.layout.VBox
import javafx.scene.text.FontWeight
import javafx.scene.text.Text


class NewHuntPane(private val parent: MainWindow): GridPane() {
    private val credits = Label("v$APP_VERSION by Miki Tellurium").apply {
        styleClass.add("text")
        setConstraints(this, 0, 0)
        setValignment(this, VPos.TOP)
        font = font(FontWeight.BOLD, 14.0)
        maxWidth = Double.MAX_VALUE
    }
    private val pausedTitle = Label("Paused hunts:").apply {
        styleClass.add("text")
        setConstraints(this, 0, 1)
        setValignment(this, VPos.BOTTOM)
        setMargin(this, Insets(50.0, 0.0, 0.0, 0.0))
        font = font(FontWeight.BOLD, 18.0)
        maxWidth = Double.MAX_VALUE
    }
    val cellHeight = 36.0
    private val pausedList = ListView<SavedHuntData>().apply {
        styleClass.add("paused-hunt-list")
        setConstraints(this, 0, 2)
        setValignment(this, VPos.TOP)
        maxWidth = 200.0
        fixedCellSize = cellHeight
        cellFactory = {
            PausedHuntCell {
                HuntData.PAUSED.remove(it)
                this@NewHuntPane.parent.setShinyHunt(it.toHunt())
            }
        }
    }
    private val buttonBox = VBox().apply {
        setConstraints(this, 1, 0)
        setRowSpan(this, 3)
        setFillHeight(this, true)
        alignment = Pos.CENTER
    }

    private val newHunt = Label("New Shiny Hunt").apply {
        font = font(FontWeight.BOLD, 32.0)
    }
    private val newHuntButton = AnimatedButton(NEW_HUNT_BUTTON).apply{
        setPrefSize(180.0, 180.0)
        onAction = { HuntSettingsWindow(this@NewHuntPane.parent.mainStage) {
            this@NewHuntPane.parent.setShinyHunt(it)
        } }
    }

    init {
        padding = Insets(0.0)

        // Column 0: paused list, hard-capped. Column 1: button box, takes the rest.
        columnConstraints.addAll(
            ColumnConstraints().apply {
                minWidth = 0.0
                maxWidth = 200.0
                hgrow = Priority.NEVER
            },
            ColumnConstraints().apply { hgrow = Priority.ALWAYS }
        )
        rowConstraints.addAll(
            RowConstraints().apply { vgrow = Priority.NEVER },
            RowConstraints().apply { vgrow = Priority.NEVER },
            RowConstraints().apply { vgrow = Priority.ALWAYS }
        )

        val listHeight = Bindings.size(pausedList.items).multiply(cellHeight).add(2.0)
        pausedList.prefHeightProperty().bind(listHeight)
        pausedList.minHeightProperty().bind(listHeight)
        pausedList.maxHeightProperty().bind(listHeight)

        pausedList.items.addAll(HuntData.PAUSED.take(10))
        buttonBox.children.addAll(newHunt, newHuntButton)
        this.children.addAll(credits, pausedTitle, pausedList, buttonBox)
    }
}