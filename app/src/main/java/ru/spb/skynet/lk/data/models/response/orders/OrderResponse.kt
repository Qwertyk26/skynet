package ru.spb.skynet.lk.data.models.response.orders


import com.google.gson.annotations.SerializedName

data class OrderResponse(
    @SerializedName("orders")
    var orders: List<Order?>?,
    @SerializedName("result")
    var result: String?,
    @SerializedName("statuses")
    val statuses: Map<String, String>,
    @SerializedName("traceId")
    var traceId: String?
)