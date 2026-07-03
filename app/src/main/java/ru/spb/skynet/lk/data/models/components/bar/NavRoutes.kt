package ru.spb.skynet.lk.data.models.components.bar

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object Contacts : NavRoutes("contacts")
    object Chat : NavRoutes("chat")
    object Settings : NavRoutes("settings")
    object Notifications : NavRoutes("notifications")
    object ChangePinCode: NavRoutes("change_pin")
    object ChangePassword: NavRoutes("change_password")
    object Sessions: NavRoutes("active_sessions")
}