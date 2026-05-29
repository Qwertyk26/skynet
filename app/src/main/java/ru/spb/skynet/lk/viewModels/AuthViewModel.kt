package ru.spb.skynet.lk.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.spb.skynet.lk.data.network.NetworkState
import ru.spb.skynet.lk.data.repository.AuthRepository
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {

    private val _loginState = MutableStateFlow<NetworkState>(NetworkState.Idle)
    val loginState: StateFlow<NetworkState> = _loginState.asStateFlow()

    fun login(phoneNumber: String, password: String) {
        viewModelScope.launch {
            _loginState.value = NetworkState.Loading
            try {
                val result = authRepository.login(phoneNumber, password)
                if (result.isSuccessful) {
                    _loginState.value = NetworkState.Success(result)
                } else {
                    _loginState.value = NetworkState.Error("Ошибка: ${result.code()}")
                }
            } catch (e: Exception) {
                _loginState.value = NetworkState.Error(e.message ?: "Network error")
            }
        }
    }
}