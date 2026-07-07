package ru.spb.skynet.lk.data.models.response.tokens


import com.google.gson.annotations.SerializedName

data class TokensResponse(
    @SerializedName("data")
    var data: List<TokenData?>?,
    @SerializedName("result")
    var result: String?,
    @SerializedName("traceId")
    var traceId: String?
)