package ru.spb.skynet.lk.viewModels.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.spb.skynet.lk.data.models.response.notifications.NotificationItem
import ru.spb.skynet.lk.data.network.NetworkState
import ru.spb.skynet.lk.data.repository.notifications.NotificationsRepository
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(private val notificationsRepository: NotificationsRepository): ViewModel() {

    private val _networkState = MutableStateFlow<NetworkState>(NetworkState.Idle)
    val networkState: StateFlow<NetworkState> = _networkState.asStateFlow()

    private val _notificationsList = MutableStateFlow<List<NotificationItem>>(emptyList())
    val notificationsList: StateFlow<List<NotificationItem>> = _notificationsList.asStateFlow()

    private val pageSize = 10
    private var currentOffset = 0
    private var isLastPage = false
    private var isPaginationLoading = false

    fun notifications() {
        if (isPaginationLoading || isLastPage) return

        isPaginationLoading = true

        // Если это самая первая страница — ставим общее состояние Loading для показа большой крутилки
        if (currentOffset == 0) {
            _networkState.value = NetworkState.Loading
        }

        viewModelScope.launch {
            try {
                val result = notificationsRepository.notifications(pageSize, currentOffset)

                if (result.isSuccessful && result.body() != null) {
                    val newItems = result.body()?.data?.items?.filterNotNull() ?: emptyList()

                    if (newItems.isEmpty()) {
                        isLastPage =
                            true // Если сервер вернул пустой список, значит страницы кончились
                    } else {
                        // КРИТИЧЕСКИ ВАЖНО: Добавляем новые элементы к старым, а не перезаписываем!
                        _notificationsList.value = _notificationsList.value + newItems
                        currentOffset += pageSize // Сдвигаем offset для следующего запроса
                    }

                    _networkState.value = NetworkState.Success(result)
                } else {
                    _networkState.value = NetworkState.Error("Network error")
                }
            } catch (e: Exception) {
                _networkState.value = NetworkState.Error(e.message ?: "Network error")
            } finally {
                isPaginationLoading = false // Снимаем блокировку в любом случае
            }
        }
    }

    fun refreshNotifications() {
        currentOffset = 0
        isLastPage = false
        _notificationsList.value = emptyList()
        notifications()
    }
}