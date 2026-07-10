package ru.spb.skynet.lk.data.repository.balance

import retrofit2.Response
import ru.spb.skynet.lk.data.SkynetApi
import ru.spb.skynet.lk.data.models.response.checks.CheckResponse
import ru.spb.skynet.lk.data.models.response.orders.OrderResponse
import ru.spb.skynet.lk.data.models.response.payments_history.PaymentsHistoryResponse
import ru.spb.skynet.lk.data.models.response.tokens.TokensResponse
import javax.inject.Inject

class BalanceRepository @Inject constructor(private val skynetApi: SkynetApi) {

    suspend fun backCard(): Response<TokensResponse> {
        return skynetApi.card()
    }

    suspend fun payments(): Response<PaymentsHistoryResponse> {
        return skynetApi.payments()
    }

    suspend fun orders(): Response<OrderResponse> {
        return skynetApi.orders()
    }

    suspend fun checks(): Response<CheckResponse> {
        return skynetApi.checks()
    }
}