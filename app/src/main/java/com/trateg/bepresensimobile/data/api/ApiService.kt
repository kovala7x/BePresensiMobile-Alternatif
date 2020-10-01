package com.trateg.bepresensimobile.data.api

import com.trateg.bepresensimobile.model.BaseResponse
import com.trateg.bepresensimobile.model.BaseResponseList
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @Headers("Accept: application/json")
    @GET("mobile/presensi/daftar-hadir/")
    suspend fun getDaftarHadir(@Query("kd_jadwal") kdJadwal :String,
                               @Query("kd_sesi") kdSesi: Int) : Response<BaseResponseList>

    @Headers("Accept: application/json")
    @GET("mobile/berita-acara/detail/{kdBeritaAcara}")
    suspend fun getDetailBeritaAcara(@Path("kdBeritaAcara") kdBeritaAcara :String) : Response<BaseResponse>

    @Headers("Accept: application/json")
    @GET("mobile/jadwal/mahasiswa/{nim}")
    suspend fun getJadwalMhs(@Path("nim") nim :String) : Response<BaseResponseList>

    @Headers("Accept: application/json")
    @GET("mobile/jadwal/dosen/{kd_dosen}")
    suspend fun getJadwalDosen(@Path("kd_dosen") kdDosen :String) : Response<BaseResponseList>

    @Headers("Accept: application/json")
    @GET("mobile/presensi/persentase-kehadiran/{kdJadwal}")
    suspend fun getPersentaseKehadiran(@Path("kdJadwal") kdJadwal :String) : Response<BaseResponseList>

    @Headers("Accept: application/json")
    @POST("mobile/jadwal/sesi-presensi/buka/{kd_jadwal}")
    suspend fun postBukaSesiPresensi(@Path("kd_jadwal") kdJadwal :String) : Response<BaseResponse>

    @Headers("Accept: application/json")
    @POST("mobile/jadwal/sesi-presensi/tutup/{kd_jadwal}")
    suspend fun postTutupSesiPresensi(@Path("kd_jadwal") kdJadwal :String) : Response<BaseResponse>

    @Multipart
    @Headers("Accept: application/json")
    @POST("mobile/presensi/catat")
    suspend fun postCatatPresensi(@Part("nim") nim: RequestBody,
                                  @Part("kd_jadwal") kdJadwal: RequestBody) : Response<BaseResponse>

    @Multipart
    @Headers("Accept: application/json")
    @POST("mobile/berita-acara/buat/")
    suspend fun postBuatBeritaAcara(@Part("kd_jadwal") kdJadwal: RequestBody,
                                    @Part("desk_perkuliahan") deskPerkuliahan: RequestBody,
                                    @Part("desk_penugasan") deskPenugasan: RequestBody) : Response<BaseResponse>

    @Multipart
    @Headers("Accept: application/json")
    @POST("login")
    suspend fun postLogin(@Part("email") email: RequestBody,
                          @Part("password") password: RequestBody) : Response<BaseResponse>

    @Headers("Accept: application/json")
    @GET("mobile/rekapitulasi-kehadiran/detail/{nim}")
    suspend fun getRekap(@Path("nim") nim :String) : Response<BaseResponse>
}