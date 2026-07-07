package ru.spb.skynet.lk.viewModels.payments_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.spb.skynet.lk.data.models.response.payments_history.PaymentsHistoryResponse
import ru.spb.skynet.lk.data.network.NetworkState
import ru.spb.skynet.lk.data.repository.payments_history.PaymentsHistoryRepository
import javax.inject.Inject

@HiltViewModel
class PaymentsHistoryViewModel @Inject constructor(private val paymentsHistoryRepository: PaymentsHistoryRepository): ViewModel() {

    private val _networkState = MutableStateFlow<NetworkState>(NetworkState.Idle)
    val networkState: StateFlow<NetworkState> = _networkState.asStateFlow()

    private val _historyData = MutableStateFlow<PaymentsHistoryResponse?>(null)
    val historyData: StateFlow<PaymentsHistoryResponse?> = _historyData.asStateFlow()

    fun payments() {
        _networkState.value = NetworkState.Loading
        viewModelScope.launch {
            try {
                val result = paymentsHistoryRepository.payments()
                if (result.isSuccessful) {
                    _networkState.value = NetworkState.Loading
                    _historyData.value = result.body()
                    _networkState.value = NetworkState.Success(historyData)
                }
            } catch (e: Exception) {
                _networkState.value = NetworkState.Error(e.message ?: "Network error")
            }
        }
    }
}