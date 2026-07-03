package ru.spb.skynet.lk.data.models.response.sessions


import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

@Immutable
data class Data(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("ip")
    var ip: String?,
    @SerializedName("is_current")
    var isCurrent: Boolean?,
    @SerializedName("updated_at")
    var updatedAt: Int?,
    @SerializedName("useragent")
    var useragent: String?
)