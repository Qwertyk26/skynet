package ru.spb.skynet.lk.data.models.response.abonent


import com.google.gson.annotations.SerializedName

data class AbonentResponse(
    @SerializedName("info")
    var info: Info?,
    @SerializedName("result")
    var result: String?,
    @SerializedName("traceId")
    var traceId: String?
)