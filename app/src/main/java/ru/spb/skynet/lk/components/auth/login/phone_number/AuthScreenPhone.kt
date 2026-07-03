package ru.spb.skynet.lk.components.auth.login.phone_number

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ru.spb.skynet.lk.R
import ru.spb.skynet.lk.components.SkynetSnackbarHost
import ru.spb.skynet.lk.components.password.Password
import ru.spb.skynet.lk.components.phone.PhoneNumber
import ru.spb.skynet.lk.components.progress_bar.ProgressBar
import ru.spb.skynet.lk.data.network.NetworkState
import ru.spb.skynet.lk.ui.theme.SkynetGreen
import ru.spb.skynet.lk.viewModels.auth.AuthViewModel

@Composable
fun AuthScreenPhone(
    navController: NavController,
    viewModel: AuthViewModel
) {
    val loginState by viewModel.loginState.collectAsStateWithLifecycle()
    val state = loginState
    val snackbarHostState = remember { SnackbarHostState() }

    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val abonent = viewModel.abonentState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.resetState()
    }

    LaunchedEffect(state) {
        when (state) {
            is NetworkState.Success -> {
                if (abonent.value?.info?.hasPincode  == true) {
                    navController.navigate("pin_code") {
                        popUpTo("auth_phone") { inclusive = true }
                    }
                    navController.navigate("main") {
                        popUpTo("auth_phone") { inclusive = true }
                    }
                }
                viewModel.resetState()
            }
            is NetworkState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetState()
            }
            else -> { /* idle / loading */ }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Основной контент
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .statusBarsPadding()
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(R.string.auth_title),
                    color = Color.Black,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = stringResource(R.string.auth_hint),
                    color = Color.Black,
                    fontWeight = FontWeight.Light
                )

                Spacer(modifier = Modifier.height(10.dp))

                PhoneNumber(
                    onValueChange = {
                        phoneNumber = it
                        Log.d("phoneNumber", it)
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Password(
                    onValueChange = {
                        password = it
                        Log.d("password", it)
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = phoneNumber.isNotBlank() && password.isNotBlank() && password.length >= 3,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SkynetGreen,
                        contentColor = Color.White,
                        disabledContainerColor = Color.LightGray,
                        disabledContentColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(12.dp),
                    onClick = {
                        keyboardController?.hide()
                        viewModel.login(phoneNumber, password)
                    },
                ) {
                    Text(stringResource(R.string.login))
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        keyboardController?.hide()
                        navController.navigate("auth_contract")
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    border = BorderStroke(1.5.dp, SkynetGreen),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.White,
                        disabledContainerColor = Color.Gray,
                        disabledContentColor = Color.LightGray
                    ),
                ) {
                    Text("Войти по номеру договора", color = SkynetGreen)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(0.dp, Color.Transparent),
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    onClick = {
                        keyboardController?.hide()
                        navController.navigate("password")
                    },
                ) {
                    Text("Забыли пароль", color = SkynetGreen)
                }
            }
        }

        // Snackbar сверху
        SkynetSnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 46.dp),
            containerColor = Color.Red,
            contentColor = Color.White,
            icon = Icons.Default.Info,
            shapeRadius = 12.dp,
            elevation = 4.dp
        )
    }

    // Прогресс-бар поверх всего
    if (state is NetworkState.Loading) {
        ProgressBar()
    }
}