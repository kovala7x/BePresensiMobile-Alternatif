package com.trateg.bepresensimobile.ui.ubah_toleransi_keterlambatan

import android.content.ContentValues
import android.util.Log
import com.trateg.bepresensimobile.data.api.ApiFactory
import com.trateg.bepresensimobile.model.BaseResponse
import com.trateg.bepresensimobile.model.Jadwal
import com.trateg.bepresensimobile.util.Constants
import com.trateg.bepresensimobile.util.ErrorUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response

class UbahToleransiKeterlambatanPresenter(var mView: UbahToleransiKeterlambatanContract.View?) :
    UbahToleransiKeterlambatanContract.Presenter {
    private val api = ApiFactory.apiService
    override suspend fun postUbahToleransiKeterlambatan(kdJadwal: String, toleransi: Int): Jadwal? =
        withContext(Dispatchers.IO) {
            var response: Response<BaseResponse>? = null
            var resultJadwal: Jadwal? = null
            // Menampilkan loading
            withContext(Dispatchers.Main) {
                mView?.showProgress()
            }
            // Mencegah waktu request melebihi timeout
            val request = withTimeoutOrNull(Constants.REQUEST_TIMEOUT) {
                try {
                    response = api.postUbahToleransiKeterlambatan(
                        kdJadwal = kdJadwal,
                        toleransiKeterlambatan = toleransi
                    )
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        mView?.showSnackbar(e.message.toString())
                        Log.d(
                            ContentValues.TAG,
                            "catch postUbahToleransiKeterlambatan: " + e.message.toString()
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
                            Log.d(
                                ContentValues.TAG,
                                "postUbahToleransiKeterlambatan: request is null!"
                            )
                            mView?.showSnackbar("Waktu tunggu telah habis...")
                        }
                    }

                    !response!!.isSuccessful -> { // Jika request gagal
                        val errorResponse = ErrorUtils.parseError(response!!)
                        withContext(Dispatchers.Main) {
                            Log.d(
                                ContentValues.TAG,
                                "postUbahToleransiKeterlambatan: " + errorResponse.message!!.toString()
                            )
                            mView?.showSnackbar(errorResponse.message.toString())
                        }
                    }

                    response!!.body() == null -> { // Jika body response kosong
                        withContext(Dispatchers.Main) {
                            Log.d(
                                ContentValues.TAG,
                                "postUbahToleransiKeterlambatan: response body is null!"
                            )
                            mView?.showSnackbar("Silahkan coba lagi...")
                        }
                    }

                    !response!!.body()!!.success!! -> { // Jika request pada backend gagal
                        withContext(Dispatchers.Main) {
                            Log.d(
                                ContentValues.TAG,
                                "postUbahToleransiKeterlambatan: " + response!!.body()!!.message!!
                            )
                            mView?.showSnackbar(response!!.body()!!.message!!)
                        }
                    }

                    response!!.body()!!.success!! -> { // Jika request berhasil
                        withContext(Dispatchers.Main) {
                            mView?.showToast(response!!.body()!!.message!!)
                            resultJadwal = response!!.body()!!.data?.jadwal!!
                        }
                    }

                    else -> {
                        withContext(Dispatchers.Main) {
                            Log.d(
                                ContentValues.TAG,
                                "postUbahToleransiKeterlambatan: uncaught error!"
                            )
                            mView?.showSnackbar("Silahkan coba lagi...")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Log.d(
                        ContentValues.TAG,
                        "postUbahToleransiKeterlambatan: exception catch is called!"
                    )
                    mView?.showSnackbar("Terjadi kesalahan...")
                }
            }
            resultJadwal
        }

    override fun onDestroy() {
        mView = null
    }
}