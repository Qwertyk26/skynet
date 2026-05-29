package ru.spb.skynet.lk.data.models

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import ru.spb.skynet.lk.data.models.response.auth.AuthResponse

interface SkynetApi {

    @Multipart
    @POST("login")
    suspend fun login(
        @Part("action") action: RequestBody,
        @Part("login") login: RequestBody,
        @Part("password") password: RequestBody
    ): Response<AuthResponse>
}