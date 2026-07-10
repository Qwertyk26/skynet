package ru.spb.skynet.lk.components.balance.pay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.spb.skynet.lk.R
import ru.spb.skynet.lk.ui.theme.SkynetGreen

@Composable
fun PayBottomSheet(onClose: () -> Unit) {
    var amount by remember { mutableStateOf("") }
    val numericAmount = amount.toDoubleOrNull() ?: 0.0
    val isAmountError = amount.isNotEmpty() && numericAmount < 10.0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 24.dp, end = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.top_up_card),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
                fontSize = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = onClose,
                modifier = Modifier.size(36.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            TextField(
                value = amount,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.matches(Regex("""^\d*[.,]?\d{0,2}$"""))) {
                        amount = newValue.replace(',', '.')
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                placeholder = {
                    Text(text = stringResource(R.string.write_off_amount), color = Color.LightGray)
                },
                singleLine = true,
                isError = isAmountError,
                supportingText = {
                    if (isAmountError) {
                        Text(
                            text = stringResource(R.string.error_min_amount),
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SkynetGreen,
                    unfocusedBorderColor = Color.LightGray,
                    errorBorderColor = Color.Red,
                    cursorColor = SkynetGreen,
                    errorCursorColor = Color.Red
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Row(modifier = Modifier.padding(end = 20.dp).fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.connect_card)
                )
                Switch(
                    modifier = Modifier.weight(1f).scale(0.75f).requiredHeight(24.dp),
                    checked = true,
                    onCheckedChange = {

                    },
                    colors = SwitchDefaults.colors(
                        checkedBorderColor = SkynetGreen,
                        checkedThumbColor = SkynetGreen,
                        checkedTrackColor = Color.White,
                    ),
                )
            }
            Button(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = amount.isNotEmpty() && !isAmountError,
                onClick = {

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = SkynetGreen
                )
            ) {
                Text(
                    text = stringResource(R.string.top_up_button_title),
                    color = if (amount.isNotEmpty() && !isAmountError) Color.White else Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
