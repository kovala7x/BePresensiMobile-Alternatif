package com.trateg.bepresensimobile.ui.surat_diproses

import android.content.ContentValues
import android.util.Log
import com.trateg.bepresensimobile.data.api.ApiFactory
import com.trateg.bepresensimobile.model.BaseResponseList
import com.trateg.bepresensimobile.model.SuratIzin
import com.trateg.bepresensimobile.util.Constants
import com.trateg.bepresensimobile.util.ErrorUtils
import com.trateg.bepresensimobile.util.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response

class SuratDiprosesPresenter(var mView: SuratDiprosesContract.View?) :
    SuratDiprosesContract.Presenter {
    private val api = ApiFactory.apiService
    lateinit var session: SessionManager
    override fun setupSession() {
        session = mView?.initSession()!!
    }

    override fun isMahasiswa(): Boolean {
        return session.getRole().toUpperCase().equals("MAHASISWA")
    }

    override suspend fun getSuratDiproses(): List<SuratIzin>? = withContext(Dispatchers.IO) {
        var response: Response<BaseResponseList>? = null
        var resultSuratDiproses: List<SuratIzin>? = null
        // Menampilkan loading
        withContext(Dispatchers.Main) {
            mView?.showProgress()
        }
        // Mencegah waktu request melebihi timeout
        val request = withTimeoutOrNull(Constants.REQUEST_TIMEOUT) {
            try {
                when (session.getRole().toUpperCase()) {
                    "MAHASISWA" -> {
                        response = api.getSuratDiproses(nim = session.getNIM())
                    }
                    else -> {
                        response = api.getSuratDiprosesDosen(kdDosen = session.getKdDosen())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mView?.showSnackbar(e.message.toString())
                    Log.e(ContentValues.TAG, "catch getSuratDiproses: " + e.message.toString())
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
                        Log.e(ContentValues.TAG, "getSuratDiproses: request is null!")
                        mView?.showSnackbar("Waktu tunggu telah habis...")
                    }
                }

                !response!!.isSuccessful -> { // Jika request gagal
                    val errorResponse = ErrorUtils.parseErrorList(response!!)
                    withContext(Dispatchers.Main) {
                        Log.e(
                            ContentValues.TAG,
                            "getSuratDiproses: " + errorResponse.message!!.toString()
                        )
                        mView?.showSnackbar(errorResponse.message.toString())
                    }
                }

                response!!.body() == null -> { // Jika body response kosong
                    withContext(Dispatchers.Main) {
                        Log.e(ContentValues.TAG, "getSuratDiproses: response body is null!")
                        mView?.showSnackbar("Silahkan coba lagi...")
                    }
                }

                !response!!.body()!!.success!! -> { // Jika request pada backend gagal
                    withContext(Dispatchers.Main) {
                        Log.e(
                            ContentValues.TAG,
                            "getSuratDiproses: " + response!!.body()!!.message!!
                        )
                        mView?.showSnackbar(response!!.body()!!.message!!)
                    }
                }

                response!!.body()!!.success!! -> { // Jika request berhasil
                    withContext(Dispatchers.Main) {
                        resultSuratDiproses = response!!.body()!!.data?.suratIzin!!
                    }
                }

                else -> {
                    withContext(Dispatchers.Main) {
                        Log.e(ContentValues.TAG, "getSuratDiproses: uncaught error!")
                        mView?.showSnackbar("Silahkan coba lagi...")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Log.e(ContentValues.TAG, "getSuratDiproses: exception catch is called!")
                mView?.showSnackbar("Terjadi kesalahan...")
            }
        }
        resultSuratDiproses
    }

    override fun onDestroy() {
        mView = null
    }
}