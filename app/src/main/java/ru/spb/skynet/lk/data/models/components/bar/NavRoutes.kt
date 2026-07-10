package ru.spb.skynet.lk.data.models.components.bar

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object News : NavRoutes("news")
    object Connect : NavRoutes("connect")
    object Settings : NavRoutes("settings")
    object Notifications : NavRoutes("notifications")
    object ChangePinCode: NavRoutes("change_pin")
    object ChangePassword: NavRoutes("change_password")
    object Sessions: NavRoutes("active_sessions")
    object PaymentsHistory: NavRoutes("payments_history")
    object BankCard: NavRoutes("bank_card")
    object Orders: NavRoutes("orders")
    object Checks: NavRoutes("checks")
    object CashReceipt: NavRoutes("cash_receipt/{checkId}")
    object Chat: NavRoutes("chat")
}