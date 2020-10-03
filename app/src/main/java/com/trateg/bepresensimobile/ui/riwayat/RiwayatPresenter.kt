package com.trateg.bepresensimobile.ui.riwayat

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import com.trateg.bepresensimobile.BaseFragment
import com.trateg.bepresensimobile.data.api.ApiFactory
import com.trateg.bepresensimobile.model.BaseResponseList
import com.trateg.bepresensimobile.model.BeritaAcara
import com.trateg.bepresensimobile.model.RiwayatKehadiran
import com.trateg.bepresensimobile.util.Constants
import com.trateg.bepresensimobile.util.ErrorUtils
import com.trateg.bepresensimobile.util.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response

class RiwayatPresenter(var mView: RiwayatContract.View?) :
    RiwayatContract.Presenter {

    private val api = ApiFactory.apiService
    lateinit var session: SessionManager

    companion object {
        fun newInstance(args: Bundle? = null): BaseFragment {
            val fragment = RiwayatFragment()
            args?.let { fragment.arguments = it }
            return fragment
        }
    }

    override fun setupSession() {
        session = mView?.initSession()!!
    }

    override fun getUserRole() {
        mView?.let {
            if (session.getRole().toUpperCase()
                    .equals("DOSEN")
            ) it.setDosenMode() else it.setMhsMode()
        }
    }

    override suspend fun getRiwayatMhs(): List<RiwayatKehadiran>? =
        withContext(Dispatchers.IO) {
            val NIM = session.getNIM()
            var response: Response<BaseResponseList>? = null
            var resultRiwayatKehadiran: List<RiwayatKehadiran>? = null
            // Menampilkan loading
            withContext(Dispatchers.Main) {
                mView?.showProgress()
            }
            // Mencegah waktu request melebihi timeout
            val request = withTimeoutOrNull(Constants.REQUEST_TIMEOUT) {
                try {
                    response = api.getRiwayatKehadiran(nim = NIM)
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        mView?.showSnackbar(e.message.toString())
                        Log.e(ContentValues.TAG, "catch getRiwayatMhs: " + e.message.toString())
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
                            Log.e(ContentValues.TAG, "getRiwayatMhs: request is null!")
                            mView?.showSnackbar("Waktu tunggu telah habis...")
                        }
                    }

                    !response!!.isSuccessful -> { // Jika request gagal
                        val errorResponse = ErrorUtils.parseErrorList(response!!)
                        withContext(Dispatchers.Main) {
                            Log.e(
                                ContentValues.TAG,
                                "getRiwayatMhs: " + errorResponse.message!!.toString()
                            )
                            mView?.showSnackbar(errorResponse.message.toString())
                        }
                    }

                    response!!.body() == null -> { // Jika body response kosong
                        withContext(Dispatchers.Main) {
                            Log.e(ContentValues.TAG, "getRiwayatMhs: response body is null!")
                            mView?.showSnackbar("Silahkan coba lagi...")
                        }
                    }

                    !response!!.body()!!.success!! -> { // Jika request pada backend gagal
                        withContext(Dispatchers.Main) {
                            Log.e(
                                ContentValues.TAG,
                                "getRiwayatMhs: " + response!!.body()!!.message!!
                            )
                            mView?.showSnackbar(response!!.body()!!.message!!)
                        }
                    }

                    response!!.body()!!.success!! -> { // Jika request berhasil
                        withContext(Dispatchers.Main) {
                            resultRiwayatKehadiran = response!!.body()!!.data?.riwayatKehadiran!!
                        }
                    }

                    else -> {
                        withContext(Dispatchers.Main) {
                            Log.e(ContentValues.TAG, "getRiwayatMhs: uncaught error!")
                            mView?.showSnackbar("Silahkan coba lagi...")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Log.e(ContentValues.TAG, "getRiwayatMhs: exception catch is called!")
                    mView?.showSnackbar("Terjadi kesalahan...")
                }
            }
            resultRiwayatKehadiran
        }

    override suspend fun getRiwayatDosen(): List<BeritaAcara>? {
        //TODO("Not yet implemented")
        return null
    }

    override fun onDestroy() {
        mView = null
    }
}