package ru.spb.skynet.lk.viewModels.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.spb.skynet.lk.data.models.response.abonent.AbonentResponse
import ru.spb.skynet.lk.data.network.NetworkState
import ru.spb.skynet.lk.data.repository.home.HomeRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository): ViewModel() {

    private val _networkState = MutableStateFlow<NetworkState>(NetworkState.Idle)
    val networkState: StateFlow<NetworkState> = _networkState.asStateFlow()

    private val _abonent = MutableStateFlow<AbonentResponse?>(null)
    val abonent: StateFlow<AbonentResponse?> = _abonent.asStateFlow()

    fun abonent() {
        viewModelScope.launch {
            _networkState.value = NetworkState.Loading
            try {
                val result = homeRepository.abonent()
                if (result.isSuccessful) {
                    _abonent.value = result.body()
                    _networkState.value = NetworkState.Success(result)
                } else {

                }
            } catch (e: Exception) {
                Log.d(HomeViewModel::class.simpleName, e.message ?: "")
            }
        }
    }
}