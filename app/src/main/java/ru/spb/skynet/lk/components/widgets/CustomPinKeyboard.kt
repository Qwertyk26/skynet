package ru.spb.skynet.lk.components.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.spb.skynet.lk.R

@Composable
fun CustomPinKeyboard(
    showBiometricButton: Boolean,
    onNumberClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onBiometricClick: () -> Unit
) {
    val rows = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9")
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Первые 3 ряда цифр (1-9)
        rows.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(40.dp)) {
                row.forEach { digit ->
                    KeyboardKey(text = digit, onClick = { onNumberClick(digit) })
                }
            }
        }

        // Последний ряд (Биометрия, 0, Удаление)
        Row(horizontalArrangement = Arrangement.spacedBy(40.dp)) {
            // Левая кнопка: Биометрия (если включена)
            if (showBiometricButton) {
                KeyboardIconButton(
                    // Замените на вашу иконку отпечатка (например, R.drawable.ic_fingerprint)
                    iconRes = R.drawable.baseline_fingerprint_24,
                    onClick = onBiometricClick
                )
            } else {
                Spacer(modifier = Modifier.size(72.dp)) // Пустое место для сохранения геометрии
            }

            // Центральная кнопка: Ноль
            KeyboardKey(text = "0", onClick = { onNumberClick("0") })

            // Правая кнопка: Стереть символ
            KeyboardIconButton(
                iconRes = R.drawable.baseline_backspace_24, // Замените на вашу иконку стирания
                onClick = onDeleteClick
            )
        }
    }
}

@Composable
private fun KeyboardKey(text: String, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple()
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
    }
}

@Composable
private fun KeyboardIconButton(iconRes: Int, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple()
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            tint = Color.Black
        )
    }
}
