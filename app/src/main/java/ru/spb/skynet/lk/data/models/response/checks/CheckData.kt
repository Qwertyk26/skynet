package ru.spb.skynet.lk.data.models.response.checks


import com.google.gson.annotations.SerializedName

data class CheckData(
    @SerializedName("ID")
    var id: String?,
    @SerializedName("money")
    var money: String?,
    @SerializedName("time")
    var time: String?
)