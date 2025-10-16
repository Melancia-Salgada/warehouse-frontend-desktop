package org.warehouse.app.screens.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.warehouse.app.components.Campo
import org.warehouse.app.components.NovoButton
import org.warehouse.app.components.Table
import org.warehouse.app.model.Fornecedor
import org.warehouse.app.network.ApiContext

@Composable
fun FornecedorScreen() {

    val camposList = listOf(
        Campo("nome", "Nome"),
        Campo("telefone", "Telefone"),
        Campo("email", "Email"),
        Campo("cnpj", "CNPJ")
    )

    val camposVisu = listOf(
        Campo("id", "ID"),
        Campo("nome", "Nome"),
        Campo("telefone", "Telefone"),
        Campo("email", "Email"),
        Campo("cnpj", "CNPJ"),
        Campo("status", "Status"),
        Campo("dataCriacao", "Data de Criação")
    )

    val camposEdit = listOf(
        Campo("nome", "Nome"),
        Campo("telefone", "Telefone"),
        Campo("email", "Email"),
        Campo("cnpj", "CNPJ")
    )

    val camposCriar = listOf(
        Campo("nome", "Nome"),
        Campo("telefone", "Telefone"),
        Campo("email", "Email"),
        Campo("cnpj", "CNPJ")
    )


    var searchTerm by remember { mutableStateOf("") }
    var items by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            println("Buscando fornecedores...")
            val fornecedores = ApiContext.getFornecedores()
            println("Fornecedores recebidos: $fornecedores")

            items = fornecedores.map { fornecedor ->
                mapOf<String, Any>(
                    "id" to (fornecedor.id ?: 0),
                    "nome" to (fornecedor.nome ?: ""),
                    "telefone" to (fornecedor.telefone ?: ""),
                    "email" to (fornecedor.email ?: ""),
                    "cnpj" to (fornecedor.cnpj ?: ""),
                    "status" to (fornecedor.status ?: false),
                    "dataCriacao" to (fornecedor.dataCriacao ?: "")
                )
            }
            isLoading = false
        } catch (e: Exception) {
            e.printStackTrace()
            errorMessage = "Erro ao carregar fornecedores: ${e.message}"
            isLoading = false
        }
    }

    val filteredItems = items.filter { item ->
        val id = item["id"]?.toString()?.lowercase() ?: ""
        val nome = item["nome"]?.toString()?.lowercase() ?: ""
        val email = item["email"]?.toString()?.lowercase() ?: ""
        val cnpj = item["cnpj"]?.toString()?.lowercase() ?: ""
        val telefone = item["telefone"]?.toString()?.lowercase() ?: ""
        searchTerm.lowercase() in id ||
                searchTerm.lowercase() in nome ||
                searchTerm.lowercase() in email ||
                searchTerm.lowercase() in cnpj ||
                searchTerm.lowercase() in telefone
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
            label = { Text("Pesquisar fornecedor...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Cabeçalho + botão de novo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Fornecedores",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )

            NovoButton(
                campos = camposCriar,
                onSave = { novoFornecedor ->
                    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                        try {
                            val fornecedor = Fornecedor(
                                nome = novoFornecedor["nome"].toString(),
                                telefone = novoFornecedor["telefone"].toString(),
                                email = novoFornecedor["email"].toString(),
                                cnpj = novoFornecedor["cnpj"].toString(),
                                status = true
                            )

                            val created = ApiContext.createFornecedor(fornecedor)

                            withContext(kotlinx.coroutines.Dispatchers.Main) {
                                items = items + mapOf<String, Any>(
                                    "id" to (created?.id ?: 0),
                                    "nome" to (created?.nome ?: ""),
                                    "telefone" to (created?.telefone ?: ""),
                                    "email" to (created?.email ?: ""),
                                    "cnpj" to (created?.cnpj ?: ""),
                                    "status" to(created?.status?: true),
                                    "dataCriacao" to (created.dataCriacao ?: "")
                                )
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            withContext(kotlinx.coroutines.Dispatchers.Main) {
                                println("Erro ao criar fornecedor: ${e.message}")
                            }
                        }
                    }
                }
            )
        }

        Spacer(Modifier.height(16.dp))

        // Tabela
        Table(
            title = "Detalhes do Fornecedor",
            camposVisu = camposVisu,
            camposEdit = camposEdit,
            camposList = camposList,
            itens = filteredItems,
            onUpdate = { updatedItem ->
                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                    try {
                        val id = (updatedItem["id"] as? Int) ?: return@launch

                        val updatedFornecedor = Fornecedor(
                            id = id,
                            nome = updatedItem["nome"]?.toString() ?: "",
                            telefone = updatedItem["telefone"]?.toString() ?: "",
                            email = updatedItem["email"]?.toString() ?: "",
                            cnpj = updatedItem["cnpj"]?.toString() ?: "",
                            dataCriacao = ""
                        )

                        ApiContext.updateFornecedor(id, updatedFornecedor)

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
                        ApiContext.deleteFornecedor(id)

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
