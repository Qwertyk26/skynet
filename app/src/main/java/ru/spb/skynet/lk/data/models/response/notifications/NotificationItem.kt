package ru.spb.skynet.lk.data.models.response.notifications

import com.google.gson.annotations.SerializedName

data class NotificationItem(
    @SerializedName("final_channel")
    var finalChannel: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("timestamp")
    var timestamp: Int?,
    @SerializedName("timestamp_delivery")
    var timestampDelivery: Int?
)