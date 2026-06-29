package ru.spb.skynet.lk.components.auth.login.contract_number

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ru.spb.skynet.lk.R
import ru.spb.skynet.lk.components.SkynetSnackbarHost
import ru.spb.skynet.lk.components.password.Password
import ru.spb.skynet.lk.components.progress_bar.ProgressBar
import ru.spb.skynet.lk.data.network.NetworkState
import ru.spb.skynet.lk.ui.theme.SkynetGray
import ru.spb.skynet.lk.ui.theme.SkynetGreen
import ru.spb.skynet.lk.viewModels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreenContract(navController: NavController, viewModel: AuthViewModel) {
    val loginState by viewModel.loginState.collectAsStateWithLifecycle()
    val state = loginState
    val keyboardController = LocalSoftwareKeyboardController.current
    var password by remember { mutableStateOf("") }
    var contractNumber by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.resetState()
    }

    LaunchedEffect(state) {
        when (state) {
            is NetworkState.Success -> {
                navController.navigate("pin_code") {
                    popUpTo("auth_phone") { inclusive = true }
                }
            }
            is NetworkState.Error -> {
                snackbarHostState.showSnackbar(state.message)
            }
            else -> { /* idle / loading */ }
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                    title = { Text("") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(innerPadding)
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                Text(
                    text = stringResource(R.string.auth_title_contract),
                    color = Color.Black,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        contentColor = SkynetGray
                    ),
                    modifier = Modifier.fillMaxWidth().size(56.dp)
                ) {
                    TextField(
                        value = contractNumber,
                        onValueChange = {
                            contractNumber = it
                        },
                        placeholder = { Text(stringResource(R.string.contract_number)) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            errorBorderColor = Color.Red,
                            cursorColor = SkynetGreen,
                        ),
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        contentColor = SkynetGray

                    ),
                    modifier = Modifier.fillMaxWidth().size(56.dp)
                ) {

                    Password(
                        onValueChange = {
                            password = it
                        }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = contractNumber.isNotBlank() && password.isNotBlank() && password.length >= 3,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SkynetGreen,
                        contentColor = Color.White,
                        disabledContainerColor = Color.Gray,
                        disabledContentColor = Color.LightGray
                    ),
                    shape = RoundedCornerShape(12.dp),
                    onClick = {
                        keyboardController?.hide()
                        viewModel.login(contractNumber, password)
                    },
                ) {
                    Text(stringResource(R.string.next))
                }
            }
        }
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
        if (loginState is NetworkState.Loading) {
            ProgressBar()
        }
    }
}