package com.mikitellurium.shinytracker.window

import com.mikitellurium.shinytracker.pane.HuntDisplayPane
import com.mikitellurium.shinytracker.util.APP_NAME
import com.mikitellurium.shinytracker.util.WINDOW_ICON
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import java.awt.Dimension
import java.awt.Toolkit


class MainWindow(
    val mainStage: Stage
) {
    init {
        mainStage.title = APP_NAME
        mainStage.icons.add(Image(WINDOW_ICON))
        mainStage.isResizable = false
        mainStage.setWidth(800.0)
        mainStage.setHeight(500.0)
        val screenSize: Dimension = Toolkit.getDefaultToolkit().screenSize
        mainStage.x = (screenSize.getWidth() - mainStage.width) / 2
        mainStage.y = (screenSize.getHeight() - mainStage.height) / 2
        val scene = Scene(HuntDisplayPane(this))
        mainStage.scene = scene
        mainStage.show()
    }
}