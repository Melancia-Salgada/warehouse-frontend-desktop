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
import org.warehouse.app.components.Campo
import org.warehouse.app.components.NovoButton
import org.warehouse.app.components.Table

@Composable
fun UserScreen() {

    val campos = listOf(
        Campo("name", "Nome"),
        Campo("email", "Email"),
        Campo("typeAccess", "Tipo")
    )


    var searchTerm by remember { mutableStateOf("") }
    var items by remember { mutableStateOf(
        listOf(
            mapOf("id" to 1, "name" to "Gabriel Almeida", "email" to "gabriel@email.com", "typeAccess" to "Admin"),
            mapOf("id" to 2, "name" to "João Pedro", "email" to "joao@email.com", "typeAccess" to "User"),
            mapOf("id" to 3, "name" to "Natália Lee", "email" to "natalia@email.com", "typeAccess" to "User"),
            mapOf("id" to 4, "name" to "Leonardo Queiroz", "email" to "leonardo@email.com", "typeAccess" to "Admin")
        )
    ) }


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
        // Campo de busca
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
                campos = campos,
                onSave = { data ->
                    println("Dados salvos: $data")
                }
            )
        }

        Spacer(Modifier.height(16.dp))

        // Lista de usuários
        Table(
            title = "Detalhes do Usuário",
            campos = campos,
            itens = filteredItems,
            onUpdate = { updated ->

            },
            onDelete = { toDelete ->
                items = items.filterNot { it["id"] == toDelete["id"] }
            }
        )

        // Modal para criar novo usuário

    }
}

@Composable
fun NovoUsuarioDialog(
    onSave: (Map<String, String>) -> Unit,
    onClose: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var typeAccess by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.padding(16.dp),
        tonalElevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Novo Usuário", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nome") })
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = typeAccess, onValueChange = { typeAccess = it }, label = { Text("Tipo de Acesso") })
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Senha") })
            Spacer(Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        onSave(
                            mapOf(
                                "name" to name,
                                "email" to email,
                                "typeAccess" to typeAccess,
                                "password" to password
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00002E))
                ) {
                    Text("Salvar")
                }
                OutlinedButton(onClick = onClose) {
                    Text("Cancelar")
                }
            }
        }
    }
}
