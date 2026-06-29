package ru.spb.skynet.lk.viewModels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.spb.skynet.lk.data.models.request.pin_code.PinCodeRequest
import ru.spb.skynet.lk.data.models.response.auth.AuthResponse
import ru.spb.skynet.lk.data.network.NetworkState
import ru.spb.skynet.lk.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository, sharedPreferences: SharedPreferences): ViewModel() {

    private val _loginState = MutableStateFlow<NetworkState>(NetworkState.Idle)
    val loginState: StateFlow<NetworkState> = _loginState.asStateFlow()

    fun login(phoneNumber: String, password: String? = null, verification: String? = null) {
        viewModelScope.launch {
            _loginState.value = NetworkState.Loading
            try {
                val result = authRepository.login(phoneNumber, password, verification = verification)
                if (result.isSuccessful) {
                    _loginState.value = NetworkState.Success(result)

                    Log.d(AuthViewModel::class.simpleName, "${result.headers()}")
                } else {
                    val errorResponse = Gson().fromJson(result.errorBody()?.string(), AuthResponse::class.java)
                    Log.d(AuthViewModel::class.simpleName, errorResponse.resultDesc ?: "")
                    _loginState.value = NetworkState.Error("Ошибка: ${errorResponse.resultDesc}")
                }
            } catch (e: Exception) {
                _loginState.value = NetworkState.Error(e.message ?: "Network error")
            }
        }
    }

    fun resetState() {
        _loginState.value = NetworkState.Idle
    }

    fun refresh(pinCodeRequest: PinCodeRequest) {
        viewModelScope.launch {
            try {
                _loginState.value = NetworkState.Loading
                val result = authRepository.refresh(pinCodeRequest)
                Log.d(AuthViewModel::class.simpleName, "${ result.body() }")
                if (result.body()?.result?.equals(ERROR) == true) {
                    _loginState.value = NetworkState.Error(result.body()?.resultCode ?: "")
                } else {
                    _loginState.value = NetworkState.Success(result)
                }
            } catch (e: Exception) {
                Log.d(AuthViewModel::class.simpleName, e.message ?: "")
                _loginState.value = NetworkState.Error(e.message ?: "Network error")
            }
        }
    }

    companion object {
        const val ERROR = "error"
    }
}