package ru.spb.skynet.lk.data.repository.settings

import retrofit2.Response
import ru.spb.skynet.lk.data.SkynetApi
import ru.spb.skynet.lk.data.models.response.settings.SettingsResponse
import javax.inject.Inject

class SettingsRepository @Inject constructor(private val skynetApi: SkynetApi) {

    suspend fun settings(): Response<SettingsResponse> {
        return skynetApi.setting()
    }
}