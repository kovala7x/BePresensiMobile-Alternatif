package com.trateg.bepresensimobile.ui.detail_surat

import android.content.ContentValues
import android.util.Log
import com.trateg.bepresensimobile.data.api.ApiFactory
import com.trateg.bepresensimobile.model.BaseResponse
import com.trateg.bepresensimobile.model.SuratIzin
import com.trateg.bepresensimobile.util.Constants
import com.trateg.bepresensimobile.util.ErrorUtils
import com.trateg.bepresensimobile.util.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class DetailSuratPresenter(var mView: DetailSuratContract.View?) : DetailSuratContract.Presenter {
    private val api = ApiFactory.apiService
    private var session: SessionManager? = null
    override suspend fun getDetailSuratIzin(kdSuratIzin: String): SuratIzin? =
        withContext(Dispatchers.IO) {
            var response: Response<BaseResponse>? = null
            var resultSuratIzin: SuratIzin? = null
            // Menampilkan loading
            withContext(Dispatchers.Main) {
                mView?.showProgress()
            }
            // Mencegah waktu request melebihi timeout
            val request = withTimeoutOrNull(Constants.REQUEST_TIMEOUT) {
                try {
                    response = api.getDetailSurat(kdSuratIzin = kdSuratIzin)
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        mView?.showSnackbar(e.message.toString())
                        Log.d(
                            ContentValues.TAG,
                            "catch getDetailSuratIzin: " + e.message.toString()
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
                            Log.d(ContentValues.TAG, "getDetailSuratIzin: request is null!")
                            mView?.showSnackbar("Waktu tunggu telah habis...")
                        }
                    }

                    !response!!.isSuccessful -> { // Jika request gagal
                        val errorResponse = ErrorUtils.parseError(response!!)
                        withContext(Dispatchers.Main) {
                            Log.d(
                                ContentValues.TAG,
                                "getDetailSuratIzin: " + errorResponse.message!!.toString()
                            )
                            mView?.showSnackbar(errorResponse.message.toString())
                        }
                    }

                    response!!.body() == null -> { // Jika body response kosong
                        withContext(Dispatchers.Main) {
                            Log.d(ContentValues.TAG, "getDetailSuratIzin: response body is null!")
                            mView?.showSnackbar("Silahkan coba lagi...")
                        }
                    }

                    !response!!.body()!!.success!! -> { // Jika request pada backend gagal
                        withContext(Dispatchers.Main) {
                            Log.d(
                                ContentValues.TAG,
                                "getDetailSuratIzin: " + response!!.body()!!.message!!
                            )
                            mView?.showSnackbar(response!!.body()!!.message!!)
                        }
                    }

                    response!!.body()!!.success!! -> { // Jika request berhasil
                        withContext(Dispatchers.Main) {
                            resultSuratIzin = response!!.body()!!.data?.suratIzin!!
                        }
                    }

                    else -> {
                        withContext(Dispatchers.Main) {
                            Log.d(ContentValues.TAG, "getDetailSuratIzin: uncaught error!")
                            mView?.showSnackbar("Silahkan coba lagi...")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Log.d(ContentValues.TAG, "getDetailSuratIzin: exception catch is called!")
                    mView?.showSnackbar("Terjadi kesalahan...")
                }
            }
            resultSuratIzin
        }

    override suspend fun postPersetujuanSurat(
        isApproved: Boolean,
        kdSuratIzin: String,
        catatanWaliDosen: String
    ): SuratIzin? =
        withContext(Dispatchers.IO) {
            var response: Response<BaseResponse>? = null
            var resultSuratIzin: SuratIzin? = null
            // Menampilkan loading
            withContext(Dispatchers.Main) {
                mView?.showProgress()
            }
            // Mencegah waktu request melebihi timeout
            val request = withTimeoutOrNull(Constants.REQUEST_TIMEOUT) {
                try {
                    if (isApproved) {
                        response = api.postSetujuiSurat(
                            kdSuratIzin = kdSuratIzin,
                            catatanWaliDosen = catatanWaliDosen
                        )
                    } else {
                        response = api.postTolakSurat(
                            kdSuratIzin = kdSuratIzin,
                            catatanWaliDosen = catatanWaliDosen
                        )
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        mView?.showSnackbar(e.message.toString())
                        Log.d(
                            ContentValues.TAG,
                            "catch postPersetujuanSurat: " + e.message.toString()
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
                            Log.d(ContentValues.TAG, "postPersetujuanSurat: request is null!")
                            mView?.showSnackbar("Waktu tunggu telah habis...")
                        }
                    }

                    !response!!.isSuccessful -> { // Jika request gagal
                        val errorResponse = ErrorUtils.parseError(response!!)
                        withContext(Dispatchers.Main) {
                            Log.d(
                                ContentValues.TAG,
                                "postPersetujuanSurat: " + errorResponse.message!!.toString()
                            )
                            mView?.showSnackbar(errorResponse.message.toString())
                        }
                    }

                    response!!.body() == null -> { // Jika body response kosong
                        withContext(Dispatchers.Main) {
                            Log.d(ContentValues.TAG, "postPersetujuanSurat: response body is null!")
                            mView?.showSnackbar("Silahkan coba lagi...")
                        }
                    }

                    !response!!.body()!!.success!! -> { // Jika request pada backend gagal
                        withContext(Dispatchers.Main) {
                            Log.d(
                                ContentValues.TAG,
                                "postPersetujuanSurat: " + response!!.body()!!.message!!
                            )
                            mView?.showSnackbar(response!!.body()!!.message!!)
                        }
                    }

                    response!!.body()!!.success!! -> { // Jika request berhasil
                        withContext(Dispatchers.Main) {
                            mView?.showToast(response!!.body()!!.message!!)
                            resultSuratIzin = response!!.body()!!.data?.suratIzin!!
                        }
                    }

                    else -> {
                        withContext(Dispatchers.Main) {
                            Log.d(ContentValues.TAG, "postPersetujuanSurat: uncaught error!")
                            mView?.showSnackbar("Silahkan coba lagi...")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Log.d(ContentValues.TAG, "postPersetujuanSurat: exception catch is called!")
                    mView?.showSnackbar("Terjadi kesalahan...")
                }
            }
            resultSuratIzin
        }

    override fun convertDate(date: String): String {
        var dateString = "-"
        try {
            val sourcePattern = "yyyy-M-d"
            val sdf = SimpleDateFormat(sourcePattern, Locale.getDefault())
            val tanggal: Date = sdf.parse(date)!!

            val targetPattern = "dd/MM/yyyy"
            val sdf2 = SimpleDateFormat(targetPattern, Locale.getDefault())
            dateString = sdf2.format(tanggal)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dateString
    }

    override fun isDosen(): Boolean {
        return session?.getRole()?.toUpperCase().equals("DOSEN")
    }

    override fun onGetDetailSuratIzinSuccess(data: SuratIzin) {
        mView?.setCardData(data)
    }

    override fun onPostPersetujuanSuratSuccess(data: SuratIzin) {
        mView?.goToHome()
    }

    override fun navigateHome() {
        mView?.goToHome()
    }

    override fun setupSession() {
        session = mView?.initSession()
    }

    override fun onDestroy() {
        mView = null
    }
}