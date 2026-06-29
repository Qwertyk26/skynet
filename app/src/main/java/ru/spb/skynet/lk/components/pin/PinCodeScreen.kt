package ru.spb.skynet.lk.components.pin

import PinCodeField
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ru.spb.skynet.lk.R
import ru.spb.skynet.lk.components.SkynetSnackbarHost
import ru.spb.skynet.lk.components.progress_bar.ProgressBar
import ru.spb.skynet.lk.data.models.request.pin_code.PinCodeRequest
import ru.spb.skynet.lk.data.network.NetworkState
import ru.spb.skynet.lk.viewModels.AuthViewModel

@Composable
fun PinCodeScreen(navController: NavController, viewModel: AuthViewModel) {

    val loginState by viewModel.loginState.collectAsStateWithLifecycle()
    val state = loginState
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state) {
        when (state) {
            is NetworkState.Success -> {
                navController.navigate("greeting") {
                    popUpTo("pin_code") { inclusive = true }
                }
                viewModel.resetState()
            }
            is NetworkState.Error -> {
                snackBarHostState.showSnackbar(state.message)
                viewModel.resetState()
            }
            else -> { /* idle / loading */ }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.enter_pin),
                color = Color.Black,
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            PinCodeField(
                length = 4,
                onComplete = { pin ->
                    keyboardController?.hide()
                    val pinCodeRequest = PinCodeRequest(pin)
                    viewModel.refresh(pinCodeRequest)
                },
                modifier = Modifier.wrapContentWidth()
            )
        }

        // Snackbar сверху
        SkynetSnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 46.dp),
            containerColor = Color.Red,
            contentColor = Color.White,
            icon = Icons.Default.Info,
            shapeRadius = 12.dp,
            elevation = 4.dp
        )

        if (state is NetworkState.Loading) {
            ProgressBar()
        }
    }
}