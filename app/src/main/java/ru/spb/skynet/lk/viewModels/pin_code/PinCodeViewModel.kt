package ru.spb.skynet.lk.viewModels.pin_code

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.spb.skynet.lk.data.models.request.pin_code.PinCodeRequest
import ru.spb.skynet.lk.data.network.NetworkState
import ru.spb.skynet.lk.data.repository.pin_code.PinCodeRepository
import ru.spb.skynet.lk.viewModels.auth.AuthViewModel
import ru.spb.skynet.lk.viewModels.auth.AuthViewModel.Companion.ERROR
import javax.inject.Inject

@HiltViewModel
class PinCodeViewModel @Inject constructor(private val pinCodeRepository: PinCodeRepository): ViewModel() {

    private val _networkState = MutableStateFlow<NetworkState>(NetworkState.Idle)
    val networkState: StateFlow<NetworkState> = _networkState.asStateFlow()

    fun refresh(pinCodeRequest: PinCodeRequest) {
        viewModelScope.launch {
            _networkState.value = NetworkState.Loading
            try {
                val result = pinCodeRepository.refresh(pinCodeRequest)
                if (result.body()?.result?.equals(ERROR) == true) {
                    _networkState.value = NetworkState.Error(result.body()?.resultCode ?: "")
                } else {
                    _networkState.value = NetworkState.Success(result)
                }
            } catch (e: Exception) {
                Log.d(AuthViewModel::class.simpleName, e.message ?: "")
                _networkState.value = NetworkState.Error(e.message ?: "Network error")
            }
        }
    }
}