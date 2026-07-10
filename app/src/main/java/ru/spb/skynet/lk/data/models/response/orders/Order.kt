package ru.spb.skynet.lk.data.models.response.orders


import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("can_cancel")
    var canCancel: Boolean?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("money")
    var money: Int?,
    @SerializedName("provider")
    var provider: String?,
    @SerializedName("queue_status")
    var queueStatus: Int?,
    @SerializedName("timestamp_create")
    var timestampCreate: Int?,
    @SerializedName("timestamp_todo")
    var timestampTodo: Int?,
    @SerializedName("timestamp_update")
    var timestampUpdate: Int?,
    @SerializedName("token_id")
    var tokenId: Int?
)