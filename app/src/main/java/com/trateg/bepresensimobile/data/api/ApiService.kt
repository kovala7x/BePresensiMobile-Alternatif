package com.trateg.bepresensimobile.data.api

import com.trateg.bepresensimobile.model.BaseResponse
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("jadwal/mahasiswa/{nim}")
    suspend fun getJadwal(@Path("nim") nim :String) : BaseResponse
}