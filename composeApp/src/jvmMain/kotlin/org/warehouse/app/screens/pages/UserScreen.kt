package org.warehouse.app.screens.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.warehouse.app.components.Campo
import org.warehouse.app.components.NovoButton
import org.warehouse.app.components.Table
import org.warehouse.app.model.User
import org.warehouse.app.network.ApiContext


@Composable
fun UserScreen() {

    val camposList = listOf(
        Campo("name", "Nome"),
        Campo("email", "Email"),
        Campo("typeAccess", "Tipo")
    )

    val camposVisu = listOf(
        Campo("id", "ID"),
        Campo("name", "Nome"),
        Campo("email", "Email"),
        Campo("typeAccess", "Tipo"),
        Campo("status", "Status"),
        Campo("dataCriacao", "Data de Criação")
    )

    val camposEdit = listOf(
        Campo("name", "Nome"),
        Campo("email", "Email"),
        Campo("typeAccess", "Tipo")
    )

    val camposCriar = listOf(
        Campo("name", "Nome"),
        Campo("email", "Email"),
        Campo("typeAccess", "Tipo"),
        Campo("password", "Senha")
    )


    var searchTerm by remember { mutableStateOf("") }
    var items by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val users = ApiContext.getUsers()
            items = users.map { user ->
                mapOf<String, Any>(
                    "id" to (user.id ?: 0),
                    "name" to (user.name ?: ""),
                    "email" to (user.email ?: ""),
                    "typeAccess" to (user.typeAccess ?: ""),
                    "status" to (user.status ?: false),
                    "dataCriacao" to (user.dataCriacao ?: "")
                )
            }
            isLoading = false
        } catch (e: Exception) {
            e.printStackTrace()
            errorMessage = "Erro ao carregar usuários: ${e.message}"
            isLoading = false
        }
    }



    var showDialog by remember { mutableStateOf(false) }


    val filteredItems = items.filter { item ->
        val id = item["id"]?.toString()?.lowercase() ?: ""
        val name = item["name"]?.toString()?.lowercase() ?: ""
        val email = item["email"]?.toString()?.lowercase() ?: ""
        val typeAccess = item["typeAccess"]?.toString()?.lowercase() ?: ""
        searchTerm.lowercase() in id || searchTerm.lowercase() in name || searchTerm.lowercase() in email || searchTerm.lowercase() in typeAccess
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F4F4))
            .padding(24.dp)
    ) {
        // busca
        OutlinedTextField(
            value = searchTerm,
            onValueChange = { searchTerm = it },
            label = { Text("Pesquisar usuário...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Usuários",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )

            NovoButton(
                campos = camposCriar,
                onSave = { novoUsuario ->
                    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                        try {
                            val user = org.warehouse.app.model.User(
                                name = novoUsuario["name"].toString(),
                                email = novoUsuario["email"].toString(),
                                password = novoUsuario["password"].toString(),
                                typeAccess = novoUsuario["typeAccess"].toString(),
                                status = true
                            )



                            val created = ApiContext.createUser(user)

                            withContext(kotlinx.coroutines.Dispatchers.Main) {
                                items = items + mapOf<String, Any>(
                                    "id" to (created.id ?: 0),
                                    "name" to (created.name ?: ""),
                                    "email" to (created.email ?: ""),
                                    "typeAccess" to (created.typeAccess ?: ""),
                                    "status" to (created.status ?: false),
                                    "dataCriacao" to (created.dataCriacao ?: "")
                                )
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            withContext(kotlinx.coroutines.Dispatchers.Main) {
                                println("Erro ao criar usuário: ${e.message}")
                            }
                        }
                    }
                }
            )
        }

        Spacer(Modifier.height(16.dp))

        // Lista
        Table(
            title = "Detalhes do Usuário",
            camposList = camposList,
            camposVisu = camposVisu,
            camposEdit = camposEdit,
            itens = filteredItems,
            onUpdate = { updatedItem ->
                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                    try {
                        val id = (updatedItem["id"] as? Int) ?: return@launch

                        val updatedUser = User(
                            id = id,
                            name = updatedItem["name"]?.toString() ?: "",
                            email = updatedItem["email"]?.toString() ?: "",
                            password = updatedItem["password"]?.toString() ?: "",
                            typeAccess = updatedItem["typeAccess"]?.toString() ?: ""
                        )

                        ApiContext.updateUser(id, updatedUser)

                        withContext(kotlinx.coroutines.Dispatchers.Main) {
                            items = items.map {
                                if (it["id"] == id) {
                                    updatedItem.mapValues { entry -> entry.value ?: "" }
                                } else it
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            },
            onDelete = { deletedItem ->
                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                    try {
                        val id = (deletedItem["id"] as? Int) ?: return@launch
                        ApiContext.deleteUser(id)

                        withContext(kotlinx.coroutines.Dispatchers.Main) {
                            items = items.filterNot { it["id"] == id }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        )

    }
}
