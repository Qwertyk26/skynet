package ru.spb.skynet.lk.tools

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.Response
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ru.spb.skynet.lk.navigation.AppNavigator
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val skyNetPreferences: SkyNetPreferences,
    private val coroutineScope: CoroutineScope
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val session = runBlocking { skyNetPreferences.sessionFlow.first() }

        val requestBuilder = request.newBuilder()
        if (!session.isNullOrEmpty()) {
            requestBuilder.addHeader("Cookie", session)
        }

        val response = chain.proceed(requestBuilder.build())

        val peekBody = response.peekBody(1024 * 1024)
        val bodyString = peekBody.string()

        if (bodyString.contains("NOT_FOUND[refresh_session]", ignoreCase = true)) {
            coroutineScope.launch {
                skyNetPreferences.clearSession()
                withContext(Dispatchers.Main) {
                    AppNavigator.navigateToLogin()
                }
            }
        }
        return response
    }
}