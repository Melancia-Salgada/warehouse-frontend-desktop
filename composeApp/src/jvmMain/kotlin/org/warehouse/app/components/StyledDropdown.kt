import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StyledDropdown(
    options: List<String>,
    initialOption: String = "Selecione uma opção",
    onOptionSelected: (String) -> Unit,
    label: String = "Escolha uma opção:"
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(initialOption) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(label)
        Spacer(modifier = Modifier.height(8.dp))

        Box {
            Button(onClick = { expanded = true }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00002E))) {
                Text(selected)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selected = option
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
