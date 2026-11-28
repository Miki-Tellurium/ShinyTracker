package com.mikitellurium.shinytracker.window

import com.mikitellurium.shinytracker.data.HuntData
import com.mikitellurium.shinytracker.data.ShinyHunt
import com.mikitellurium.shinytracker.data.TempFile
import com.mikitellurium.shinytracker.display.HuntDisplayPane
import com.mikitellurium.shinytracker.display.NewHuntPane
import com.mikitellurium.shinytracker.util.APP_NAME
import com.mikitellurium.shinytracker.util.WINDOW_ICON
import com.mikitellurium.shinytracker.util.withDefaultStyle
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Screen
import javafx.stage.Stage


class MainWindow(val mainStage: Stage) {
    init {
        mainStage.title = APP_NAME
        mainStage.icons.add(Image(WINDOW_ICON))
        mainStage.isResizable = false
        mainStage.setWidth(500.0)
        mainStage.setHeight(520.0)
        val screenBounds = Screen.getPrimary().bounds
        mainStage.x = (screenBounds.width - mainStage.width) / 2
        mainStage.y = (screenBounds.height - mainStage.height) / 2
        mainStage.scene = Scene(NewHuntPane(this)).withDefaultStyle()
        mainStage.onCloseRequest = {
            val pane = mainStage.scene.root
            if (pane is HuntDisplayPane) {
                HuntData.addPaused(pane.shinyHunt.toSavedData())
                HuntData.save()
            }
            TempFile.delete()
        }
        mainStage.show()
    }

    fun setShinyHunt(shinyHunt: ShinyHunt) {
        mainStage.scene = Scene(HuntDisplayPane(this, shinyHunt)).withDefaultStyle()
    }

    fun clearShinyHunt() {
        mainStage.scene = Scene(NewHuntPane(this)).withDefaultStyle()
    }
}