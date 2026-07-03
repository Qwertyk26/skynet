package ru.spb.skynet.lk.components.dialogs.logout

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.spb.skynet.lk.R

@Composable
fun LogOutDialog(onDismiss: () -> Unit,
                 onConfirm: () -> Unit) {
    AlertDialog(
        containerColor = Color.White,
        shape = RoundedCornerShape(12.dp),
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Text(
                text = stringResource(R.string.confirm),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = stringResource(R.string.logout_subtitle)
            )
        },
        dismissButton =  {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Red
                ),
                onClick = {
                    onDismiss()
                }
            ) {
                Text(
                    text = stringResource(R.string.no)
                )
            }
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                onClick = {
                    onConfirm()
                }
            ) {
                Text(
                    text = stringResource(R.string.yes)
                )
            }
        },
    )
}