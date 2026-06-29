package ru.spb.skynet.lk.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.spb.skynet.lk.components.auth.login.contract_number.AuthScreenContract
import ru.spb.skynet.lk.components.auth.login.phone_number.AuthScreenPhone
import ru.spb.skynet.lk.components.greeting.GreetingScreen
import ru.spb.skynet.lk.components.password.PasswordScreen
import ru.spb.skynet.lk.components.pin.PinCodeScreen
import ru.spb.skynet.lk.components.sms.SmsScreen
import ru.spb.skynet.lk.viewModels.AuthViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = "auth"
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
            PinCodeScreen(navController = navController, authViewModel)
        }

        composable ("greeting") {
            GreetingScreen(navController = navController)
        }
        composable("sms") {
            SmsScreen(navController = navController)
        }
    }
}