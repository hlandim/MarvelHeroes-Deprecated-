package com.hlandim.marvelheroes.web.mavel

import com.google.gson.GsonBuilder
import com.hlandim.marvelheroes.model.MarvelParticipationResponses
import com.hlandim.marvelheroes.model.MarvelHeroResponses
import com.hlandim.marvelheroes.util.md5
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*


interface MarvelApi {

    companion object {

        const val URL = "http://gateway.marvel.com/v1/public/"
        const val API_KEY_PRIVATE = "3274ea7e32afc7d375f8a219258a856414fec465"
        const val API_KEY_PUBLIC = "e1b13260fe6e390810479686622d590c"
        const val LIMIT_RESULT_SIZE = 20

        fun create(): MarvelApi {

            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz").create()
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)  // <--
            val retrofit = Retrofit.Builder()
                .baseUrl(URL)
                .client(OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build()
            return retrofit.create(MarvelApi::class.java)
        }

        private fun getHashMd5(timeStamp: Long): String {
            val bodyHash = timeStamp.toString() + API_KEY_PRIVATE + API_KEY_PUBLIC
            return bodyHash.md5()
        }
    }

    @GET("characters")
    fun getHeroes(
        @Query("ts") ts: Long = Date().time,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int,
        @Query("apikey") apikey: String = API_KEY_PUBLIC,
        @Query("hash") hash: String = getHashMd5(ts),
        @Query("nameStartsWith") nameStartsWith: String?
    ): Observable<MarvelHeroResponses>

    @GET("{path}")
    fun getParticipationDetails(
        @Path("path", encoded = true) path: String,
        @Query("ts") ts: Long = Date().time,
        @Query("apikey") apikey: String = API_KEY_PUBLIC,
        @Query("hash") hash: String = getHashMd5(ts)
    ): Observable<MarvelParticipationResponses>
}