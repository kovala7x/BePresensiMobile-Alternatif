package com.trateg.bepresensimobile.data.api

import com.trateg.bepresensimobile.model.BaseResponseList
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ApiService {
    @Headers("Accept: application/json")
    @GET("jadwal/mahasiswa/{nim}")
    suspend fun getJadwal(@Path("nim") nim :String) : BaseResponseList
}