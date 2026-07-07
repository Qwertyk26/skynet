package ru.spb.skynet.lk.data.models.response.tariffs


import com.google.gson.annotations.SerializedName

data class Tariff(
    @SerializedName("behind_nat")
    var behindNat: Boolean?,
    @SerializedName("free_options")
    var freeOptions: List<String>?,
    @SerializedName("iptv")
    var iptv: Iptv?,
    @SerializedName("link")
    var link: String?,
    @SerializedName("packet_tv")
    var packetTv: Boolean?,
    @SerializedName("price_add")
    var priceAdd: Int?,
    @SerializedName("speed")
    var speed: Int?,
    @SerializedName("tarifs")
    var tarifs: List<TariffData>?,
    @SerializedName("title")
    var title: String?
)