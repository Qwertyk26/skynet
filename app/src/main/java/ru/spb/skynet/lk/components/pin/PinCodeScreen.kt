package ru.spb.skynet.lk.components.pin

import PinCodeField
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.spb.skynet.lk.R
import ru.spb.skynet.lk.viewModels.AuthViewModel

@Composable
fun PinCodeScreen(navController: NavController, viewModel: AuthViewModel) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 20.dp), // симметричные отступы
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = context.getString(R.string.enter_pin),
            color = Color.Black,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        PinCodeField(
            length = 4,
            onComplete = { pin ->
                // отправка пин-кода
            }
        )
    }
}