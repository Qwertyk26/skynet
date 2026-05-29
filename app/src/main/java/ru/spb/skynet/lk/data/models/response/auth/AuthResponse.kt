package ru.spb.skynet.lk.data.models.response.auth


import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("result")
    var result: String?,
    @SerializedName("traceId")
    var traceId: String?
)