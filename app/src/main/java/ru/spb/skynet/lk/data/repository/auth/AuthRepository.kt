package ru.spb.skynet.lk.data.repository.auth

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import ru.spb.skynet.lk.data.SkynetApi
import ru.spb.skynet.lk.data.models.request.pin_code.PinCodeRequest
import ru.spb.skynet.lk.data.models.response.abonent.AbonentResponse
import ru.spb.skynet.lk.data.models.response.auth.AuthResponse
import ru.spb.skynet.lk.data.models.response.pin_code.PinCodeResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(private val skynetApi: SkynetApi) {

    suspend fun login(login: String, password: String? = null, verification: String? = null): Response<AuthResponse> {
        val action = "authorize".toRequestBody("text/plain".toMediaType())
        val login = login.toRequestBody("text/plain".toMediaType())
        val password = password?.toRequestBody("text/plain".toMediaType())
        val verification = verification?.toRequestBody("text/plain".toMediaType())

        return skynetApi.login(action, login, password, verification)
    }

    suspend fun abonent(): Response<AbonentResponse> {
        return skynetApi.abonent()
    }
}