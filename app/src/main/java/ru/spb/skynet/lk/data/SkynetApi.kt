package ru.spb.skynet.lk.data

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import ru.spb.skynet.lk.data.models.request.pin_code.PinCodeRequest
import ru.spb.skynet.lk.data.models.response.abonent.AbonentResponse
import ru.spb.skynet.lk.data.models.response.auth.AuthResponse
import ru.spb.skynet.lk.data.models.response.checks.CheckResponse
import ru.spb.skynet.lk.data.models.response.notifications.NotificationResponse
import ru.spb.skynet.lk.data.models.response.orders.OrderResponse
import ru.spb.skynet.lk.data.models.response.payments_history.PaymentsHistoryResponse
import ru.spb.skynet.lk.data.models.response.pin_code.PinCodeResponse
import ru.spb.skynet.lk.data.models.response.pos.PosResponse
import ru.spb.skynet.lk.data.models.response.sessions.SessionsResponse
import ru.spb.skynet.lk.data.models.response.settings.SettingsResponse
import ru.spb.skynet.lk.data.models.response.tokens.TokensResponse

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
    suspend fun refresh(@Body pinCode: PinCodeRequest): Response<PinCodeResponse>

    @GET("settings")
    suspend fun setting(): Response<SettingsResponse>

    @GET("settings/sessions")
    suspend fun sessions(): Response<SessionsResponse>

    @GET("notifications")
    suspend fun notifications(
        @Query("page_limit") pageLimit: Int,
        @Query("offset") offset: Int
    ): Response<NotificationResponse>

    @GET("abonent")
    suspend fun abonent(): Response<AbonentResponse>

    @GET("payments")
    suspend fun payments(): Response<PaymentsHistoryResponse>

    @GET("pos")
    suspend fun pos(): Response<PosResponse>

    @GET("payments/tokens")
    suspend fun card(): Response<TokensResponse>

    @GET("payments/orders")
    suspend fun orders(): Response<OrderResponse>

    @GET("checks")
    suspend fun checks(): Response<CheckResponse>
}