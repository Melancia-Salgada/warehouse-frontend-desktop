package org.warehouse.app.components

import StyledDropdown
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NovoButton(
    text: String = "Novo",
    campos: List<Campo>,
    onSave: (Map<String, String>) -> Unit

) {
    var hovered by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }

    Button(
        onClick = {showDialog = true},
        modifier = Modifier
            .animateContentSize(animationSpec = TweenSpec(durationMillis = 300))
            .padding(8.dp)
            .height(48.dp)
            .pointerHoverIcon(PointerIcon.Hand),
        colors = ButtonDefaults.buttonColors(containerColor = if (hovered) Color(0xFFFFB84D) else Color(0xFFFAA72C)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.pointerMoveFilter(
                onEnter = { hovered = true; false },
                onExit = { hovered = false; false }
            )
        ) {
            Icon(
                painter = painterResource("icons/plusIcon.svg"),
                contentDescription = "Adicionar",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    if (showDialog) {
        PopupNovoDialog(
            campos = campos,
            onClose = { showDialog = false },
            onSave = { data ->
                onSave(data)
                showDialog = false
            }
        )
    }
}

@Composable
fun PopupNovoDialog(
    campos: List<Campo>,
    onClose: () -> Unit,
    onSave: (Map<String, String>) -> Unit
) {
    var formData by remember {
        mutableStateOf(campos.associate { it.nome to "" }.toMutableMap())
    }
    var selected by remember { mutableStateOf("USER") }
    val options = listOf("USER", "ADMIN")
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(
        onCloseRequest = { onClose() },
        state = DialogState(size = DpSize(600.dp, 600.dp)),
        title = "Novo"
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .heightIn(min = 500.dp)
                .background(Color(0xFFF4F4F4))
        ) {
            Box(modifier = Modifier.background(Color(0xFFF4F4F4)).padding(24.dp)) {
                Column () {

                    Text(
                        "Novo Registro",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Campos
                    campos.forEach { campo ->
                        var value by remember { mutableStateOf(formData[campo.nome] ?: if (campo.nome == "typeAccess") "USER" else "") }

                        if (campo.nome == "typeAccess") {
                            StyledDropdown(
                                options = listOf("USER", "ADMIN"),
                                initialOption = "Selecione uma opção", // valor inicial
                                onOptionSelected = { selected ->
                                    value = selected
                                    formData[campo.nome] = selected
                                },
                                label = "Tipo de Acesso"
                            )
                        } else {
                            OutlinedTextField(
                                value = value,
                                onValueChange = {
                                    value = it
                                    formData[campo.nome] = it
                                },
                                label = { Text(campo.label) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = Color.Red,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                val emptyField = formData.entries.find { it.value.isBlank() }
                                if (emptyField != null) {
                                    errorMessage = "O campo \"${emptyField.key}\" não pode estar vazio."
                                    return@Button
                                }

                                val email = formData["email"]
                                if (email != null) {
                                    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
                                    if (!emailRegex.matches(email)) {
                                        errorMessage = "Por favor, insira um e-mail válido."
                                        return@Button
                                    }
                                }

                                onSave(formData)
                                onClose()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00002E))
                        ) {
                            Text("Criar", color = Color.White, modifier = Modifier.pointerHoverIcon(PointerIcon.Hand))
                        }

                        OutlinedButton(onClick = onClose) {
                            Text("Cancelar", modifier = Modifier.pointerHoverIcon(PointerIcon.Hand))
                        }
                    }
                }
            }
        }
    }
}
