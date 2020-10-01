package com.trateg.bepresensimobile.ui.detail_presensi_kelas

import android.content.ContentValues
import android.util.Log
import com.trateg.bepresensimobile.data.api.ApiFactory
import com.trateg.bepresensimobile.model.BaseResponseList
import com.trateg.bepresensimobile.model.Kehadiran
import com.trateg.bepresensimobile.util.Constants
import com.trateg.bepresensimobile.util.ErrorUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response

class DetailPresensiKelasPresenter(private var mView: DetailPresensiKelasContract.View?) :
    DetailPresensiKelasContract.Presenter {
    private val api = ApiFactory.apiService
    override suspend fun getListDaftarHadirKelas(kdJadwal: String, kdSesi: Int): List<Kehadiran>? =
        withContext(Dispatchers.IO) {
            var response: Response<BaseResponseList>? = null
            var resultKehadiran: List<Kehadiran>? = null
            // Menampilkan loading
            withContext(Dispatchers.Main) {
                mView?.showProgress()
            }
            // Mencegah waktu request melebihi timeout
            val request = withTimeoutOrNull(Constants.REQUEST_TIMEOUT) {
                try {
                    response = api.getDaftarHadir(kdJadwal = kdJadwal, kdSesi = kdSesi)
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        mView?.showSnackbar(e.message.toString())
                        Log.e(
                            ContentValues.TAG,
                            "catch getListDaftarHadirKelas: " + e.message.toString()
                        )
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
                            Log.e(ContentValues.TAG, "getListDaftarHadirKelas: request is null!")
                            mView?.showSnackbar("Waktu tunggu telah habis...")
                        }
                    }

                    !response!!.isSuccessful -> { // Jika request gagal
                        val errorResponse = ErrorUtils.parseErrorList(response!!)
                        withContext(Dispatchers.Main) {
                            Log.e(
                                ContentValues.TAG,
                                "getListDaftarHadirKelas: " + errorResponse.message!!.toString()
                            )
                            mView?.showSnackbar(errorResponse.message.toString())
                        }
                    }

                    response!!.body() == null -> { // Jika body response kosong
                        withContext(Dispatchers.Main) {
                            Log.e(
                                ContentValues.TAG,
                                "getListDaftarHadirKelas: response body is null!"
                            )
                            mView?.showSnackbar("Silahkan coba lagi...")
                        }
                    }

                    !response!!.body()!!.success!! -> { // Jika request pada backend gagal
                        withContext(Dispatchers.Main) {
                            Log.e(
                                ContentValues.TAG,
                                "getListDaftarHadirKelas: " + response!!.body()!!.message!!
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
                            Log.e(ContentValues.TAG, "getListDaftarHadirKelas: uncaught error!")
                            mView?.showSnackbar("Silahkan coba lagi...")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Log.e(ContentValues.TAG, "getListDaftarHadirKelas: exception catch is called!")
                    mView?.showSnackbar("Terjadi kesalahan...")
                }
            }
            resultKehadiran
        }

    override fun onDestroy() {
        mView = null
    }

}