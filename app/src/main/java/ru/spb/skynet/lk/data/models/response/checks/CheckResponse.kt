package ru.spb.skynet.lk.data.models.response.checks


import com.google.gson.annotations.SerializedName

data class CheckResponse(
    @SerializedName("data")
    var data: List<CheckData?>?,
    @SerializedName("page_count")
    var pageCount: Int?,
    @SerializedName("result")
    var result: String?,
    @SerializedName("traceId")
    var traceId: String?
)