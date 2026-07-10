package ru.spb.skynet.lk.data.repository.home

import retrofit2.Response
import ru.spb.skynet.lk.data.SkynetApi
import ru.spb.skynet.lk.data.models.response.abonent.AbonentResponse
import ru.spb.skynet.lk.data.models.response.pos.PosResponse
import javax.inject.Inject

class HomeRepository @Inject constructor(private val skynetApi: SkynetApi) {

    suspend fun abonent(): Response<AbonentResponse> {
        return skynetApi.abonent()
    }

    suspend fun pos(): Response<PosResponse> {
        return skynetApi.pos()
    }
}