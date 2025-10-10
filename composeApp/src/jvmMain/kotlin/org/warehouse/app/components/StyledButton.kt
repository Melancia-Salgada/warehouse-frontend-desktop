package org.warehouse.app.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

enum class ButtonType {
    BIG, SMALL, LIST
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun StyledButton(
    type: ButtonType,
    isLoading: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,

) {
    var isPressed by remember { mutableStateOf(false) }
    var isHovered by remember { mutableStateOf(false) }

    var widthDp by remember { mutableStateOf(0.dp)}

    val density = LocalDensity.current

    // Definindo cores baseadas no tipo e estado
    val backgroundColor = when (type) {
        ButtonType.BIG -> if (isPressed) Color(0xFF03035E) else Color(0xFF00002E)
        ButtonType.SMALL -> if (isPressed) Color(0xFF03035E) else Color(0xFF00002E)
        ButtonType.LIST -> when {
            !enabled -> Color(0xFF00002E).copy(alpha = 0.5f)
            isPressed -> Color(0xFF2F2FC4)
            isHovered -> Color(0xFF2F2FC4)
            else -> Color(0xFF00002E)
        }
    }

    val contentColor = when(type) {
        ButtonType.BIG -> Color(0xFFEAEAEA)
        ButtonType.SMALL -> Color.White
        ButtonType.LIST -> Color.White
    }

    val paddingValues = when (type) {
        ButtonType.BIG -> PaddingValues(20.dp)
        ButtonType.SMALL -> PaddingValues(8.dp)
        ButtonType.LIST -> PaddingValues(8.dp)
    }

    val fontWeight = when (type) {
        ButtonType.BIG -> FontWeight.Black
        ButtonType.SMALL -> FontWeight.Normal
        ButtonType.LIST -> FontWeight.Normal
    }

    val heightModifier = when(type) {
        ButtonType.SMALL -> Modifier.height(50.dp)
        else -> Modifier.height(IntrinsicSize.Min)
    }

    val fontSize = if (type == ButtonType.BIG && widthDp > 0.dp) {
        (widthDp.value * 0.07f).coerceIn(18f, 28f).sp
    } else {
        when (type) {
            ButtonType.BIG -> 24.sp
            ButtonType.SMALL -> 14.sp
            ButtonType.LIST -> 16.sp
        }
    }

    Box(
        modifier = modifier
            .then(heightModifier)
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(5.dp))
            .onSizeChanged { size ->
                widthDp = with(density) { size.width.toDp() }
            }
            .pointerHoverIcon(PointerIcon.Hand)
            .pointerMoveFilter(
                onEnter = {
                    isHovered = true
                    false
                },
                onExit = {
                    isHovered = false
                    false
                }
            )
            .clickable(
                enabled = enabled && !isLoading,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPressed = true
                onClick()
                isPressed = false
            }
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = contentColor,
                strokeWidth = 2.dp,
                modifier = Modifier.size(22.dp)
            )
        } else {
            Box(
                contentAlignment = Alignment.Center
            ) {
                ProvideTextStyle(
                    value = LocalTextStyle.current.copy(
                        fontSize = fontSize,
                        color = contentColor,
                        fontWeight = fontWeight
                    )
                ) {
                    content()
                }
            }
        }
    }
}

@Preview
@Composable
fun StyledButtonPreview() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StyledButton(
            type = ButtonType.BIG,
            isLoading = false,
            enabled = true,
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }

        StyledButton(
            type = ButtonType.BIG,
            isLoading = true,
            enabled = false,
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }

        StyledButton(
            type = ButtonType.SMALL,
            isLoading = false,
            enabled = true,
            onClick = { /* TODO */ },
            modifier = Modifier.width(150.dp)
        ) {
            Text("Pequeno")
        }

        StyledButton(
            type = ButtonType.LIST,
            isLoading = false,
            enabled = true,
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Lista")
        }
    }
}
