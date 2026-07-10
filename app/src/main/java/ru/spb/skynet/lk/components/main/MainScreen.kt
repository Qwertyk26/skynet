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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.spb.skynet.lk.components.balance.bank_card.BankCardScreen
import ru.spb.skynet.lk.components.balance.checks.CashReceiptScreen
import ru.spb.skynet.lk.components.balance.checks.ChecksScreen
import ru.spb.skynet.lk.components.balance.orders.OperationsScreen
import ru.spb.skynet.lk.components.balance.payments_history.PaymentsHistoryScreen
import ru.spb.skynet.lk.components.chat.ChatScreen
import ru.spb.skynet.lk.components.main.bar.BottomNavigationBar
import ru.spb.skynet.lk.components.main.connect.ConnectScreen
import ru.spb.skynet.lk.components.main.home.HomeScreen
import ru.spb.skynet.lk.components.main.news.NewsScreen
import ru.spb.skynet.lk.components.main.settings.SettingsScreen
import ru.spb.skynet.lk.components.main.settings.sessions.SessionsScreen
import ru.spb.skynet.lk.components.notifications.NotificationsScreen
import ru.spb.skynet.lk.components.password.change.ChangePassword
import ru.spb.skynet.lk.components.pin.change.ChangePinCodeScreen
import ru.spb.skynet.lk.data.models.components.bar.NavRoutes
import ru.spb.skynet.lk.viewModels.balance.BalanceViewModel
import ru.spb.skynet.lk.viewModels.home.HomeViewModel
import ru.spb.skynet.lk.viewModels.notifications.NotificationsViewModel
import ru.spb.skynet.lk.viewModels.sessions.SessionsViewModel
import ru.spb.skynet.lk.viewModels.settings.SettingsViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val sessionsViewModel: SessionsViewModel = hiltViewModel()
    val notificationsViewModel: NotificationsViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val balanceViewModel: BalanceViewModel = hiltViewModel()

    Column {
        NavHost(navController, startDestination = NavRoutes.Home.route,
            modifier = Modifier.weight(1f),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            composable(NavRoutes.Home.route) { HomeScreen(navController, homeViewModel) }
            composable(NavRoutes.News.route) { NewsScreen() }
            composable(NavRoutes.Connect.route) { ConnectScreen(navController, homeViewModel) }
            composable(NavRoutes.Settings.route) { SettingsScreen(navController, settingsViewModel) }
            composable(NavRoutes.Notifications.route) { NotificationsScreen(navController = navController, notificationsViewModel) }
            composable(NavRoutes.ChangePinCode.route) { ChangePinCodeScreen(navController = navController) }
            composable(NavRoutes.ChangePassword.route) { ChangePassword(navController = navController) }
            composable(NavRoutes.Sessions.route) { SessionsScreen(navController = navController, sessionsViewModel) }
            composable(NavRoutes.PaymentsHistory.route) { PaymentsHistoryScreen(navController, balanceViewModel) }
            composable(NavRoutes.BankCard.route) { BankCardScreen(navController, balanceViewModel) }
            composable(NavRoutes.Orders.route ) { OperationsScreen(navController, balanceViewModel) }
            composable(NavRoutes.Checks.route) { ChecksScreen(navController, balanceViewModel) }
            composable(NavRoutes.CashReceipt.route,  arguments = listOf(navArgument("checkId") { type = NavType.StringType })) {
                CashReceiptScreen(
                    navController = navController,
                    viewModel = hiltViewModel()
                )
            }
            composable(NavRoutes.Chat.route) { ChatScreen(navController) }
        }
        HorizontalDivider(
            thickness = 0.5.dp,
            color = Color.LightGray.copy(alpha = 0.5f)
        )
        BottomNavigationBar(navController = navController)
    }
}