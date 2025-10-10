package org.warehouse.app.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController

@Composable
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var lembrar by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Banner lateral (imagem de fundo)
        Image(
            painter = painterResource("img/banner.png"),
            contentDescription = "Banner",
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.6f),
            contentScale = ContentScale.Crop
        )

        // Conteúdo sobre o banner
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.6f)
                .padding(start = 60.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Bem-vindo",
                fontSize = 48.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                lineHeight = 56.sp
            )
            Text(
                text = "ao Warehouse!",
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF00008F),
                lineHeight = 56.sp
            )
            Spacer(Modifier.height(24.dp))
            Text("A solução para organizar, proteger", fontSize = 20.sp)
            Text("e gerenciar os dados da sua empresa.", fontSize = 20.sp)
        }

        // Painel branco (formulário)
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
                .width(400.dp)
                .background(Color.White)
                .padding(40.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Usuário") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = senha,
                    onValueChange = { senha = it },
                    label = { Text("Senha") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = lembrar, onCheckedChange = { lembrar = it })
                        Text("Lembre de mim", fontSize = 18.sp)
                    }
                    TextButton(onClick = { /* TODO: redefinir senha */ }) {
                        Text("Redefinir senha", fontSize = 18.sp, color = Color(0xFF005BB1))
                    }
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = { isLoading = true /* TODO: login */ },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    } else {
                        Text("Entrar", fontSize = 18.sp)
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Ainda não tem o acesso?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                TextButton(onClick = { /* TODO: abrir e-mail */ }) {
                    Text("Entre em contato", fontSize = 16.sp, color = Color(0xFF005BB1))
                }
            }
        }
    }
}
