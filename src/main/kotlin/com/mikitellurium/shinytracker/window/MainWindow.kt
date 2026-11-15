package com.mikitellurium.shinytracker.window

import com.mikitellurium.shinytracker.pane.HuntDisplayPane
import com.mikitellurium.shinytracker.pane.NewHuntPane
import com.mikitellurium.shinytracker.util.APP_NAME
import com.mikitellurium.shinytracker.util.CSS_STYLE
import com.mikitellurium.shinytracker.util.WINDOW_ICON
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Screen
import javafx.stage.Stage


class MainWindow(
    val mainStage: Stage
) {
    init {
        mainStage.title = APP_NAME
        mainStage.icons.add(Image(WINDOW_ICON))
        mainStage.isResizable = false
        mainStage.setWidth(500.0)
        mainStage.setHeight(520.0)
        val screenBounds = Screen.getPrimary().bounds
        mainStage.x = (screenBounds.width - mainStage.width) / 2
        mainStage.y = (screenBounds.height - mainStage.height) / 2
        mainStage.scene = Scene(NewHuntPane(this))
            .apply { stylesheets.add(CSS_STYLE) }
        mainStage.show()
    }
}