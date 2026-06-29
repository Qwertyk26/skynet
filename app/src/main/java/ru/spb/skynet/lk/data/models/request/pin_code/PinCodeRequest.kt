package ru.spb.skynet.lk.data.models.request.pin_code

import com.google.gson.annotations.SerializedName

data class PinCodeRequest(
    @SerializedName("pincode")
    var pinCode: String? = null
)
