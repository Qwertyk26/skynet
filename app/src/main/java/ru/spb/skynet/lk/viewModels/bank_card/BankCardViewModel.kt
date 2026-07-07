package ru.spb.skynet.lk.viewModels.bank_card

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.spb.skynet.lk.data.models.response.tokens.TokensResponse
import ru.spb.skynet.lk.data.network.NetworkState
import ru.spb.skynet.lk.data.repository.bank_card.BankCardRepository
import javax.inject.Inject

@HiltViewModel
class BankCardViewModel  @Inject constructor(private val bankCardRepository: BankCardRepository): ViewModel() {

    private val _networkState = MutableStateFlow<NetworkState>(NetworkState.Idle)
    val networkState: StateFlow<NetworkState> = _networkState.asStateFlow()

    private val _tokens = MutableStateFlow<TokensResponse?>(null)
    val tokens: StateFlow<TokensResponse?> = _tokens.asStateFlow()

    fun card() {
        viewModelScope.launch {
            _networkState.value = NetworkState.Loading
            try {
                val result = bankCardRepository.backCard()
                if (result.isSuccessful) {
                    _tokens.value = result.body()
                    _networkState.value = NetworkState.Success(tokens)
                } else {
                    _networkState.value = NetworkState.Error("Произошла ошибка")
                }
            } catch (e: Exception) {
                Log.e(BankCardViewModel::class.simpleName, e.message ?: "")
                _networkState.value = NetworkState.Error(e.message ?: "Network error")
            }
        }
    }
}