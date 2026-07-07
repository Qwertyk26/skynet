package ru.spb.skynet.lk.data.models.response.tariffs


import com.google.gson.annotations.SerializedName

data class TariffsResponse(
    @SerializedName("cur_tarif")
    var curTariff: CurTariff?,
    @SerializedName("result")
    var result: String?,
    @SerializedName("tarifs")
    var tariffs: List<Tariff>?,
    @SerializedName("traceId")
    var traceId: String?
)