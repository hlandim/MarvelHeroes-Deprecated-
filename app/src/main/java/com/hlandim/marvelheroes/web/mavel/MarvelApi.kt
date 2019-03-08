package com.hlandim.marvelheroes.web.mavel

import com.google.gson.GsonBuilder
import com.hlandim.marvelheroes.model.MarvelResponses
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface MarvelApi {

    companion object {
        private const val URL = "http://gateway.marvel.com/v1/public/"
        const val API_KEY_PRIVATE = "3274ea7e32afc7d375f8a219258a856414fec465"
        const val API_KEY_PUBLIC = "e1b13260fe6e390810479686622d590c"
        const val LIMIT_RESULT_SIZE = 20

        fun create(): MarvelApi {

//            2014-04-29T14:18:17-0400
            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz").create()

            val logging = HttpLoggingInterceptor()
            // set your desired log level
            logging.level = HttpLoggingInterceptor.Level.BODY

            val httpClient = OkHttpClient.Builder()
            // add your other interceptors …

            // add logging as last interceptor
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
    }

    @GET("characters")
    fun getHeroes(
        @Query("ts") ts: Long,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int,
        @Query("apikey") apikey: String = API_KEY_PUBLIC,
        @Query("hash") hash: String,
        @Query("nameStartsWith") nameStartsWith: String?
    ): Observable<MarvelResponses>
}