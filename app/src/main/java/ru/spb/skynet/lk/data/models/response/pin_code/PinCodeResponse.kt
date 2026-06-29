package ru.spb.skynet.lk.data.models.response.pin_code


import com.google.gson.annotations.SerializedName

data class PinCodeResponse(
    @SerializedName("result")
    var result: String?,
    @SerializedName("traceId")
    var traceId: String?,
    @SerializedName("resultCode")
    var resultCode: String?
)