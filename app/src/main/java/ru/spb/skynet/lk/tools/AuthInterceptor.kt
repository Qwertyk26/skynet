package ru.spb.skynet.lk.tools

import okhttp3.Interceptor
import okhttp3.Response
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class AuthInterceptor(private val skyNetPreferences: SkyNetPreferences) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()

        val session = runBlocking { skyNetPreferences.sessionFlow.first() }

        val requestBuilder = originalRequest.newBuilder()

        if (!session.isNullOrEmpty()) {
            requestBuilder.addHeader("Cookie", session)
        }

        return chain.proceed(requestBuilder.build())
    }
}