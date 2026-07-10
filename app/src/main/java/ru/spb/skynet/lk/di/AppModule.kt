package ru.spb.skynet.lk.di

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.Call
import okhttp3.EventListener
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.spb.skynet.lk.BuildConfig
import ru.spb.skynet.lk.SkynetApp
import ru.spb.skynet.lk.data.SkynetApi
import ru.spb.skynet.lk.navigation.AppNavigator
import ru.spb.skynet.lk.tools.AuthInterceptor
import ru.spb.skynet.lk.tools.SkyNetPreferences
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.Main)

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext context: Context): SkynetApp {
        return context as SkynetApp
    }

    @Provides
    @Singleton
    fun provideSkyNetPreferences(@ApplicationContext context: Context): SkyNetPreferences {
        return SkyNetPreferences(context)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        skyNetPreferences: SkyNetPreferences,
        coroutineScope: CoroutineScope
    ): AuthInterceptor {
        return AuthInterceptor(skyNetPreferences, coroutineScope)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val eventListener = object : EventListener() {
            override fun callStart(call: Call) {
                Log.d("Network", "📡 Запрос начат: ${call.request().url}")
            }

            override fun callEnd(call: Call) {
                Log.d("Network", "✅ Запрос завершен: ${call.request().url}")
            }

            override fun callFailed(call: Call, ioe: IOException) {
                Log.e("Network", "❌ Ошибка: ${ioe.message}", ioe)
            }
        }

        return OkHttpClient.Builder()
            .eventListener(eventListener)
            .addNetworkInterceptor(httpLoggingInterceptor)
            .addNetworkInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    @Singleton
    fun provideSkynetApi(retrofit: Retrofit): SkynetApi = retrofit.create(SkynetApi::class.java)
}