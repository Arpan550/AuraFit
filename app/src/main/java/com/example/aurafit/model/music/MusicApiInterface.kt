package com.example.aurafit.model.music

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MusicApiInterface {

    @Headers("x-rapidapi-host: deezerdevs-deezer.p.rapidapi.com","x-rapidapi-key: 87317030eemsh220bd3d7bd20ef6p15062ajsn664f1fc1cf13")
    @GET("search")
    fun getData(@Query("q") query: String): Call<MusicData>
}