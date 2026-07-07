package ru.spb.skynet.lk.data.models.response.payments_history


import com.google.gson.annotations.SerializedName

data class PaymentsHistoryResponse(
    @SerializedName("payments")
    var payments: List<Payment?>?,
    @SerializedName("result")
    var result: String?,
    @SerializedName("traceId")
    var traceId: String?
)