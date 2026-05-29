package ru.spb.skynet.lk.data.repository

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import ru.spb.skynet.lk.data.models.SkynetApi
import ru.spb.skynet.lk.data.models.response.auth.AuthResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(private val skynetApi: SkynetApi) {

    suspend fun login(login: String, password: String): Response<AuthResponse> {
        val action = "authorize".toRequestBody("text/plain".toMediaType())
        val login = login.toRequestBody("text/plain".toMediaType())
        val password = password.toRequestBody("text/plain".toMediaType())
        return skynetApi.login(action, login, password)
    }
}