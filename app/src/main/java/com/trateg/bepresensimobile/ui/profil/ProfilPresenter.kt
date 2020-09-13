package com.trateg.bepresensimobile.ui.profil

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import com.trateg.bepresensimobile.BaseFragment
import com.trateg.bepresensimobile.data.api.ApiFactory
import com.trateg.bepresensimobile.model.BaseResponse
import com.trateg.bepresensimobile.model.Rekapitulasi
import com.trateg.bepresensimobile.util.Constants
import com.trateg.bepresensimobile.util.ErrorUtils
import com.trateg.bepresensimobile.util.SessionManager
import com.trateg.bepresensimobile.util.TextHelper
import kotlinx.coroutines.*
import retrofit2.Response

class ProfilPresenter(var mView: ProfilContract.View?) :
    ProfilContract.Presenter {
    companion object {
        fun newInstance(args: Bundle? = null): BaseFragment {
            val fragment = ProfilFragment()
            args?.let { fragment.arguments = it }
            return fragment
        }
    }

    lateinit var session: SessionManager
    private val api = ApiFactory.apiService

    override fun setupSession() {
        session = mView?.initSession()!!
    }

    override fun getUserData() {
        GlobalScope.launch(Dispatchers.Main) {
            session.also {
                mView?.setUserData(
                    role = it.getRole(),
                    nama = TextHelper.captEachWord(it.getNama()),
                    email = it.getEmail(),
                    kode = if (it.getKdDosen().isEmpty()) it.getNIM() else it.getKdDosen()
                )

                when (it.getRole().toUpperCase()) {
                    "DOSEN" -> mView?.setDosenMode()
                    "MAHASISWA" -> mView?.setMahasiswaMode()
                }
            }
        }
    }

    override fun doLogout() {
        session.logout()
    }

    override fun getRekap() {
        val NIM = session.getNIM()
        var response: Response<BaseResponse>? = null
        GlobalScope.launch(Dispatchers.IO) {
            // Menampilkan loading
            withContext(Dispatchers.Main) {
                mView?.showProgress()
            }
            // Mencegah waktu request melebihi timeout
            val request = withTimeoutOrNull(Constants.REQUEST_TIMEOUT) {
                try {
                    response = api.getRekap(NIM)
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        mView?.onError(e.message.toString())
                        Log.d(ContentValues.TAG, "catch getRekap: " + e.message.toString())
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
                            Log.d(ContentValues.TAG, "getRekap: request is null!")
                            mView?.onError("Waktu tunggu telah habis...")
                        }
                    }

                    !response!!.isSuccessful -> { // Jika request gagal
                        val errorResponse = ErrorUtils.parseError(response!!)
                        withContext(Dispatchers.Main) {
                            Log.d(
                                ContentValues.TAG,
                                "getRekap: " + errorResponse.message!!.toString()
                            )
                            mView?.onError(errorResponse.message!!.toString())
                        }
                    }

                    response!!.body() == null -> { // Jika body response kosong
                        withContext(Dispatchers.Main) {
                            Log.d(ContentValues.TAG, "getRekap: response body is null!")
                            mView?.onError("Silahkan coba lagi...")
                        }
                    }

                    !response!!.body()!!.success!! -> { // Jika request pada backend gagal
                        withContext(Dispatchers.Main) {
                            Log.d(ContentValues.TAG, "getRekap: " + response!!.body()!!.message!!)
                            mView?.onError(response!!.body()!!.message!!)
                        }
                    }

                    response!!.body()!!.success!! -> { // Jika request berhasil
                        withContext(Dispatchers.Main) {
                            onGetRekapSuccess(response!!.body()!!.data?.rekapitulasi!!)
                        }
                    }

                    else -> {
                        withContext(Dispatchers.Main) {
                            Log.d(ContentValues.TAG, "getRekap: uncaught error!")
                            mView?.onError("Silahkan coba lagi...")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Log.d(ContentValues.TAG, "getRekap: exception catch is called!")
                    mView?.onError("Terjadi kesalahan...")
                }
            }
        }
    }

    override fun onGetRekapSuccess(rekap: Rekapitulasi) {
        GlobalScope.launch(Dispatchers.Main) {
            rekap.let {
                mView?.setRekap(
                    sakit = it.sakit?:-1,
                    izin = it.izin?:-1,
                    alfa = it.alfa?:-1,
                    status = it.status?.kdStatusRekapitulasi?:"Err!"
                )
            }
        }
    }

    override fun onDestroy() {
        mView = null
    }
}