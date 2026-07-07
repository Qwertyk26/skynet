package ru.spb.skynet.lk.data.models.response.payments_history


import com.google.gson.annotations.SerializedName

data class Payment(
    @SerializedName("balance")
    var balance: Int?,
    @SerializedName("cashFlow")
    var cashFlow: Int?,
    @SerializedName("dateTime")
    var dateTime: String?,
    @SerializedName("details")
    var details: String?
)