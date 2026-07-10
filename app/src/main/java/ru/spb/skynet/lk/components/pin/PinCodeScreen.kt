package ru.spb.skynet.lk.components.pin

import PinCodeDisplayFields
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ru.spb.skynet.lk.R
import ru.spb.skynet.lk.components.SkynetSnackbarHost
import ru.spb.skynet.lk.components.progress_bar.ProgressBar
import ru.spb.skynet.lk.components.widgets.CustomPinKeyboard
import ru.spb.skynet.lk.data.models.request.pin_code.PinCodeRequest
import ru.spb.skynet.lk.data.network.NetworkState
import ru.spb.skynet.lk.viewModels.auth.AuthViewModel
import ru.spb.skynet.lk.viewModels.pin_code.PinCodeViewModel
import ru.spb.skynet.lk.viewModels.settings.SettingsViewModel

@Composable
fun PinCodeScreen(
    navController: NavController,
    viewModel: PinCodeViewModel,
    settingsViewModel: SettingsViewModel
) {
    val loginState by viewModel.networkState.collectAsStateWithLifecycle()
    val state = loginState
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val activity = context as? FragmentActivity

    val useBiometric by settingsViewModel.useBiometric.collectAsStateWithLifecycle()

    var pinCodeInput by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        settingsViewModel.getBiometric()
    }

    val triggerBiometric = {
        if (useBiometric == true && activity != null) {
            showBiometricPrompt(
                activity = activity,
                onSuccess = {
                    navController.navigate("greeting") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                },
                onError = { errorMessage ->
                    scope.launch { snackBarHostState.showSnackbar(errorMessage) }
                }
            )
        }
    }

    LaunchedEffect(useBiometric) {
        triggerBiometric()
    }

    LaunchedEffect(state) {
        when (state) {
            is NetworkState.Success -> {
                navController.navigate("greeting") {
                    popUpTo("pin_code") { inclusive = true }
                }
            }
            is NetworkState.Error -> {
                snackBarHostState.showSnackbar(state.message)
                pinCodeInput = ""
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.enter_pin),
                    color = Color.Black,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                PinCodeDisplayFields(pinCode = pinCodeInput)
            }

            CustomPinKeyboard(
                showBiometricButton = useBiometric == true,
                onNumberClick = { digit ->
                    if (pinCodeInput.length < 4) {
                        pinCodeInput += digit
                        if (pinCodeInput.length == 4) {
                            val pinCodeRequest = PinCodeRequest(pinCodeInput)
                            viewModel.refresh(pinCodeRequest)
                        }
                    }
                },
                onDeleteClick = {
                    if (pinCodeInput.isNotEmpty()) {
                        pinCodeInput = pinCodeInput.dropLast(1)
                    }
                },
                onBiometricClick = {
                    triggerBiometric()
                }
            )
        }

        SkynetSnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 46.dp),
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
private fun showBiometricPrompt(
    activity: FragmentActivity,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val executor = ContextCompat.getMainExecutor(activity)

    val biometricPrompt = BiometricPrompt(
        activity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                if (errorCode != BiometricPrompt.ERROR_USER_CANCELED && errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    onError(errString.toString())
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onError("Биометрия не распознана. Попробуйте еще раз.")
            }
        }
    )

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(activity.getString(R.string.biometric_title))
        .setSubtitle(activity.getString(R.string.biometric_sub_title))
        .setNegativeButtonText(activity.getString(R.string.cancel))
        .build()

    biometricPrompt.authenticate(promptInfo)
}