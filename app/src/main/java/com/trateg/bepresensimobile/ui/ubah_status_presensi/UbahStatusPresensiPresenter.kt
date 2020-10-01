package com.trateg.bepresensimobile.ui.ubah_status_presensi

import android.content.ContentValues
import android.util.Log
import com.trateg.bepresensimobile.data.api.ApiFactory
import com.trateg.bepresensimobile.model.BaseResponse
import com.trateg.bepresensimobile.model.Kehadiran
import com.trateg.bepresensimobile.util.Constants
import com.trateg.bepresensimobile.util.ErrorUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response

class UbahStatusPresensiPresenter(private var mView: UbahStatusPresensiContract.View?) :
    UbahStatusPresensiContract.Presenter {
    private val api = ApiFactory.apiService
    override suspend fun simpanPerubahanStatusPresensi(
        kdKehadiran: Int,
        isHadir: Boolean
    ): Kehadiran? =
        withContext(Dispatchers.IO) {
            var response: Response<BaseResponse>? = null
            var resultKehadiran: Kehadiran? = null
            // Menampilkan loading
            withContext(Dispatchers.Main) {
                mView?.showProgress()
            }
            // Mencegah waktu request melebihi timeout
            val request = withTimeoutOrNull(Constants.REQUEST_TIMEOUT) {
                try {
                    if(isHadir){
                        response = api.postUbahStatusKehadiran(
                            kdKehadiran = kdKehadiran,
                            isHadir = 1
                        )
                    }else{
                        response = api.postUbahStatusKehadiran(
                            kdKehadiran = kdKehadiran,
                            isHadir = 0
                        )
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        mView?.showSnackbar(e.message.toString())
                        Log.d(ContentValues.TAG, "catch simpanPerubahanStatusPresensi: " + e.message.toString())
                    }
                }
            }
            // Menghilangkan loading
            withContext(Dispatchers.Main) {
                mView?.hideProgress()
            }

            try {
                when {
                    request == null -> { // Jika waktu request melebihi timeout
                        withContext(Dispatchers.Main) {
                            Log.d(ContentValues.TAG, "simpanPerubahanStatusPresensi: request is null!")
                            mView?.showSnackbar("Waktu tunggu telah habis...")
                        }
                    }

                    !response!!.isSuccessful -> { // Jika request gagal
                        val errorResponse = ErrorUtils.parseError(response!!)
                        withContext(Dispatchers.Main) {
                            Log.d(
                                ContentValues.TAG,
                                "simpanPerubahanStatusPresensi: " + errorResponse.message!!.toString()
                            )
                            mView?.showSnackbar(errorResponse.message.toString())
                        }
                    }

                    response!!.body() == null -> { // Jika body response kosong
                        withContext(Dispatchers.Main) {
                            Log.d(ContentValues.TAG, "simpanPerubahanStatusPresensi: response body is null!")
                            mView?.showSnackbar("Silahkan coba lagi...")
                        }
                    }

                    !response!!.body()!!.success!! -> { // Jika request pada backend gagal
                        withContext(Dispatchers.Main) {
                            Log.d(
                                ContentValues.TAG,
                                "simpanPerubahanStatusPresensi: " + response!!.body()!!.message!!
                            )
                            mView?.showSnackbar(response!!.body()!!.message!!)
                        }
                    }

                    response!!.body()!!.success!! -> { // Jika request berhasil
                        withContext(Dispatchers.Main) {
                            resultKehadiran = response!!.body()!!.data?.kehadiran!!
                        }
                    }

                    else -> {
                        withContext(Dispatchers.Main) {
                            Log.d(ContentValues.TAG, "simpanPerubahanStatusPresensi: uncaught error!")
                            mView?.showSnackbar("Silahkan coba lagi...")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Log.d(ContentValues.TAG, "simpanPerubahanStatusPresensi: exception catch is called!")
                    mView?.showSnackbar("Terjadi kesalahan...")
                }
            }
            resultKehadiran
    }

    override fun onDestroy() {
        mView = null
    }
}