package ru.spb.skynet.lk.data.models.response.tokens


import com.google.gson.annotations.SerializedName

data class TokenData(
    @SerializedName("autopay")
    var autopay: Boolean?,
    @SerializedName("bank")
    var bank: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("pan")
    var pan: String?,
    @SerializedName("payment_provider")
    var paymentProvider: String?
)