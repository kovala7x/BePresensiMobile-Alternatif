package com.trateg.bepresensimobile.ui.home

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import com.trateg.bepresensimobile.BaseFragment
import com.trateg.bepresensimobile.data.api.ApiFactory
import com.trateg.bepresensimobile.model.BaseResponseList
import com.trateg.bepresensimobile.util.Constants
import com.trateg.bepresensimobile.util.ErrorUtils
import com.trateg.bepresensimobile.util.SessionManager
import kotlinx.coroutines.*
import retrofit2.Response
import java.text.DateFormat
import java.util.*


class HomePresenter(var mView: HomeContract.View?) :
    HomeContract.Presenter {

    private val api = ApiFactory.apiService
    lateinit var session: SessionManager

    companion object {
        fun newInstance(args: Bundle? = null): BaseFragment {
            val fragment = HomeFragment()
            args?.let { fragment.arguments = it }
            return fragment
        }
    }

    override fun getCurrentDate() {
        //Menampilkan hari, tanggal, dan tahun
        //Format menyesuaikan bahasa device
        val calendar: Calendar = Calendar.getInstance()
        val currentDate: String =
            DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)
        mView?.updateTextDate(currentDate)
    }

    override fun setupSession() {
        session = mView?.initSession()!!
    }

    override fun getUserRole() {
        mView?.let {
            if(session.getRole().toUpperCase().equals("DOSEN")) it.setDosenMode() else it.setMhsMode()
        }
    }

    override fun getJadwalMhs() {
        val NIM = session.getNIM()
        var response: Response<BaseResponseList>? = null
        GlobalScope.launch(Dispatchers.IO) {
            // Menampilkan loading
            withContext(Dispatchers.Main) {
                mView?.showProgress()
            }
            // Mencegah waktu request melebihi timeout
            val request = withTimeoutOrNull(Constants.REQUEST_TIMEOUT) {
                try {
                    response = api.getJadwalMhs(NIM)
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        mView?.onError(e.message.toString())
                        Log.d(ContentValues.TAG, "catch getJadwalMhs: " + e.message.toString())
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
                            Log.d(ContentValues.TAG, "getJadwalMhs: request is null!")
                            mView?.onError("Waktu tunggu telah habis...")
                        }
                    }

                    !response!!.isSuccessful -> { // Jika request gagal
                        val errorResponse = ErrorUtils.parseErrorList(response!!)
                        withContext(Dispatchers.Main) {
                            Log.d(
                                ContentValues.TAG,
                                "getJadwalMhs: " + errorResponse.message!!.toString()
                            )
                            mView?.onError(errorResponse.message.toString())
                        }
                    }

                    response!!.body() == null -> { // Jika body response kosong
                        withContext(Dispatchers.Main) {
                            Log.d(ContentValues.TAG, "getJadwalMhs: response body is null!")
                            mView?.onError("Silahkan coba lagi...")
                        }
                    }

                    !response!!.body()!!.success!! -> { // Jika request pada backend gagal
                        withContext(Dispatchers.Main) {
                            Log.d(ContentValues.TAG, "getJadwalMhs: " + response!!.body()!!.message!!)
                            mView?.onError(response!!.body()!!.message!!)
                        }
                    }

                    response!!.body()!!.success!! -> { // Jika request berhasil
                        withContext(Dispatchers.Main) {
                            mView?.onGetJadwalMhsSuccess(response!!.body()!!.data?.jadwal!!)
                        }
                    }

                    else -> {
                        withContext(Dispatchers.Main) {
                            Log.d(ContentValues.TAG, "getJadwalMhs: uncaught error!")
                            mView?.onError("Silahkan coba lagi...")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Log.d(ContentValues.TAG, "getJadwalMhs: exception catch is called!")
                    mView?.onError("Terjadi kesalahan...")
                }
            }
        }
    }

    override fun getJadwalDosen() {
        val kdDosen = session.getKdDosen()
        var response: Response<BaseResponseList>? = null
        GlobalScope.launch(Dispatchers.IO) {
            // Menampilkan loading
            withContext(Dispatchers.Main) {
                mView?.showProgress()
            }
            // Mencegah waktu request melebihi timeout
            val request = withTimeoutOrNull(Constants.REQUEST_TIMEOUT) {
                try {
                    response = api.getJadwalDosen(kdDosen)
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        mView?.onError(e.message.toString())
                        Log.d(ContentValues.TAG, "catch getJadwalDosen: " + e.message.toString())
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
                            Log.d(ContentValues.TAG, "getJadwalDosen: request is null!")
                            mView?.onError("Waktu tunggu telah habis...")
                        }
                    }

                    !response!!.isSuccessful -> { // Jika request gagal
                        val errorResponse = ErrorUtils.parseErrorList(response!!)
                        withContext(Dispatchers.Main) {
                            Log.d(
                                ContentValues.TAG,
                                "getJadwalDosen: " + errorResponse.message!!.toString()
                            )
                            mView?.onError(errorResponse.message.toString())
                        }
                    }

                    response!!.body() == null -> { // Jika body response kosong
                        withContext(Dispatchers.Main) {
                            Log.d(ContentValues.TAG, "getJadwalDosen: response body is null!")
                            mView?.onError("Silahkan coba lagi...")
                        }
                    }

                    !response!!.body()!!.success!! -> { // Jika request pada backend gagal
                        withContext(Dispatchers.Main) {
                            Log.d(ContentValues.TAG, "getJadwalDosen: " + response!!.body()!!.message!!)
                            mView?.onError(response!!.body()!!.message!!)
                        }
                    }

                    response!!.body()!!.success!! -> { // Jika request berhasil
                        withContext(Dispatchers.Main) {
                            mView?.onGetJadwalDosenSuccess(response!!.body()!!.data?.jadwal!!)
                        }
                    }

                    else -> {
                        withContext(Dispatchers.Main) {
                            Log.d(ContentValues.TAG, "getJadwalDosen: uncaught error!")
                            mView?.onError("Silahkan coba lagi...")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Log.d(ContentValues.TAG, "getJadwalDosen: exception catch is called!")
                    mView?.onError("Terjadi kesalahan...")
                }
            }
        }
    }

    override fun onDestroy() {
        mView = null
    }
}