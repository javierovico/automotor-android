package com.example.automotor.utils

import com.example.automotor.utils.entities.AccessToken
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitBuilder {
//        private const val BASE_URL = "http://sanatorio.kamaleon360.com/api/";
    private const val BASE_URL = "http://192.168.0.4:84/api/"
    private val client = buildClient()
    @JvmStatic
    val retrofit = buildRetrofit(client)
    private fun buildClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor { chain ->
                var request = chain.request()
                val builder = request.newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Connection", "close")
                request = builder.build()
                chain.proceed(request)
            }
        return builder.build()
    }

    private fun buildRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @JvmStatic
    fun <T> createService(service: Class<T>?): T {
        return retrofit.create(service)
    }

    fun <T> createServiceWithAuth(service: Class<T>?, tokenManager: AccessToken): T {
        val newClient =
            client.newBuilder().addInterceptor { chain ->
                var request = chain.request()
                val builder = request.newBuilder()
                if (tokenManager.accessToken != null) {
                    builder.addHeader(
                        "Authorization",
                        "Bearer " + tokenManager.accessToken
                    )
                }
                request = builder.build()
                chain.proceed(request)
            }.build()       //todo: ver el refresh token
        val newRetrofit = retrofit.newBuilder().client(newClient).build()
        return newRetrofit.create(service)
    }

}