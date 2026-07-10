package ru.spb.skynet.lk.viewModels.balance

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.spb.skynet.lk.data.models.response.checks.CheckResponse
import ru.spb.skynet.lk.data.models.response.orders.OrderResponse
import ru.spb.skynet.lk.data.models.response.payments_history.PaymentsHistoryResponse
import ru.spb.skynet.lk.data.models.response.tokens.TokensResponse
import ru.spb.skynet.lk.data.network.NetworkState
import ru.spb.skynet.lk.data.repository.balance.BalanceRepository
import ru.spb.skynet.lk.tools.SkyNetPreferences
import javax.inject.Inject

@HiltViewModel
class BalanceViewModel @Inject constructor(private val balanceRepository: BalanceRepository,
                                           private val skyNetPreferences: SkyNetPreferences,
                                           savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _networkState = MutableStateFlow<NetworkState>(NetworkState.Idle)
    val networkState: StateFlow<NetworkState> = _networkState.asStateFlow()

    private val _tokens = MutableStateFlow<TokensResponse?>(null)
    val tokens: StateFlow<TokensResponse?> = _tokens.asStateFlow()

    private val _historyData = MutableStateFlow<PaymentsHistoryResponse?>(null)
    val historyData: StateFlow<PaymentsHistoryResponse?> = _historyData.asStateFlow()

    private val _ordersData = MutableStateFlow<OrderResponse?>(null)
    val ordersData: StateFlow<OrderResponse?> = _ordersData.asStateFlow()

    private val _checksData = MutableStateFlow<CheckResponse?>(null)
    val checksData: StateFlow<CheckResponse?> = _checksData.asStateFlow()

    val checkId: String = savedStateHandle["checkId"] ?: ""

    val receiptUrl = "https://lk.sknt.ru/balance/checks/"

    val sessionToken: StateFlow<String?> = skyNetPreferences.sessionFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun card() {
        _networkState.value = NetworkState.Loading
        viewModelScope.launch {
            try {
                val result = balanceRepository.backCard()
                if (result.isSuccessful) {
                    _tokens.value = result.body()
                    _networkState.value = NetworkState.Success(tokens)
                } else {
                    _networkState.value = NetworkState.Error("Произошла ошибка")
                }
            } catch (e: Exception) {
                Log.e(BalanceViewModel::class.simpleName, e.message ?: "")
                _networkState.value = NetworkState.Error(e.message ?: "Network error")
            }
        }
    }

    fun payments() {
        _networkState.value = NetworkState.Loading
        viewModelScope.launch {
            try {
                val result = balanceRepository.payments()
                if (result.isSuccessful) {
                    _historyData.value = result.body()
                    _networkState.value = NetworkState.Success(historyData)
                }
            } catch (e: Exception) {
                _networkState.value = NetworkState.Error(e.message ?: "Network error")
            }
        }
    }

    fun orders() {
        _networkState.value = NetworkState.Loading
        viewModelScope.launch {
            try {
                val result = balanceRepository.orders()
                if (result.isSuccessful) {
                    _ordersData.value = result.body()
                    _networkState.value = NetworkState.Success(historyData)
                }
            } catch (e: Exception) {
                _networkState.value = NetworkState.Error(e.message ?: "Network error")
            }
        }
    }

    fun checks() {
        _networkState.value = NetworkState.Loading
        viewModelScope.launch {
            try {
                val result = balanceRepository.checks()
                if (result.isSuccessful) {
                    _checksData.value = result.body()
                    _networkState.value = NetworkState.Success(historyData)
                }
            } catch (e: Exception) {
                _networkState.value = NetworkState.Error(e.message ?: "Network error")
            }
        }
    }
}