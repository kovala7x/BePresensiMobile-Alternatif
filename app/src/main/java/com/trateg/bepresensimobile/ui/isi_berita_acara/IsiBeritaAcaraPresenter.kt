package com.trateg.bepresensimobile.ui.isi_berita_acara

import android.content.ContentValues
import android.util.Log
import com.trateg.bepresensimobile.data.api.ApiFactory
import com.trateg.bepresensimobile.model.BaseResponse
import com.trateg.bepresensimobile.model.BeritaAcara
import com.trateg.bepresensimobile.model.Jadwal
import com.trateg.bepresensimobile.util.Constants
import com.trateg.bepresensimobile.util.ErrorUtils
import com.trateg.bepresensimobile.util.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class IsiBeritaAcaraPresenter(private var mView: IsiBeritaAcaraContract.View?) :
    IsiBeritaAcaraContract.Presenter {
    private val api = ApiFactory.apiService
    lateinit var session: SessionManager
    override fun getCurrentDate() {
        try {
            val date: Date = Calendar.getInstance().time
            val pattern = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(pattern, Locale.getDefault())
            val currentDate: String = sdf.format(date)
            mView?.setTextDate(currentDate)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun simpanBeritaAcara(
        data: Jadwal,
        deskTatapMuka: String,
        deskPenugasan: String
    ): BeritaAcara? =
        withContext(Dispatchers.IO) {
            val kdJadwal: String = data.kdJadwal?:"null"
            val mKdJadwal: RequestBody = kdJadwal.toRequestBody(kdJadwal.toMediaTypeOrNull())
            val mDeskTatapMuka: RequestBody = deskTatapMuka.toRequestBody(deskTatapMuka.toMediaTypeOrNull())
            val mDeskPenugasan: RequestBody = deskPenugasan.toRequestBody(deskPenugasan.toMediaTypeOrNull())
            var response: Response<BaseResponse>? = null
            var resultBeritaAcara: BeritaAcara? = null
            // Menampilkan loading
            withContext(Dispatchers.Main) {
                mView?.showProgress()
            }
            // Mencegah waktu request melebihi timeout
            val request = withTimeoutOrNull(Constants.REQUEST_TIMEOUT) {
                try {
                    response = api.postBuatBeritaAcara(
                        kdJadwal = mKdJadwal,
                        deskPerkuliahan = mDeskTatapMuka,
                        deskPenugasan = mDeskPenugasan
                    )
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        mView?.showSnackbar(e.message.toString())
                        Log.d(ContentValues.TAG, "catch simpanBeritaAcara: " + e.message.toString())
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
                            Log.d(ContentValues.TAG, "simpanBeritaAcara: request is null!")
                            mView?.showSnackbar("Waktu tunggu telah habis...")
                        }
                    }

                    !response!!.isSuccessful -> { // Jika request gagal
                        val errorResponse = ErrorUtils.parseError(response!!)
                        withContext(Dispatchers.Main) {
                            Log.d(
                                ContentValues.TAG,
                                "simpanBeritaAcara: " + errorResponse.message!!.toString()
                            )
                            mView?.showSnackbar(errorResponse.message.toString())
                        }
                    }

                    response!!.body() == null -> { // Jika body response kosong
                        withContext(Dispatchers.Main) {
                            Log.d(ContentValues.TAG, "simpanBeritaAcara: response body is null!")
                            mView?.showSnackbar("Silahkan coba lagi...")
                        }
                    }

                    !response!!.body()!!.success!! -> { // Jika request pada backend gagal
                        withContext(Dispatchers.Main) {
                            Log.d(
                                ContentValues.TAG,
                                "simpanBeritaAcara: " + response!!.body()!!.message!!
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
                            Log.d(ContentValues.TAG, "simpanBeritaAcara: uncaught error!")
                            mView?.showSnackbar("Silahkan coba lagi...")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Log.d(ContentValues.TAG, "simpanBeritaAcara: exception catch is called!")
                    mView?.showSnackbar("Terjadi kesalahan...")
                }
            }
            resultBeritaAcara
        }

    override fun onSimpanBeritaAcaraSuccess(data: BeritaAcara) {
        mView?.showToast("Berhasil menyimpan berita acara!")
        navigateHome()
    }

    override fun navigateHome() {
        mView?.goToHome()
    }

    override fun setupSession() {
        session = mView?.initSession()!!
    }

    override fun onDestroy() {
        mView = null
    }
}