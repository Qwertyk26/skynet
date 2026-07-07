package ru.spb.skynet.lk.data.models.response.tariffs


import com.google.gson.annotations.SerializedName

data class CurTariff(
    @SerializedName("bl_date_add")
    var blDateAdd: String?,
    @SerializedName("bl_pipe")
    var blPipe: String?,
    @SerializedName("bl_s1_additional_works")
    var blS1AdditionalWorks: String?,
    @SerializedName("bl_s1_connection")
    var blS1Connection: String?,
    @SerializedName("bl_s1_ip_price")
    var blS1IpPrice: String?,
    @SerializedName("bl_s1_price_min")
    var blS1PriceMin: String?,
    @SerializedName("bl_s1_price_step")
    var blS1PriceStep: String?,
    @SerializedName("bl_s1_tarif_speed_in")
    var blS1TarifSpeedIn: String?,
    @SerializedName("bl_s1_tarif_speed_out")
    var blS1TarifSpeedOut: String?,
    @SerializedName("bl_s1t_1day_price")
    var blS1t1dayPrice: String?,
    @SerializedName("bl_t_eday")
    var blTEday: String?,
    @SerializedName("bl_t_id_after")
    var blTIdAfter: String?,
    @SerializedName("bl_t_loc")
    var blTLoc: String?,
    @SerializedName("bl_t_nat")
    var blTNat: String?,
    @SerializedName("bl_t_otkl")
    var blTOtkl: String?,
    @SerializedName("bl_t_pminsum")
    var blTPminsum: String?,
    @SerializedName("bl_t_pon")
    var blTPon: String?,
    @SerializedName("bl_t_pon_profile")
    var blTPonProfile: Any?,
    @SerializedName("bl_t_type")
    var blTType: String?,
    @SerializedName("bl_t_ur")
    var blTUr: String?,
    @SerializedName("description_short")
    var descriptionShort: String?,
    @SerializedName("ID")
    var iD: Int?,
    @SerializedName("import_uid")
    var importUid: Any?,
    @SerializedName("inc_mb")
    var incMb: String?,
    @SerializedName("price_add")
    var priceAdd: String?,
    @SerializedName("price_mb")
    var priceMb: String?,
    @SerializedName("price_month")
    var priceMonth: String?,
    @SerializedName("price_on")
    var priceOn: String?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("user_id")
    var userId: String?
)