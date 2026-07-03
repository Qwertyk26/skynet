package ru.spb.skynet.lk.data.models.response.settings


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("bl_u_s_avans")
    var blUSAvans: String?,
    @SerializedName("bl_u_s_notify")
    var blUSNotify: String?,
    @SerializedName("bl_u_s_poll")
    var blUSPoll: String?,
    @SerializedName("has_other_devices")
    var hasOtherDevices: String?,
    @SerializedName("is_pin_enabled")
    var isPinEnabled: String?,
    @SerializedName("user_id")
    var userId: Int?
)