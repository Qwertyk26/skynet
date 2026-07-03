package ru.spb.skynet.lk.data.models.response.notifications

import com.google.gson.annotations.SerializedName
import ru.spb.skynet.lk.data.models.response.Data

data class NotificationResponse(
    @SerializedName("data")
    var data: Data?,
    @SerializedName("result")
    var result: String?,
    @SerializedName("traceId")
    var traceId: String?
)