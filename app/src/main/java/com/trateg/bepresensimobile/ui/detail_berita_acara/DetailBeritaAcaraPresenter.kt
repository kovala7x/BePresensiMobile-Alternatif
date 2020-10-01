package com.trateg.bepresensimobile.ui.detail_berita_acara

import android.content.ContentValues
import android.util.Log
import com.trateg.bepresensimobile.data.api.ApiFactory
import com.trateg.bepresensimobile.model.BaseResponse
import com.trateg.bepresensimobile.model.BeritaAcara
import com.trateg.bepresensimobile.util.Constants
import com.trateg.bepresensimobile.util.ErrorUtils
import com.trateg.bepresensimobile.util.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class DetailBeritaAcaraPresenter(private var mView: DetailBeritaAcaraContract.View?) :
    DetailBeritaAcaraContract.Presenter {
    private val api = ApiFactory.apiService
    lateinit var session: SessionManager
    override suspend fun getDetailBeritaAcara(kdBeritaAcara: String): BeritaAcara? =
        withContext(Dispatchers.IO) {
            var response: Response<BaseResponse>? = null
            var resultBeritaAcara: BeritaAcara? = null
            // Menampilkan loading
            withContext(Dispatchers.Main) {
                mView?.showProgress()
            }
            // Mencegah waktu request melebihi timeout
            val request = withTimeoutOrNull(Constants.REQUEST_TIMEOUT) {
                try {
                    response = api.getDetailBeritaAcara(kdBeritaAcara= kdBeritaAcara)
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        mView?.showSnackbar(e.message.toString())
                        Log.d(ContentValues.TAG, "catch getDetailBeritaAcara: " + e.message.toString())
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
                            Log.d(ContentValues.TAG, "getDetailBeritaAcara: request is null!")
                            mView?.showSnackbar("Waktu tunggu telah habis...")
                        }
                    }

                    !response!!.isSuccessful -> { // Jika request gagal
                        val errorResponse = ErrorUtils.parseError(response!!)
                        withContext(Dispatchers.Main) {
                            Log.d(
                                ContentValues.TAG,
                                "getDetailBeritaAcara: " + errorResponse.message!!.toString()
                            )
                            mView?.showSnackbar(errorResponse.message.toString())
                        }
                    }

                    response!!.body() == null -> { // Jika body response kosong
                        withContext(Dispatchers.Main) {
                            Log.d(ContentValues.TAG, "getDetailBeritaAcara: response body is null!")
                            mView?.showSnackbar("Silahkan coba lagi...")
                        }
                    }

                    !response!!.body()!!.success!! -> { // Jika request pada backend gagal
                        withContext(Dispatchers.Main) {
                            Log.d(
                                ContentValues.TAG,
                                "getDetailBeritaAcara: " + response!!.body()!!.message!!
                            )
                            mView?.showSnackbar(response!!.body()!!.message!!)
                        }
                    }

                    response!!.body()!!.success!! -> { // Jika request berhasil
                        withContext(Dispatchers.Main) {
                            resultBeritaAcara = response!!.body()!!.data?.beritaAcara!!
                        }
                    }

                    else -> {
                        withContext(Dispatchers.Main) {
                            Log.d(ContentValues.TAG, "getDetailBeritaAcara: uncaught error!")
                            mView?.showSnackbar("Silahkan coba lagi...")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Log.d(ContentValues.TAG, "getDetailBeritaAcara: exception catch is called!")
                    mView?.showSnackbar("Terjadi kesalahan...")
                }
            }
            resultBeritaAcara
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

    override fun onGetDetailBeritaAcaraSuccess(data: BeritaAcara) {
        mView?.setCardData(data)
    }

    override fun navigateHome() {
        mView?.goToHome()
    }

    override fun onDestroy() {
        mView = null
    }
}