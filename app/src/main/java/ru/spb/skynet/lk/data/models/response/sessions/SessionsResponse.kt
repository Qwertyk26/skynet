package ru.spb.skynet.lk.data.models.response.sessions


import com.google.gson.annotations.SerializedName

data class SessionsResponse(
    @SerializedName("data")
    var data: List<Data?>?,
    @SerializedName("result")
    var result: String?,
    @SerializedName("traceId")
    var traceId: String?
)