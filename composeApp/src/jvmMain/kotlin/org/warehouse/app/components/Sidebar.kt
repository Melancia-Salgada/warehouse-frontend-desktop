package org.warehouse.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Sidebar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    sidebarVisible: Boolean
) {
    BoxWithConstraints(
        modifier = Modifier
            .width(280.dp)
            .fillMaxHeight()
    ) {
        SidebarContent(currentRoute, onNavigate, sidebarVisible)
    }
}

@Composable
private fun SidebarContent(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    sidebarVisible: Boolean
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .width(280.dp)
            .fillMaxHeight()
            .background(Color(0xFF00002E))
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp),
                horizontalArrangement = Arrangement.Start
            ) {

                if(sidebarVisible) {
                    Spacer(modifier = Modifier.width(30.dp))
                }

                Text(
                    text = "Warehouse",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp, bottom = 40.dp)
                )
            }

            SidebarSection(
                title = null,
                items = listOf(
                    SidebarItem("Home", "/dashboard/home", "icons/homeIcon.svg")
                ),
                currentRoute,
                onNavigate
            )

            SidebarSection(
                title = "Armazém",
                items = listOf(
                    SidebarItem("Fornecedor", "/dashboard/fornecedor", "icons/fornecedorIcon.svg"),
                    SidebarItem("Produtos", "/dashboard/produtos", "icons/produtosIcon.svg"),
                    SidebarItem("Local", "/dashboard/local", "icons/localIcon.svg"),
                    SidebarItem("Estoque", "/dashboard/estoque", "icons/estoqueIcon.svg"),
                ),
                currentRoute,
                onNavigate
            )

            SidebarSection(
                title = "Produção",
                items = listOf(
                    SidebarItem("Pedido", "/dashboard/pedido", "icons/pedidoIcon.svg"),
                    SidebarItem("Matéria-Prima", "/dashboard/materiaprima", "icons/materiaprimaIcon.svg"),
                    SidebarItem("Maquinário", "/dashboard/maquinario", "icons/maquinarioIcon.svg"),
                    SidebarItem("Tipo de Maquinário", "/dashboard/tipomaquinario", "icons/tipoMaquinarioIcon.svg"),
                    SidebarItem("Ordem de Serviço", "/dashboard/ordemservico", "icons/servicoIcon.svg"),
                    SidebarItem("Moldes", "/dashboard/moldes", "icons/moldeIcon.svg"),
                ),
                currentRoute,
                onNavigate
            )

            SidebarSection(
                title = "Pessoas",
                items = listOf(
                    SidebarItem("Clientes", "/dashboard/clientes", "icons/clienteIcon.svg"),
                    SidebarItem("Usuários", "/dashboard/usuario", "icons/usuarioIcon.svg"),
                ),
                currentRoute,
                onNavigate
            )
        }

        SidebarSection(
            title = null,
            items = listOf(SidebarItem("Sair", "/", "icons/sairIcon.svg")),
            currentRoute,
            onNavigate,
            ultimo = true
        )
    }
}

data class SidebarItem(val text: String, val route: String, val iconPath: String)

@Composable
private fun SidebarSection(
    title: String?,
    items: List<SidebarItem>,
    currentRoute: String,
    onNavigate: (String) -> Unit,
    ultimo: Boolean = false
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        if (title != null) {
            Text(
                text = title,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp, top = 16.dp)
            )
        }

        items.forEach { item ->
            SidebarButton(
                text = item.text,
                iconPath = item.iconPath,
                active = currentRoute == item.route,
                onClick = { onNavigate(item.route) }
            )
        }

        if (!ultimo) {
            Divider(
                color = Color.White.copy(alpha = 0.2f),
                thickness = 1.dp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun SidebarButton(
    text: String,
    iconPath: String,
    active: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (active) Color.White.copy(alpha = 0.15f) else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, shape = MaterialTheme.shapes.small)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(iconPath),
            contentDescription = text,
            tint = Color.White.copy(alpha = 0.85f),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = text, color = Color.White, fontSize = 16.sp)
    }
}
