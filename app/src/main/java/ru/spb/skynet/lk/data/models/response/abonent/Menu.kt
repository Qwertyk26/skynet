package ru.spb.skynet.lk.data.models.response.abonent


import com.google.gson.annotations.SerializedName

data class Menu(
    @SerializedName("access")
    var access: Int?,
    @SerializedName("name_code")
    var nameCode: String?
)