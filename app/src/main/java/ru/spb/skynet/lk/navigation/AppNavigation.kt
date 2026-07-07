package ru.spb.skynet.lk.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
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
import ru.spb.skynet.lk.components.payments_history.PaymentsHistoryScreen
import ru.spb.skynet.lk.components.pin.PinCodeScreen
import ru.spb.skynet.lk.components.sms.SmsScreen
import ru.spb.skynet.lk.tools.SkyNetPreferences
import ru.spb.skynet.lk.viewModels.auth.AuthViewModel
import ru.spb.skynet.lk.viewModels.settings.SettingsViewModel

@Composable
fun AppNavigation(
    skyNetPreferences: SkyNetPreferences
) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val settingsViewModel: SettingsViewModel = hiltViewModel()

    // Используем простое состояние строки вместо прямого collectAsState
    var startDestination by remember { mutableStateOf<String?>(null) }

    //LaunchedEffect сработает ОДИН РАЗ при старте экрана
    LaunchedEffect(Unit) {
        try {
            Log.d("AppNavigation", "Запуск чтения sessionFlow...")

            // На выбор запускаем получение данных или прерываем через 300мс, если поток завис
            val session = skyNetPreferences.sessionFlow.firstOrNull()

            Log.d("AppNavigation", "Получено значение сессии: '$session'")

            if (!session.isNullOrEmpty()) {
                startDestination = "pin_code"
            } else {
                startDestination = "auth"
            }
        } catch (e: Exception) {
            Log.e("AppNavigation", "Ошибка загрузки сессии, принудительно на auth", e)
            startDestination = "auth"
        }
    }

    // Предохранитель: Если за 400 миллисекунд поток ничего не вернул, принудительно открываем auth
    LaunchedEffect(Unit) {
        delay(400)
        if (startDestination == null) {
            Log.w("AppNavigation", "ВНИМАНИЕ: Поток завис! Сработал предохранитель таймаута.")
            startDestination = "auth"
        }
    }

    // Пока дестинация не определена, показываем индикатор загрузки, чтобы не было просто белого экрана
    if (startDestination == null) {
        return
    }

    Log.d("AppNavigation", "Монтирование NavHost на экран: $startDestination")

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
            PinCodeScreen(navController, authViewModel, settingsViewModel)
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
