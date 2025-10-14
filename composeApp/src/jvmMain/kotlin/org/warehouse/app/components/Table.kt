package org.warehouse.app.components

import StyledDropdown
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.WindowSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.warehouse.app.network.ApiContext
import kotlin.collections.set

data class Campo(
    val nome: String,
    val label: String
)


@Composable
fun Table(
    title: String,
    campos: List<Campo>,
    itens: List<Map<String, Any>>,
    onUpdate: ((Map<String, Any?>) -> Unit)? = null,
    onDelete: ((Map<String, Any?>) -> Unit)? = null
) {
    var selectedItem by remember { mutableStateOf<Map<String, Any>?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .border(1.dp, Color(0xFFCCCCCC), shape = RoundedCornerShape(8.dp))
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Cabeçalho fixo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 12.dp, horizontal = 38.dp)
            ) {
                campos.forEach { campo ->
                    Text(
                        text = campo.label,
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }
                Text(
                    text = "Ação",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }

            // Linhas da lista
            itens.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(0.5.dp, Color(0xFF878787))
                        .padding(vertical = 12.dp, horizontal = 38.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    campos.forEach { campo ->
                        Text(
                            text = item[campo.nome]?.toString() ?: "",
                            modifier = Modifier.weight(1f),
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Button(
                            onClick = { selectedItem = item },
                            modifier = Modifier.height(38.dp).pointerHoverIcon(PointerIcon.Hand),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00002E)),
                            shape = MaterialTheme.shapes.small,
                            contentPadding = PaddingValues(8.dp)
                        ) {
                            Image(
                                painter = painterResource("icons/eyeIcon.svg"),
                                contentDescription = "Visualizar"
                            )
                        }
                    }
                }
            }
        }

        // Modal de visualização
        if (selectedItem != null) {
            Dialog(
                onCloseRequest = { selectedItem = null },
                state = DialogState(size = DpSize(600.dp, 550.dp)),
                title = "Detalhar Info"
            ) {
                VisualizarDialog(

                    title = title,
                    item = selectedItem!!,
                    campos = campos,
                    onClose = { selectedItem = null },
                    onUpdate = { updatedItem ->
                        onUpdate?.invoke(updatedItem)
                        selectedItem = updatedItem
                    },
                    onDelete = { onDelete?.invoke(it) }

                )
            }
        }
    }
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun VisualizarDialog(
    title: String,
    item: Map<String, Any>,
    campos: List<Campo>,
    onClose: () -> Unit,
    onUpdate: (Map<String, Any>) -> Unit,
    onDelete: (Map<String, Any>) -> Unit
) {
    var editDialogVisible by remember { mutableStateOf(false) }
    var confirmDelete by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .defaultMinSize(minWidth = 400.dp, minHeight = 300.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()

        ) {
            Column(modifier = Modifier.background(Color(0xFFF5F5F5)).padding(16.dp)) {
                // Botões no topo direito
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()

                ) {
                    // Título maio
                    Text(
                        title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp),
                    )

                    Row() {
                        IconButton(
                            onClick = { confirmDelete = true },
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = Color(0xFF00002E),
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .pointerMoveFilter(
                                    onEnter = { true; false },
                                    onExit = { false; false }
                                )
                                .pointerHoverIcon(PointerIcon.Hand),
                        ) {
                            Icon(
                                painterResource("icons/trashIcon.svg"),
                                contentDescription = "Excluir",
                                modifier = Modifier.size(24.dp),
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Botão de editar
                        IconButton(
                            onClick = { editDialogVisible = true },
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = Color(0xFF00002E),
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .pointerHoverIcon(PointerIcon.Hand)
                        ) {
                            Icon(
                                painterResource("icons/pencilIcon.svg"),
                                contentDescription = "Editar",
                                modifier = Modifier.size(24.dp),
                                tint = Color.White
                            )
                        }
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))



                // Campos destacados (como <strong> no React)
                campos.forEach { campo ->
                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text(
                            "${campo.label}: ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            item[campo.nome]?.toString() ?: "",
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }

    // edita
    if (editDialogVisible) {
        Dialog(onCloseRequest = { editDialogVisible = false },
            state = DialogState(size = DpSize(600.dp, 550.dp)),
            title= "Editar Registro"
            ) {
            PopupEditarDialog(
                item = item,
                campos = campos,
                onUpdate = {
                    onUpdate(it)
                    editDialogVisible = false
                },
                onClose = { editDialogVisible = false }
            )
        }
    }

    //  deleta
    if (confirmDelete) {
        Dialog(
            onCloseRequest = { confirmDelete = false },
            title = "Confirmação deletar usuário",
            state = DialogState(size = DpSize(400.dp, 200.dp)),
            resizable = false
        ) {
        Surface(
            modifier = Modifier.background(Color(0xFFF4F4F4)),
                shape = RoundedCornerShape(8.dp),
                color = Color.White,
                tonalElevation = 8.dp,

            ) {
                Column(
                    modifier = Modifier.background(Color(0xFFF4F4F4)).padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Tem certeza?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Você não poderá reverter isso!")

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { confirmDelete = false }) {
                            Text("Cancelar",  color = Color(0xFF00008F), modifier = Modifier.pointerHoverIcon(PointerIcon.Hand))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = {
                            onDelete(item)
                            confirmDelete = false
                            onClose()
                        }, modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)) {
                            Text("Sim, deletar!",color = Color(0xFF00008F))
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun PopupEditarDialog(
    item: Map<String, Any>,
    campos: List<Campo>,
    onUpdate: (Map<String, Any>) -> Unit,
    onClose: () -> Unit
) {
    var editedItem by remember { mutableStateOf(item.toMutableMap()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }


    Surface(
        modifier = Modifier
            .fillMaxSize()
            .heightIn(min = 500.dp)
            .background(Color(0xFFF4F4F4))
            .background(Color(0xFFF4F4F4))
    ) {
        Box(modifier = Modifier.background(Color(0xFFF4F4F4)).padding(24.dp)) {
            Column {
                Text(
                    "Editar Registro",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Campos
                campos.forEach { campo ->
                    var value by remember { mutableStateOf(editedItem[campo.nome]?.toString() ?: "") }

                    if (campo.nome == "typeAccess") {
                        StyledDropdown(
                            options = listOf("USER", "ADMIN"),
                            initialOption = value,
                            onOptionSelected = { selected ->
                                value = selected
                                editedItem[campo.nome] = selected
                            },
                            label = "Tipo de Acesso"
                        )
                    } else {
                        OutlinedTextField(
                            value = value,
                            onValueChange = {
                                value = it
                                editedItem[campo.nome] = it
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
                            val emptyField = campos.find { editedItem[it.nome].toString().isBlank() }
                            if (emptyField != null) {
                                errorMessage = "O campo \"${emptyField.label}\" não pode estar vazio."
                                return@Button
                            }
                            onUpdate(editedItem)
                            onClose()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00002E)),
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)

                    ) {
                        Text("Salvar", color = Color.White)
                    }

                    OutlinedButton(onClick = onClose, modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)) {
                        Text("Cancelar")
                    }
                }
            }
        }
    }

}
