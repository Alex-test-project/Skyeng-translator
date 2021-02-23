package ru.example.core.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitFactory @Inject constructor() {

    fun <T> getApi(api: Class<T>) = Retrofit.Builder()
        .client(getOkHttpClient())
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(api)

    private fun getOkHttpClient() = OkHttpClient()
        .newBuilder()
        .addInterceptor(getLoggingInterceptor())
        .build()

    private fun getLoggingInterceptor() = HttpLoggingInterceptor()
        .also { it.level = HttpLoggingInterceptor.Level.BODY }

    companion object {
        private const val BASE_URL = "https://dictionary.skyeng.ru/"
    }
}