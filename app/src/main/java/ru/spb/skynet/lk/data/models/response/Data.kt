package ru.spb.skynet.lk.data.models.response


import com.google.gson.annotations.SerializedName
import ru.spb.skynet.lk.data.models.response.notifications.NotificationItem

data class Data(
    @SerializedName("count")
    var count: Int?,
    @SerializedName("items")
    var items: List<NotificationItem?>?
)