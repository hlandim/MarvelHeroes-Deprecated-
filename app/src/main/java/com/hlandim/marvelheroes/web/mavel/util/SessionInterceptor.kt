package com.hlandim.marvelheroes.web.mavel.util

import com.hlandim.marvelheroes.util.md5
import com.hlandim.marvelheroes.web.mavel.MarvelApi
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

class SessionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val ts: String = Date().time.toString()
        val url = chain.request().url().newBuilder()
            .addQueryParameter("ts", ts)
            .addQueryParameter("limit", MarvelApi.LIMIT_RESULT_SIZE.toString())
            .addQueryParameter("apikey", MarvelApi.API_KEY_PUBLIC)
            .addQueryParameter("hash", getHashMd5(ts))
            .build()
        val request = chain.request().newBuilder()
            // .addHeader("Authorization", "Bearer token")
            .url(url)
            .build()
        return chain.proceed(request)
    }

    private fun getHashMd5(timeStamp: String): String {
        val bodyHash = "$timeStamp${MarvelApi.API_KEY_PRIVATE}${MarvelApi.API_KEY_PUBLIC}"
        return bodyHash.md5()
    }

}