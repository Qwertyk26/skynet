package ru.spb.skynet.lk.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import ru.spb.skynet.lk.components.auth.login.contract_number.AuthScreenContract
import ru.spb.skynet.lk.components.auth.login.phone_number.AuthScreenPhone
import ru.spb.skynet.lk.components.greeting.GreetingScreen
import ru.spb.skynet.lk.components.main.MainScreen
import ru.spb.skynet.lk.components.password.PasswordScreen
import ru.spb.skynet.lk.components.pin.PinCodeScreen
import ru.spb.skynet.lk.components.sms.SmsScreen
import ru.spb.skynet.lk.tools.SkyNetPreferences
import ru.spb.skynet.lk.viewModels.auth.AuthViewModel
import ru.spb.skynet.lk.viewModels.pin_code.PinCodeViewModel
import ru.spb.skynet.lk.viewModels.settings.SettingsViewModel

@Composable
fun AppNavigation(
    skyNetPreferences: SkyNetPreferences,
) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val pinCodeViewModel: PinCodeViewModel = hiltViewModel()
    val settingsViewModel: SettingsViewModel = hiltViewModel()

    var startDestination by remember { mutableStateOf<String?>(null) }
    val navigationEvent by AppNavigator.navigationEvent.collectAsStateWithLifecycle()

    LaunchedEffect(navigationEvent) {
        navigationEvent?.let { event ->
            Log.d("AppNavigation", "Получено событие на UI: $event")

            AppNavigator.clear() // Очищаем напрямую

            navController.navigate("auth") {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    LaunchedEffect(Unit) {
        startDestination = try {
            val session = skyNetPreferences.sessionFlow.firstOrNull()
            if (!session.isNullOrEmpty()) {
                "pin_code"
            } else {
                "auth"
            }
        } catch (e: Exception) {
            "auth"
        }
    }

    LaunchedEffect(Unit) {
        delay(400)
        if (startDestination == null) {
            startDestination = "auth"
        }
    }

    if (startDestination == null) {
        return
    }

    NavHost(
        navController = navController,
        startDestination = startDestination!!
    ) {
        composable("auth") {
            AuthScreenPhone(navController = navController, authViewModel)
        }
        composable("password") {
            PasswordScreen(navController = navController, authViewModel)
        }
        composable("auth_contract") {
            AuthScreenContract(navController = navController, authViewModel)
        }
        composable("pin_code") {
            PinCodeScreen(navController, pinCodeViewModel, settingsViewModel)
        }
        composable("greeting") {
            GreetingScreen(navController = navController)
        }
        composable("sms") {
            SmsScreen(navController = navController)
        }
        composable("main") {
            MainScreen()
        }
    }
}
