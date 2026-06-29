package ru.spb.skynet.lk.data.models

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import ru.spb.skynet.lk.data.models.request.pin_code.PinCodeRequest
import ru.spb.skynet.lk.data.models.response.auth.AuthResponse
import ru.spb.skynet.lk.data.models.response.pin_code.PinCodeResponse

interface SkynetApi {

    @Multipart
    @POST("login")
    suspend fun login(
        @Part("action") action: RequestBody,
        @Part("login") login: RequestBody,
        @Part("password") password: RequestBody?,
        @Part("verification") verification: RequestBody?
    ): Response<AuthResponse>

    @POST("refresh")
    @Headers("x-trace-id:c50af6d8ba941de8054688ff16eaefeb", "Cookie:GenesisSessionRefresh=a71c03d9d9e895d4f3d27b01f75d8692")
    suspend fun refresh(@Body pinCode: PinCodeRequest): Response<PinCodeResponse>
}