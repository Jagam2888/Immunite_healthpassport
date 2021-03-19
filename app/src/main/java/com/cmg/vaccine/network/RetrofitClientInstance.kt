package com.cmg.vaccine.network

import android.content.Context
import com.cmg.vaccine.util.NoInternetException
import com.cmg.vaccine.util.Passparams
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

fun createOkHttp(context: Context): OkHttpClient {
    return OkHttpClient.Builder()
            .addInterceptor(InternetConnectionInterceptor(context))
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .build()
}

fun RetrofitClientInstance(context: Context) : Retrofit {
    return Retrofit.Builder()
            .baseUrl(Passparams.URL)
            .addConverterFactory(GsonConverterFactory.create())
        /*.addCallAdapterFactory(RxJava2CallAdapterFactory.create())*/
            .client(createOkHttp(context))
            .build()

}

fun getUnSafeOkkHttpClient(): OkHttpClient {
    var builder = OkHttpClient.Builder()
    try {
        val trustAllCerts: Array<TrustManager> = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }
        )

        // Install the all-trusting trust manager

        // Install the all-trusting trust manager
        val sslContext: SSLContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())

        // Create an ssl socket factory with our all-trusting manager

        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory


        builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        builder.hostnameVerifier { hostname, session -> true }

        return builder.build()
    } catch (e: Exception) {
        RuntimeException(e)
    }
    return builder.build()
}

fun RetrofitBlockChainClientInstance(context: Context) : Retrofit {
    return Retrofit.Builder()
        .baseUrl(Passparams.ICARE_URL)
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