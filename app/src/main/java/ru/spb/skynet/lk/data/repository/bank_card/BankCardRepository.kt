package ru.spb.skynet.lk.data.repository.bank_card

import retrofit2.Response
import ru.spb.skynet.lk.data.models.SkynetApi
import ru.spb.skynet.lk.data.models.response.tokens.TokensResponse
import javax.inject.Inject

class BankCardRepository @Inject constructor(private val skynetApi: SkynetApi) {

    suspend fun backCard(): Response<TokensResponse> {
        return skynetApi.card()
    }
}