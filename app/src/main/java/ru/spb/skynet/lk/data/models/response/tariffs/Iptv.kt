package ru.spb.skynet.lk.data.models.response.tariffs


import com.google.gson.annotations.SerializedName

data class Iptv(
    @SerializedName("channels_count")
    var channelsCount: Int?,
    @SerializedName("ID")
    var iD: Int?,
    @SerializedName("title")
    var title: String?
)