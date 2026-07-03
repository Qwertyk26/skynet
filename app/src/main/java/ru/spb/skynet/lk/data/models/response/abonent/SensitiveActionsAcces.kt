package ru.spb.skynet.lk.data.models.response.abonent


import com.google.gson.annotations.SerializedName

data class SensitiveActionsAcces(
    @SerializedName("action")
    var action: String?,
    @SerializedName("method")
    var method: String?
)