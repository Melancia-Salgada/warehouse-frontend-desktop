package org.warehouse.app.screens

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.warehouse.app.components.ButtonType
import org.warehouse.app.components.StyledButton
import org.warehouse.app.components.StyledPasswordField
import org.warehouse.app.components.StyledTextField
import org.warehouse.app.context.LocalNavigation

@Composable
@Preview
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var lembrar by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    var windowW by remember { mutableStateOf(0) }

    val nav = LocalNavigation.current

    fun handleLogin() {
        kotlinx.coroutines.MainScope().launch {
            kotlinx.coroutines.delay(2000)
            isLoading = false
            nav.navigate("/dashboard/usuario")
        }
    }



    Box(modifier = Modifier.fillMaxSize().onSizeChanged{windowW =it.width}) {
        // Banner lateral (imagem de fundo)

        val density = LocalDensity.current
        val windowWDP = with(density) {windowW.toDp().value}

        val fontSize = (windowWDP * 0.05f)
            .coerceIn(32f, 56f)
            .sp

        val supportFontSize = (windowWDP * 0.02f)
            .coerceIn(14f, 20f)
            .sp


        Image(
            painter = painterResource("img/banner.png"),
            contentDescription = "Banner",
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.6f),
            contentScale = ContentScale.Crop
        )

        // Conteudo o banner
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.6f)
                .padding(start = 60.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.Center,

        ) {
            Text(
                text = "Bem-vindo",
                fontSize = fontSize,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                lineHeight = fontSize * 1.15f

            )
            Row(){
                Text(
                    text = "ao",
                    fontSize = fontSize,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    lineHeight = fontSize * 1.15f
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Warehouse!",
                    fontSize = fontSize,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF00008F),
                    lineHeight = fontSize * 1.15f
                )
            }

            Spacer(Modifier.height(24.dp))
            Text("A solução para organizar, proteger", fontSize = supportFontSize)
            Text("e gerenciar os dados da sua empresa.", fontSize = supportFontSize)
        }

        //formulario
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
                .fillMaxWidth(0.4f)
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

                StyledTextField(value = email,
                    onValueChange = { email = it },
                    label = "Usuário",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                StyledPasswordField(
                    value = senha,
                    onValueChange = { senha = it },
                    label = "Senha",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = lembrar, onCheckedChange = { lembrar = it }, modifier = Modifier.pointerHoverIcon(PointerIcon.Hand))
                        Text("Lembre de mim", fontSize = 16.sp)
                    }
                    Text("Redefinir", fontSize = 16.sp, color = Color(0xFF005BB1),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                            .pointerHoverIcon(PointerIcon.Hand)
                            .background(Color.Transparent)
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                            .clickable (indication = null, interactionSource = remember { MutableInteractionSource()}){
                                //TODO: abrir email
                        }
                    )
                }

                Spacer(Modifier.height(20.dp))

                StyledButton(
                    type = ButtonType.BIG,
                    isLoading = isLoading,
                    enabled = !isLoading,
                    onClick = {
                        isLoading = true
                        handleLogin()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Entrar")
                }


                Spacer(Modifier.height(20.dp))

                Text(
                    text = "Ainda não tem o acesso?",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )

                Text("Entre em contato", fontSize = 14.sp, color = Color(0xFF005BB1),
                    modifier = Modifier
                        .pointerHoverIcon(PointerIcon.Hand)
                        .background(Color.Transparent)
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                        .clickable (indication = null, interactionSource = remember { MutableInteractionSource()}){
                            //TODO: abrir email
                        }
                )

            }
        }
    }
}
