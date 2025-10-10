package org.warehouse.app

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import java.awt.Dimension

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "WarehouseDesktop",
    ) {
        window.minimumSize = Dimension(900, 600)
        App()
    }
}