package org.warehouse.app.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.warehouse.app.components.Sidebar
import org.warehouse.app.context.LocalNavigation
import org.warehouse.app.screens.pages.HomeScreen
import org.warehouse.app.screens.pages.UserScreen

@Composable
fun DashboardScreen() {
    val nav = LocalNavigation.current
    val currentRoute = nav.currentRoute
    val navigate = nav.navigate
    var windowHeightPx by remember { mutableStateOf(0) }
    val density = LocalDensity.current
    val windowHeightDp = with(density) { windowHeightPx.toDp() }

    val isSmallScreen = windowHeightDp < 900.dp
    var sidebarVisible by remember { mutableStateOf(false) }

    println(currentRoute)


    println(currentRoute)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged() { windowHeightPx = it.height }
    ) {
        if (!isSmallScreen) {

            sidebarVisible = false

            Row(Modifier.fillMaxSize()) {
                Sidebar(currentRoute = currentRoute, onNavigate = navigate,sidebarVisible= sidebarVisible)
                ContentArea(currentRoute)
            }
        } else {
            ContentArea(currentRoute)

            AnimatedVisibility(
                visible = sidebarVisible,
                enter = slideInHorizontally(
                    initialOffsetX = { -it }, // entra da esquerda
                    animationSpec = tween(durationMillis = 300)
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { -it }, // sai esquerda
                    animationSpec = tween(durationMillis = 300)
                ),
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    Sidebar(
                        currentRoute = currentRoute,
                        onNavigate = {
                            navigate(it)
                            sidebarVisible = false
                        },
                        sidebarVisible = sidebarVisible
                    )
                }
            }
            IconButton(
                onClick = { sidebarVisible = !sidebarVisible },
                modifier = Modifier
                    .padding(8.dp)
                    .size(40.dp)
                    .align(Alignment.TopStart)
                    .background(Color(0xFF00002E), shape = MaterialTheme.shapes.small)
            ) {
                Icon(
                    painter = painterResource(
                        if (sidebarVisible) "icons/closeIcon.svg"
                        else "icons/hamburgerIcon.svg"
                    ),
                    contentDescription = "Menu",
                    tint = Color.White
                )
            }
        }
    }

}

@Composable
private fun ContentArea(currentRoute: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(24.dp)
    ) {
        when (currentRoute) {
            "/dashboard/home" -> HomeScreen()
            "/dashboard/usuario" -> UserScreen()
            else -> Text("Bem-vindo ao Dashboard!", color = Color.Black)
        }
    }
}