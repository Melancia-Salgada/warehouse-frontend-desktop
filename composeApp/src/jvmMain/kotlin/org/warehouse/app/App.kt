package org.warehouse.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.warehouse.app.components.Sidebar
import org.warehouse.app.context.LocalNavigation
import org.warehouse.app.context.NavigationState
import org.warehouse.app.screens.DashboardScreen
import org.warehouse.app.screens.LoginScreen

import warehousedesktop.composeapp.generated.resources.Res
import warehousedesktop.composeapp.generated.resources.compose_multiplatform

@Composable
fun App() {
    var currentRoute by remember { mutableStateOf("/") }

    CompositionLocalProvider(
        LocalNavigation provides NavigationState(
            currentRoute = currentRoute,
            navigate = { newRoute -> currentRoute = newRoute }
        )
    ) {
        Row(modifier = Modifier.fillMaxSize()) {


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFEFEFEF))
            ) {
                when {
                    currentRoute == "/" -> LoginScreen()
                    currentRoute.startsWith("/dashboard") -> DashboardScreen()
                    else -> Text("Página não encontrada", color = Color.Black)
                }
            }
        }
    }
}
