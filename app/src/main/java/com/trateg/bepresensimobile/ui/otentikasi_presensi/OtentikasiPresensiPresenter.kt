package com.trateg.bepresensimobile.ui.otentikasi_presensi

import android.content.ContentValues.TAG
import android.util.Log
import com.trateg.bepresensimobile.data.api.ApiFactory
import com.trateg.bepresensimobile.model.BaseResponse
import com.trateg.bepresensimobile.model.BaseResponseList
import com.trateg.bepresensimobile.model.Jadwal
import com.trateg.bepresensimobile.util.Constants
import com.trateg.bepresensimobile.util.ErrorUtils
import com.trateg.bepresensimobile.util.SessionManager
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

class OtentikasiPresensiPresenter(private var mView: OtentikasiPresensiContract.View?) :
    OtentikasiPresensiContract.Presenter {
    private val api = ApiFactory.apiService
    lateinit var session: SessionManager
    override fun setupSession() {
        session = mView?.initSession()!!
    }

    override fun onUserAuthenticated(actionType: Int, data: Jadwal) {
        when (actionType) {
            Constants.BUKA_PRESENSI -> {
                bukaSesiPresensi(data)
            }
            Constants.TUTUP_PRESENSI -> {
                tutupSesiPresensi(data)
            }
            Constants.CATAT_PRESENSI -> {
                catatPresensi(data)
            }
            else -> {
                mView?.onError("Gagal mengambil tipe aksi!")
            }
        }
    }

    override fun bukaSesiPresensi(data: Jadwal) {
        val kdJadwal = data.kdJadwal
        var response: Response<BaseResponse>? = null
        GlobalScope.launch(Dispatchers.IO) {
            // Menampilkan loading
            withContext(Dispatchers.Main) {
                mView?.showProgress()
                mView?.setLokasiPresensi(lokasi = "-", visible = false)
                mView?.setActionStatus(action = "Membuka sesi presensi...", visible = true)
            }
            // Mencegah waktu request melebihi timeout
            val request = withTimeoutOrNull(Constants.REQUEST_TIMEOUT) {
                try {
                    response = api.postBukaSesiPresensi(kdJadwal!!)
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        mView?.onError(e.message.toString())
                        Log.d(TAG, "catch bukaSesiPresensi: " + e.message.toString())
                    }
                }
            }
            // Menghilangkan loading
            withContext(Dispatchers.Main) {
                mView?.hideProgress()
                mView?.setActionStatus(action = "Sesi presensi dibuka...", visible = false)
            }

            try {
                when {
                    request == null -> { // Jika waktu request melebihi timeout
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "bukaSesiPresensi: request is null!")
                            mView?.onError("Waktu tunggu telah habis...")
                        }
                    }

                    !response!!.isSuccessful -> { // Jika request gagal
                        val errorResponse = ErrorUtils.parseError(response!!)
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "bukaSesiPresensi: " + errorResponse.message!!.toString())
                            mView?.onError(errorResponse.message.toString())
                        }
                    }

                    response!!.body() == null -> { // Jika body response kosong
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "bukaSesiPresensi: response body is null!")
                            mView?.onError("Silahkan coba lagi...")
                        }
                    }

                    !response!!.body()!!.success!! -> { // Jika request pada backend gagal
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "bukaSesiPresensi: " + response!!.body()!!.message!!)
                            mView?.onError(response!!.body()!!.message!!)
                        }
                    }

                    response!!.body()!!.success!! -> { // Jika request berhasil
                        withContext(Dispatchers.Main) {
                            mView?.onBukaSesiPresensiSuccess(response!!.body()!!)
                        }
                    }

                    else -> {
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "bukaSesiPresensi: uncaught error!")
                            mView?.onError("Silahkan coba lagi...")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Log.d(TAG, "bukaSesiPresensi: exception catch is called!")
                    mView?.onError("Terjadi kesalahan...")
                }
            }
        }
    }

    override fun tutupSesiPresensi(data: Jadwal) {
        val kdJadwal = data.kdJadwal
        var response: Response<BaseResponse>? = null
        GlobalScope.launch(Dispatchers.IO) {
            // Menampilkan loading
            withContext(Dispatchers.Main) {
                mView?.showProgress()
                mView?.setLokasiPresensi(lokasi = "-", visible = false)
                mView?.setActionStatus(action = "Menutup sesi presensi...", visible = true)
            }
            // Mencegah waktu request melebihi timeout
            val request = withTimeoutOrNull(Constants.REQUEST_TIMEOUT) {
                try {
                    response = api.postTutupSesiPresensi(kdJadwal!!)
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        mView?.onError(e.message.toString())
                        Log.d(TAG, "catch tutupSesiPresensi: " + e.message.toString())
                    }
                }
            }
            // Menghilangkan loading
            withContext(Dispatchers.Main) {
                mView?.hideProgress()
                mView?.setActionStatus(action = "Sesi presensi ditutup...", visible = false)
            }

            try {
                when {
                    request == null -> { // Jika waktu request melebihi timeout
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "tutupSesiPresensi: request is null!")
                            mView?.onError("Waktu tunggu telah habis...")
                        }
                    }

                    !response!!.isSuccessful -> { // Jika request gagal
                        val errorResponse = ErrorUtils.parseError(response!!)
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "tutupSesiPresensi: " + errorResponse.message!!.toString())
                            mView?.onError(errorResponse.message.toString())
                        }
                    }

                    response!!.body() == null -> { // Jika body response kosong
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "tutupSesiPresensi: response body is null!")
                            mView?.onError("Silahkan coba lagi...")
                        }
                    }

                    !response!!.body()!!.success!! -> { // Jika request pada backend gagal
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "tutupSesiPresensi: " + response!!.body()!!.message!!)
                            mView?.onError(response!!.body()!!.message!!)
                        }
                    }

                    response!!.body()!!.success!! -> { // Jika request berhasil
                        withContext(Dispatchers.Main) {
                            mView?.onTutupSesiPresensiSuccess(response!!.body()!!)
                        }
                    }

                    else -> {
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "tutupSesiPresensi: uncaught error!")
                            mView?.onError("Silahkan coba lagi...")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Log.d(TAG, "tutupSesiPresensi: exception catch is called!")
                    mView?.onError("Terjadi kesalahan...")
                }
            }
        }
    }

    override fun catatPresensi(data: Jadwal) {
        val mNIM: String = session.getNIM()
        val mKdJadwal: String = data.kdJadwal!!
        val NIM: RequestBody = mNIM.toRequestBody(mNIM.toMediaTypeOrNull())
        val kdJadwal: RequestBody = mKdJadwal.toRequestBody(mKdJadwal.toMediaTypeOrNull())
        var response: Response<BaseResponseList>? = null
        GlobalScope.launch(Dispatchers.IO) {
            // Menampilkan loading
            withContext(Dispatchers.Main) {
                mView?.showProgress()
            }
            // Mencegah waktu request melebihi timeout
            val request = withTimeoutOrNull(Constants.REQUEST_TIMEOUT) {
                try {
                    response = api.postCatatPresensi(NIM, kdJadwal)
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        mView?.onError(e.message.toString())
                        Log.d(TAG, "catch catatPresensi: " + e.message.toString())
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
                            Log.d(TAG, "catatPresensi: request is null!")
                            mView?.onError("Waktu tunggu telah habis...")
                        }
                    }

                    !response!!.isSuccessful -> { // Jika request gagal
                        val errorResponse = ErrorUtils.parseErrorList(response!!)
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "catatPresensi: " + errorResponse.message!!.toString())
                            mView?.onError(errorResponse.message.toString())
                        }
                    }

                    response!!.body() == null -> { // Jika body response kosong
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "catatPresensi: response body is null!")
                            mView?.onError("Silahkan coba lagi...")
                        }
                    }

                    !response!!.body()!!.success!! -> { // Jika request pada backend gagal
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "catatPresensi: " + response!!.body()!!.message!!)
                            mView?.onError(response!!.body()!!.message!!)
                        }
                    }

                    response!!.body()!!.success!! -> { // Jika request berhasil
                        withContext(Dispatchers.Main) {
                            mView?.onCatatPresensiSuccess(response!!.body()!!)
                        }
                    }

                    else -> {
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "catatPresensi: uncaught error!")
                            mView?.onError("Silahkan coba lagi...")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Log.d(TAG, "catatPresensi: exception catch is called!")
                    mView?.onError("Terjadi kesalahan...")
                }
            }
        }
    }

    override fun onDestroy() {
        mView = null
    }
}