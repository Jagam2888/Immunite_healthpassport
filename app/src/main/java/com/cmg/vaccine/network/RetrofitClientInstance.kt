package com.cmg.vaccine.network

import android.content.Context
import com.cmg.vaccine.prefernces.PreferenceProvider
import com.cmg.vaccine.util.NoInternetException
import com.cmg.vaccine.util.Passparams
import io.paperdb.Paper
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun createOkHttp(context: Context): OkHttpClient {
    return OkHttpClient.Builder()
            .addInterceptor(InternetConnectionInterceptor(context))
            .connectTimeout(10,TimeUnit.SECONDS)
            .readTimeout(30,TimeUnit.SECONDS)
            .build()
}

fun RetrofitClientInstance(context: Context,preferenceProvider: PreferenceProvider) : Retrofit {
    //val url = Paper.book().read("url","")
    return Retrofit.Builder()
            .baseUrl(Passparams.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttp(context))
            .build()

}

class InternetConnectionInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!CheckInternetConnectivity.isAvailable(context))
            throw NoInternetException(CheckInternetConnectivity.errorMsg)

        return chain.proceed(chain.request())
    }
}