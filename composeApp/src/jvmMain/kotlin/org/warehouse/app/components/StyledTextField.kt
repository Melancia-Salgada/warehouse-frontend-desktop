package org.warehouse.app.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation


@Composable
fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 16.sp, fontWeight = FontWeight.Normal) },
        modifier = modifier
            .height(68.dp)
            .onFocusChanged { focusState -> isFocused = focusState.isFocused }
            .padding(horizontal = 4.dp),
        textStyle = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF00008F),
            unfocusedBorderColor = Color(0xFF00008F),
            cursorColor = Color(0xFF00008F),
            focusedLabelColor = Color(0xFF00008F),
            unfocusedLabelColor = Color.Gray
        ),
        singleLine = true,
        visualTransformation = VisualTransformation.None
    )
}

@Composable
fun StyledPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 16.sp, fontWeight = FontWeight.Normal) },
        modifier = modifier
            .height(68.dp)
            .onFocusChanged { focusState -> isFocused = focusState.isFocused }
            .padding(horizontal = 4.dp),
        textStyle = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF00008F),
            unfocusedBorderColor = Color(0xFF00008F),
            cursorColor = Color(0xFF00008F),
            focusedLabelColor = Color(0xFF00008F),
            unfocusedLabelColor = Color.Gray
        ),
        singleLine = true,
        visualTransformation = PasswordVisualTransformation()
    )
}
