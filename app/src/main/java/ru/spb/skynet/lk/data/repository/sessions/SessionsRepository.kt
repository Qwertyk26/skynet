package ru.spb.skynet.lk.data.repository.sessions

import retrofit2.Response
import ru.spb.skynet.lk.data.SkynetApi
import ru.spb.skynet.lk.data.models.response.sessions.SessionsResponse
import javax.inject.Inject

class SessionsRepository @Inject constructor(private val skynetApi: SkynetApi) {

    suspend fun sessions(): Response<SessionsResponse> {
        return skynetApi.sessions()
    }
}