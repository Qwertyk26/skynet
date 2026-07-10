package ru.spb.skynet.lk.navigation

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object AppNavigator {

    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent: StateFlow<NavigationEvent?> = _navigationEvent.asStateFlow()

    fun navigateToLogin() {
        Log.d("AppNavigator", "Отправка события Auth через статический синглтон")
        _navigationEvent.value = NavigationEvent.Auth
    }

    fun clear() {
        Log.d("AppNavigator", "Очистка события навигации")
        _navigationEvent.value = null
    }
}

sealed class NavigationEvent {
    object Auth : NavigationEvent()
}