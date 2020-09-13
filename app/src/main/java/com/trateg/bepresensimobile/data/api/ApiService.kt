package com.trateg.bepresensimobile.data.api

import com.trateg.bepresensimobile.model.BaseResponse
import com.trateg.bepresensimobile.model.BaseResponseList
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @Headers("Accept: application/json")
    @GET("mobile/jadwal/mahasiswa/{nim}")
    suspend fun getJadwal(@Path("nim") nim :String) : BaseResponseList

    @Multipart
    @Headers("Accept: application/json")
    @POST("login")
    suspend fun postLogin(@Part("email") email: RequestBody,
                          @Part("password") password: RequestBody) : Response<BaseResponse>

    @Headers("Accept: application/json")
    @GET("mobile/rekapitulasi-kehadiran/detail/{nim}")
    suspend fun getRekap(@Path("nim") nim :String) : Response<BaseResponse>
}