package ru.spb.skynet.lk.data.repository.payments_history

import retrofit2.Response
import ru.spb.skynet.lk.data.models.SkynetApi
import ru.spb.skynet.lk.data.models.response.payments_history.PaymentsHistoryResponse
import javax.inject.Inject

class PaymentsHistoryRepository @Inject constructor(private val skynetApi: SkynetApi) {

    suspend fun payments(): Response<PaymentsHistoryResponse> {
        return skynetApi.payments()
    }
}