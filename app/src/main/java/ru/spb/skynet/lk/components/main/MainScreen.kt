package ru.spb.skynet.lk.components.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.spb.skynet.lk.components.bank_card.BankCardScreen
import ru.spb.skynet.lk.components.chat.ChatScreen
import ru.spb.skynet.lk.components.contacts.Contacts
import ru.spb.skynet.lk.components.home.HomeScreen
import ru.spb.skynet.lk.components.main.bar.BottomNavigationBar
import ru.spb.skynet.lk.components.notifications.NotificationsScreen
import ru.spb.skynet.lk.components.password.change.ChangePassword
import ru.spb.skynet.lk.components.payments_history.PaymentsHistoryScreen
import ru.spb.skynet.lk.components.pin.change.ChangePinCodeScreen
import ru.spb.skynet.lk.components.sessions.SessionsScreen
import ru.spb.skynet.lk.components.settings.SettingsScreen
import ru.spb.skynet.lk.data.models.components.bar.NavRoutes
import ru.spb.skynet.lk.viewModels.bank_card.BankCardViewModel
import ru.spb.skynet.lk.viewModels.home.HomeViewModel
import ru.spb.skynet.lk.viewModels.notifications.NotificationsViewModel
import ru.spb.skynet.lk.viewModels.payments_history.PaymentsHistoryViewModel
import ru.spb.skynet.lk.viewModels.sessions.SessionsViewModel
import ru.spb.skynet.lk.viewModels.settings.SettingsViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val sessionsViewModel: SessionsViewModel = hiltViewModel()
    val notificationsViewModel: NotificationsViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val paymentsViewModel: PaymentsHistoryViewModel = hiltViewModel()
    val bankCardViewModel: BankCardViewModel = hiltViewModel()

    Column {
        NavHost(navController, startDestination = NavRoutes.Home.route,
            modifier = Modifier.weight(1f),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            composable(NavRoutes.Home.route) { HomeScreen(navController, homeViewModel) }
            composable(NavRoutes.Contacts.route) { Contacts()  }
            composable(NavRoutes.Chat.route) { ChatScreen(navController) }
            composable(NavRoutes.Settings.route) { SettingsScreen(navController, settingsViewModel) }
            composable(NavRoutes.Notifications.route) { NotificationsScreen(navController = navController, notificationsViewModel) }
            composable(NavRoutes.ChangePinCode.route) { ChangePinCodeScreen(navController = navController) }
            composable(NavRoutes.ChangePassword.route) { ChangePassword(navController = navController) }
            composable(NavRoutes.Sessions.route) { SessionsScreen(navController = navController, sessionsViewModel) }
            composable(NavRoutes.PaymentsHistory.route) { PaymentsHistoryScreen(navController, paymentsViewModel) }
            composable(NavRoutes.BanckCard.route) { BankCardScreen(navController, bankCardViewModel) }
        }
        HorizontalDivider(
            thickness = 0.5.dp,
            color = Color.LightGray.copy(alpha = 0.5f)
        )
        BottomNavigationBar(navController = navController)
    }
}