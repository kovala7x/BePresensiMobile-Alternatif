package com.trateg.bepresensimobile.ui.ajukan_surat

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
import java.util.*

class AjukanSuratPresenter(private var mView: AjukanSuratContract.View?) :
    AjukanSuratContract.Presenter {
    private val api = ApiFactory.apiService
    lateinit var session: SessionManager
    override fun setupSession() {
        session = mView?.initSession()!!
    }

    override suspend fun ajukanSurat(
        kdJenisIzin: String,
        tglMulai: String,
        tglSelesai: String,
        jamMulai: String?,
        jamSelesai: String?,
        catatanSurat: String?
    ): SuratIzin? = withContext(Dispatchers.IO) {
        var resultSuratIzin: SuratIzin? = null
        var response: Response<BaseResponse>? = null
        // Menampilkan loading
        withContext(Dispatchers.Main) {
            mView?.showProgress()
        }
        // Mencegah waktu request melebihi timeout
        val request = withTimeoutOrNull(Constants.REQUEST_TIMEOUT) {
            try {
                when (kdJenisIzin) {
                    Constants.SURAT_DISPEN -> {
                        response = api.postAjukanSuratDispen(
                            nim = session.getNIM(),
                            tglMulai = tglMulai,
                            tglSelesai = tglSelesai,
                            jamMulai = jamMulai,
                            jamSelesai = jamSelesai,
                            catatanSurat = catatanSurat
                        )
                    }
                    Constants.SURAT_IZIN -> {
                        response = api.postAjukanSuratIzin(
                            nim = session.getNIM(),
                            tglMulai = tglMulai,
                            tglSelesai = tglSelesai,
                            jamMulai = jamMulai,
                            jamSelesai = jamSelesai,
                            catatanSurat = catatanSurat
                        )
                    }
                    else -> {
                        response = api.postAjukanSuratSakit(
                            nim = session.getNIM(),
                            tglMulai = tglMulai,
                            tglSelesai = tglSelesai,
                            catatanSurat = catatanSurat
                        )
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mView?.showSnackbar(e.message.toString())
                    Log.d(
                        ContentValues.TAG,
                        "catch ajukanSurat: " + e.message.toString()
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
                        Log.d(ContentValues.TAG, "ajukanSurat: request is null!")
                        mView?.showSnackbar("Waktu tunggu telah habis...")
                    }
                }

                !response!!.isSuccessful -> { // Jika request gagal
                    val errorResponse = ErrorUtils.parseError(response!!)
                    withContext(Dispatchers.Main) {
                        Log.d(
                            ContentValues.TAG,
                            "ajukanSurat: " + errorResponse.message!!.toString()
                        )
                        mView?.showSnackbar(errorResponse.message.toString())
                    }
                }

                response!!.body() == null -> { // Jika body response kosong
                    withContext(Dispatchers.Main) {
                        Log.d(
                            ContentValues.TAG,
                            "ajukanSurat: response body is null!"
                        )
                        mView?.showSnackbar("Silahkan coba lagi...")
                    }
                }

                !response!!.body()!!.success!! -> { // Jika request pada backend gagal
                    withContext(Dispatchers.Main) {
                        Log.d(
                            ContentValues.TAG,
                            "ajukanSurat: " + response!!.body()!!.message!!
                        )
                        mView?.showSnackbar(response!!.body()!!.message!!)
                    }
                }

                response!!.body()!!.success!! -> { // Jika request berhasil
                    resultSuratIzin = response!!.body()!!.data?.suratIzin!!
                }

                else -> {
                    withContext(Dispatchers.Main) {
                        Log.d(ContentValues.TAG, "ajukanSurat: uncaught error!")
                        mView?.showSnackbar("Silahkan coba lagi...")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Log.d(
                    ContentValues.TAG,
                    "ajukanSurat: exception catch is called!"
                )
                mView?.showSnackbar("Terjadi kesalahan...")
            }
        }
        resultSuratIzin
    }

    override fun isWaktuValid(startTime: Calendar, endTime: Calendar): Boolean {
        return ((startTime == endTime) || (startTime.before(endTime)))
    }

    override fun onDestroy() {
        mView = null
    }
}