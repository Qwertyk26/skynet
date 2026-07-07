package ru.spb.skynet.lk.data.models.response.tariffs


import com.google.gson.annotations.SerializedName

data class TariffData(
    @SerializedName("behind_nat")
    var behindNat: Boolean?,
    @SerializedName("bl_date_add")
    var blDateAdd: String?,
    @SerializedName("bl_s1_tarif_speed_in")
    var blS1TarifSpeedIn: String?,
    @SerializedName("bl_t_act_on")
    var blTActOn: String?,
    @SerializedName("bl_t_id_after")
    var blTIdAfter: Int?,
    @SerializedName("bl_t_type")
    var blTType: String?,
    @SerializedName("can_get_avans")
    var canGetAvans: Boolean?,
    @SerializedName("can_pay")
    var canPay: Boolean?,
    @SerializedName("doplata")
    var doplata: Double?,
    @SerializedName("ID")
    var iD: Int?,
    @SerializedName("ip_price")
    var ipPrice: String?,
    @SerializedName("new_payday")
    var newPayday: String?,
    @SerializedName("payday")
    var payday: String?,
    @SerializedName("payment_now")
    var paymentNow: Double?,
    @SerializedName("price")
    var price: Int?,
    @SerializedName("price_add")
    var priceAdd: Int?,
    @SerializedName("speed")
    var speed: Int?,
    @SerializedName("tarif_connect")
    var tarifConnect: String?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("works_price")
    var worksPrice: Int?
)