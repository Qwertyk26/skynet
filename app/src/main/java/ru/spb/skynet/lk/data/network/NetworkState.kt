package ru.spb.skynet.lk.data.network

sealed class NetworkState {

    object Idle : NetworkState()
    object Loading : NetworkState()
    data class Success(val data: Any) : NetworkState()
    data class Error(val message: String) : NetworkState()
}