package ru.spb.skynet.lk.data.repository.notifications

import retrofit2.Response
import ru.spb.skynet.lk.data.models.SkynetApi
import ru.spb.skynet.lk.data.models.response.notifications.NotificationResponse
import javax.inject.Inject

class NotificationsRepository @Inject constructor(private val skynetApi: SkynetApi) {

    suspend fun notifications(pageLimit: Int, offset: Int): Response<NotificationResponse> {
        return skynetApi.notifications(pageLimit, offset)
    }
}