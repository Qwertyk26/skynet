package ru.spb.skynet.lk.data.models.response.settings


import com.google.gson.annotations.SerializedName

data class SettingsResponse(
    @SerializedName("data")
    var data: Data?,
    @SerializedName("result")
    var result: String?,
    @SerializedName("traceId")
    var traceId: String?
)