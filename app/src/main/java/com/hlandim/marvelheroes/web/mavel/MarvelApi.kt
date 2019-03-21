package com.hlandim.marvelheroes.web.mavel

import android.app.Application
import com.google.gson.GsonBuilder
import com.hlandim.marvelheroes.web.MarvelHeroResponses
import com.hlandim.marvelheroes.web.MarvelParticipationResponses
import com.hlandim.marvelheroes.web.RxRetryCallAdapterFactory
import com.hlandim.marvelheroes.web.mavel.util.SessionInterceptor
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MarvelApi {

    companion object {

        val subject = PublishSubject.create<Unit>()

        const val URL = "http://gateway.marvel.com/v1/public/"
        const val API_KEY_PRIVATE = "3274ea7e32afc7d375f8a219258a856414fec465"
        const val API_KEY_PUBLIC = "e1b13260fe6e390810479686622d590c"
        const val LIMIT_RESULT_SIZE = 20

        fun create(application: Application): MarvelApi {

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(SessionInterceptor())
                .addInterceptor(logging)

            val retrofit = Retrofit.Builder()
                .baseUrl(URL)
                .client(OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .addCallAdapterFactory(RxRetryCallAdapterFactory.create(application))
                .client(client.build())
                .build()
            return retrofit.create(MarvelApi::class.java)
        }

    }

    @GET("characters")
    fun getHeroes(
        @Query("offset") offset: Int,
        @Query("nameStartsWith") nameStartsWith: String?
    ): Observable<MarvelHeroResponses>

    @GET("{path}")
    fun getParticipationDetails(
        @Path("path", encoded = true) path: String
    ): Observable<MarvelParticipationResponses>
}