package ru.spb.skynet.lk.viewModels.sessions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.spb.skynet.lk.data.models.response.sessions.SessionsResponse
import ru.spb.skynet.lk.data.network.NetworkState
import ru.spb.skynet.lk.data.repository.sessions.SessionsRepository
import javax.inject.Inject

@HiltViewModel
class SessionsViewModel @Inject constructor(private val sessionsRepository: SessionsRepository): ViewModel() {

    private val _sessionsState = MutableStateFlow<NetworkState>(NetworkState.Idle)
    val sessionsState: StateFlow<NetworkState> = _sessionsState.asStateFlow()

    private val _sessionsData = MutableStateFlow<SessionsResponse?>(null)
    val sessionsData: StateFlow<SessionsResponse?> = _sessionsData.asStateFlow()

    fun sessions() {
        viewModelScope.launch {
            _sessionsState.value = NetworkState.Loading
            try {
                val result = sessionsRepository.sessions()
                _sessionsData.value = result.body()
                _sessionsState.value = NetworkState.Success(sessionsData)
            } catch (e: Exception) {

            }
        }
    }
}