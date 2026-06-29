package ru.spb.skynet.lk.data.models.response.auth


import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("result")
    var result: String? = null,
    @SerializedName("resultCode")
    var resultCode: String? = null,
    @SerializedName("resultData")
    var resultData: Any? = null,
    @SerializedName("resultDesc")
    var resultDesc: String? = null,
    @SerializedName("traceId")
    var traceId: String? = null
)