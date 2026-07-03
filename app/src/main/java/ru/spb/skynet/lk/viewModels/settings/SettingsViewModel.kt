package ru.spb.skynet.lk.viewModels.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.spb.skynet.lk.data.models.response.settings.SettingsResponse
import ru.spb.skynet.lk.data.network.NetworkState
import ru.spb.skynet.lk.data.repository.settings.SettingsRepository
import ru.spb.skynet.lk.tools.SkyNetPreferences
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val settingsRepository: SettingsRepository,
                                            private val skyNetPreferences: SkyNetPreferences): ViewModel() {

    private val _settingsState = MutableStateFlow<NetworkState>(NetworkState.Idle)
    val settingsState: StateFlow<NetworkState> = _settingsState.asStateFlow()
    private val _settingsData = MutableStateFlow<SettingsResponse?>(null)
    val settingsData: StateFlow<SettingsResponse?> = _settingsData.asStateFlow()

    private val _useBiometric = MutableStateFlow(false)
    val useBiometric: StateFlow<Boolean?> = _useBiometric.asStateFlow()

    fun settings() {
        viewModelScope.launch {
            _settingsState.value = NetworkState.Loading
            try {
                val result = settingsRepository.settings()
                if (result.isSuccessful) {
                    val response = result.body()
                    _settingsState.value = NetworkState.Success(result)
                    _settingsData.value = response
                } else {
                    _settingsState.value = NetworkState.Error("Network error")
                }
            } catch (e: Exception) {
                _settingsState.value = NetworkState.Error(e.message ?: "Network error")
            }
        }
    }

    fun getBiometric() {
        viewModelScope.launch {
            _useBiometric.value = skyNetPreferences.biometricFlow.firstOrNull() ?: false
        }
    }

    fun toggleBiometric(isEnabled: Boolean) {
        _useBiometric.value = isEnabled

        viewModelScope.launch {
            try {
                skyNetPreferences.saveBiometric(isEnabled)
            } catch (e: Exception) {
                _useBiometric.value = !isEnabled
            }
        }
    }
    fun togglePools(isEnabled: Boolean) {
        _settingsData.update { currentSettings ->
            currentSettings?.copy(
                data = currentSettings.data?.copy(
                    blUSPoll = if (isEnabled) "yes" else "no"
                )
            )
        }
    }
}