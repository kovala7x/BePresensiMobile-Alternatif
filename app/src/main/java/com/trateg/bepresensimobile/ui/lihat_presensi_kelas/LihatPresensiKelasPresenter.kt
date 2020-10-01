package com.trateg.bepresensimobile.ui.lihat_presensi_kelas

import android.content.ContentValues
import android.util.Log
import com.trateg.bepresensimobile.data.api.ApiFactory
import com.trateg.bepresensimobile.model.BaseResponseList
import com.trateg.bepresensimobile.model.PersentaseKehadiran
import com.trateg.bepresensimobile.util.Constants
import com.trateg.bepresensimobile.util.ErrorUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response

class LihatPresensiKelasPresenter (private var mView: LihatPresensiKelasContract.View?) :
    LihatPresensiKelasContract.Presenter {
    private val api = ApiFactory.apiService
    override suspend fun getListPersentaseKehadiran(kdJadwal: String): List<PersentaseKehadiran>? =
        withContext(Dispatchers.IO) {
            var response: Response<BaseResponseList>? = null
            var resultPersentaseKehadiran: List<PersentaseKehadiran>? = null
            // Menampilkan loading
            withContext(Dispatchers.Main) {
                mView?.showProgress()
            }
            // Mencegah waktu request melebihi timeout
            val request = withTimeoutOrNull(Constants.REQUEST_TIMEOUT) {
                try {
                    response = api.getPersentaseKehadiran(kdJadwal= kdJadwal)
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        mView?.showSnackbar(e.message.toString())
                        Log.e(ContentValues.TAG, "catch getListPersentaseKehadiran: " + e.message.toString())
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
                            Log.e(ContentValues.TAG, "getListPersentaseKehadiran: request is null!")
                            mView?.showSnackbar("Waktu tunggu telah habis...")
                        }
                    }

                    !response!!.isSuccessful -> { // Jika request gagal
                        val errorResponse = ErrorUtils.parseErrorList(response!!)
                        withContext(Dispatchers.Main) {
                            Log.e(
                                ContentValues.TAG,
                                "getListPersentaseKehadiran: " + errorResponse.message!!.toString()
                            )
                            mView?.showSnackbar(errorResponse.message.toString())
                        }
                    }

                    response!!.body() == null -> { // Jika body response kosong
                        withContext(Dispatchers.Main) {
                            Log.e(ContentValues.TAG, "getListPersentaseKehadiran: response body is null!")
                            mView?.showSnackbar("Silahkan coba lagi...")
                        }
                    }

                    !response!!.body()!!.success!! -> { // Jika request pada backend gagal
                        withContext(Dispatchers.Main) {
                            Log.e(
                                ContentValues.TAG,
                                "getListPersentaseKehadiran: " + response!!.body()!!.message!!
                            )
                            mView?.showSnackbar(response!!.body()!!.message!!)
                        }
                    }

                    response!!.body()!!.success!! -> { // Jika request berhasil
                        withContext(Dispatchers.Main) {
                            resultPersentaseKehadiran = response!!.body()!!.data?.persentaseKehadiran!!
                        }
                    }

                    else -> {
                        withContext(Dispatchers.Main) {
                            Log.e(ContentValues.TAG, "getListPersentaseKehadiran: uncaught error!")
                            mView?.showSnackbar("Silahkan coba lagi...")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Log.e(ContentValues.TAG, "getListPersentaseKehadiran: exception catch is called!")
                    mView?.showSnackbar("Terjadi kesalahan...")
                }
            }
            resultPersentaseKehadiran
        }

    override fun navigateHome() {
        mView?.goToHome()
    }

    override fun onDestroy() {
        mView = null
    }

}