import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.spb.skynet.lk.ui.theme.SkynetGreen

@Composable
fun PinCodeField(
    onComplete: (String) -> Unit,
    modifier: Modifier = Modifier,
    length: Int = 4
) {
    var code by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Скрытое поле ввода
        OutlinedTextField(
            value = code,
            onValueChange = { newValue ->
                val digits = newValue.filter { it.isDigit() }.take(length)
                if (digits != code) {
                    code = digits
                    if (code.length == length) {
                        onComplete(code)
                    }
                }
            },
            modifier = Modifier
                .width(1.dp)
                .height(1.dp)
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            visualTransformation = PasswordVisualTransformation(), // маскируем звёздочками
            singleLine = true,
            textStyle = TextStyle(fontSize = 0.sp) // невидимый текст
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Ряд с кружками
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(length) { index ->
                PinDigit(
                    digit = code.getOrNull(index)?.toString() ?: "",
                    isActive = index == code.length
                )
                if (index < length - 1) {
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }
    }
}

@Composable
private fun PinDigit(
    digit: String,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(20.dp)
            .height(20.dp),
        shape = RoundedCornerShape(10.dp),
        color = if (isActive) SkynetGreen else Color.LightGray
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("●", color = Color.Transparent, fontSize = 24.sp)
        }
    }
}