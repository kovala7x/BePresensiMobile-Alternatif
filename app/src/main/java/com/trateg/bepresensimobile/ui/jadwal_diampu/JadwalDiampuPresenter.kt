package com.trateg.bepresensimobile.ui.jadwal_diampu

import android.content.ContentValues
import android.util.Log
import com.trateg.bepresensimobile.data.api.ApiFactory
import com.trateg.bepresensimobile.model.BaseResponseList
import com.trateg.bepresensimobile.model.Jadwal
import com.trateg.bepresensimobile.util.Constants
import com.trateg.bepresensimobile.util.ErrorUtils
import com.trateg.bepresensimobile.util.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response

class JadwalDiampuPresenter(var mView: JadwalDiampuContract.View?) :
    JadwalDiampuContract.Presenter {
    private val api = ApiFactory.apiService
    private var session: SessionManager? = null
    override suspend fun getJadwalDiampu(): List<Jadwal>? = withContext(Dispatchers.IO){
        var response: Response<BaseResponseList>? = null
        var resultJadwalDiampu: List<Jadwal>? = null
        // Menampilkan loading
        withContext(Dispatchers.Main) {
            mView?.showProgress()
        }
        // Mencegah waktu request melebihi timeout
        val request = withTimeoutOrNull(Constants.REQUEST_TIMEOUT) {
            try {
                response = api.getJadwalDiampu(session?.getKdDosen()!!)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mView?.showSnackbar(e.message.toString())
                    Log.e(ContentValues.TAG, "catch getJadwalDiampu: " + e.message.toString())
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
                        Log.e(ContentValues.TAG, "getJadwalDiampu: request is null!")
                        mView?.showSnackbar("Waktu tunggu telah habis...")
                    }
                }

                !response!!.isSuccessful -> { // Jika request gagal
                    val errorResponse = ErrorUtils.parseErrorList(response!!)
                    withContext(Dispatchers.Main) {
                        Log.e(
                            ContentValues.TAG,
                            "getJadwalDiampu: " + errorResponse.message!!.toString()
                        )
                        mView?.showSnackbar(errorResponse.message.toString())
                    }
                }

                response!!.body() == null -> { // Jika body response kosong
                    withContext(Dispatchers.Main) {
                        Log.e(ContentValues.TAG, "getJadwalDiampu: response body is null!")
                        mView?.showSnackbar("Silahkan coba lagi...")
                    }
                }

                !response!!.body()!!.success!! -> { // Jika request pada backend gagal
                    withContext(Dispatchers.Main) {
                        Log.e(
                            ContentValues.TAG,
                            "getJadwalDiampu: " + response!!.body()!!.message!!
                        )
                        mView?.showSnackbar(response!!.body()!!.message!!)
                    }
                }

                response!!.body()!!.success!! -> { // Jika request berhasil
                    withContext(Dispatchers.Main) {
                        resultJadwalDiampu = response!!.body()!!.data?.jadwal!!
                    }
                }

                else -> {
                    withContext(Dispatchers.Main) {
                        Log.e(ContentValues.TAG, "getJadwalDiampu: uncaught error!")
                        mView?.showSnackbar("Silahkan coba lagi...")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Log.e(ContentValues.TAG, "getJadwalDiampu: exception catch is called!")
                mView?.showSnackbar("Terjadi kesalahan...")
            }
        }
        resultJadwalDiampu
    }

    override fun setupSession(sessionManager: SessionManager) {
        session = sessionManager
    }

    override fun onDestroy() {
        mView = null
    }
}