package org.warehouse.app.context

import androidx.compose.runtime.*

data class NavigationState(
    val currentRoute: String,
    val navigate: (String) -> Unit
)

val LocalNavigation = compositionLocalOf<NavigationState> {
    error("NavigationState not provided")
}