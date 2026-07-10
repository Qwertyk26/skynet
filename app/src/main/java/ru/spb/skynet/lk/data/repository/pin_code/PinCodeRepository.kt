package ru.spb.skynet.lk.data.repository.pin_code

import retrofit2.Response
import ru.spb.skynet.lk.data.SkynetApi
import ru.spb.skynet.lk.data.models.request.pin_code.PinCodeRequest
import ru.spb.skynet.lk.data.models.response.pin_code.PinCodeResponse
import javax.inject.Inject

class PinCodeRepository @Inject constructor(private val skynetApi: SkynetApi)  {

    suspend fun refresh(pinCodeRequest: PinCodeRequest): Response<PinCodeResponse> {
        return skynetApi.refresh(pinCodeRequest)
    }
}