package ru.spb.skynet.lk.data.repository.home

import retrofit2.Response
import ru.spb.skynet.lk.data.models.SkynetApi
import ru.spb.skynet.lk.data.models.response.abonent.AbonentResponse
import javax.inject.Inject

class HomeRepository @Inject constructor(private val skynetApi: SkynetApi) {

    suspend fun abonent(): Response<AbonentResponse> {
        return skynetApi.abonent()
    }
}