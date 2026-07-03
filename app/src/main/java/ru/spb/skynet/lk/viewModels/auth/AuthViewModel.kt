package ru.spb.skynet.lk.viewModels.auth

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
import ru.spb.skynet.lk.data.models.response.abonent.AbonentResponse
import ru.spb.skynet.lk.data.models.response.auth.AuthResponse
import ru.spb.skynet.lk.data.network.NetworkState
import ru.spb.skynet.lk.data.repository.auth.AuthRepository
import ru.spb.skynet.lk.tools.SkyNetPreferences
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository,
                                        private val skyNetPreferences: SkyNetPreferences
): ViewModel() {

    private val _loginState = MutableStateFlow<NetworkState>(NetworkState.Idle)
    val loginState: StateFlow<NetworkState> = _loginState.asStateFlow()

    private val _abonentState = MutableStateFlow<AbonentResponse?>(null)
    val abonentState: StateFlow<AbonentResponse?> = _abonentState.asStateFlow()

    fun login(phoneNumber: String, password: String? = null, verification: String? = null) {
        viewModelScope.launch {
            // 1. Включаем общую крутилку прогресс-бара на экране
            _loginState.value = NetworkState.Loading

            try {
                // ШАГ А: Запуск первого запроса авторизации
                val loginResult = authRepository.login(phoneNumber, password, verification = verification)

                if (loginResult.isSuccessful) {
                    // Сохраняем сессию/куки из заголовков успешного входа
                    val cookie = loginResult.headers()["Set-cookie"] ?: ""
                    skyNetPreferences.saveSession(cookie)

                    Log.d(AuthViewModel::class.simpleName, "Логин успешен. Запуск запроса abonent...")

                    // ШАГ Б: Запуск второго запроса (вызывается строго ПОСЛЕ логина)
                    val abonentResult = authRepository.abonent()

                    if (abonentResult.isSuccessful) {
                        // ОБА запроса выполнены успешно. Переводим экран в финальный NetworkState.Success.
                        // Передаем abonentResult (или loginResult, в зависимости от того, какие данные нужны экрану дальше)
                        _abonentState.value = abonentResult.body()
                        _loginState.value = NetworkState.Success(abonentResult)
                        Log.d(AuthViewModel::class.simpleName, "Данные абонента успешно получены!")
                    } else {
                        // Ошибка на шаге запроса абонента
                        val errorResponse = Gson().fromJson(abonentResult.errorBody()?.string(), AuthResponse::class.java)
                        _loginState.value = NetworkState.Error("Ошибка получения данных абонента: ${errorResponse.resultDesc}")
                    }

                } else {
                    // Ошибка на шаге самого логина
                    val errorResponse = Gson().fromJson(loginResult.errorBody()?.string(), AuthResponse::class.java)
                    Log.d(AuthViewModel::class.simpleName, errorResponse.resultDesc ?: "")
                    _loginState.value = NetworkState.Error("Ошибка авторизации: ${errorResponse.resultDesc}")
                }

            } catch (e: Exception) {
                // Перехват любых сетевых таймаутов или сбоев парсинга обоих запросов
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