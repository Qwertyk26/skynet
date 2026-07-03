package ru.spb.skynet.lk.data.models.response.abonent


import com.google.gson.annotations.SerializedName

data class PayCardInfo(
    @SerializedName("has_autopay")
    var hasAutopay: Boolean?,
    @SerializedName("has_card")
    var hasCard: Boolean?
)