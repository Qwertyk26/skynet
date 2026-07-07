package ru.spb.skynet.lk.data.models.response.pos


import com.google.gson.annotations.SerializedName

data class PosResponse(
    @SerializedName("pos")
    var pos: List<Po?>?,
    @SerializedName("result")
    var result: String?,
    @SerializedName("traceId")
    var traceId: String?
)