package com.trateg.bepresensimobile.ui.detail_presensi_mahasiswa

import android.content.ContentValues
import android.util.Log
import com.trateg.bepresensimobile.data.api.ApiFactory
import com.trateg.bepresensimobile.model.BaseResponse
import com.trateg.bepresensimobile.model.Kehadiran
import com.trateg.bepresensimobile.util.Constants
import com.trateg.bepresensimobile.util.ErrorUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class DetailPresensiMahasiswaPresenter(private var mView: DetailPresensiMahasiswaContract.View?) :
    DetailPresensiMahasiswaContract.Presenter {
    private val api = ApiFactory.apiService
    override suspend fun getDetailPresensiMahasiswa(kdKehadiran: Int): Kehadiran? =
        withContext(Dispatchers.IO) {
            var response: Response<BaseResponse>? = null
            var resultKehadiran: Kehadiran? = null
            // Menampilkan loading
            withContext(Dispatchers.Main) {
                mView?.showProgress()
            }
            // Mencegah waktu request melebihi timeout
            val request = withTimeoutOrNull(Constants.REQUEST_TIMEOUT) {
                try {
                    response = api.getDetailKehadiran(kdKehadiran = kdKehadiran)
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        mView?.showSnackbar(e.message.toString())
                        Log.d(
                            ContentValues.TAG,
                            "catch getDetailPresensiMahasiswa: " + e.message.toString()
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
                            Log.d(ContentValues.TAG, "getDetailPresensiMahasiswa: request is null!")
                            mView?.showSnackbar("Waktu tunggu telah habis...")
                        }
                    }

                    !response!!.isSuccessful -> { // Jika request gagal
                        val errorResponse = ErrorUtils.parseError(response!!)
                        withContext(Dispatchers.Main) {
                            Log.d(
                                ContentValues.TAG,
                                "getDetailPresensiMahasiswa: " + errorResponse.message!!.toString()
                            )
                            mView?.showSnackbar(errorResponse.message.toString())
                        }
                    }

                    response!!.body() == null -> { // Jika body response kosong
                        withContext(Dispatchers.Main) {
                            Log.d(ContentValues.TAG, "getDetailPresensiMahasiswa: response body is null!")
                            mView?.showSnackbar("Silahkan coba lagi...")
                        }
                    }

                    !response!!.body()!!.success!! -> { // Jika request pada backend gagal
                        withContext(Dispatchers.Main) {
                            Log.d(
                                ContentValues.TAG,
                                "getDetailPresensiMahasiswa: " + response!!.body()!!.message!!
                            )
                            mView?.showSnackbar(response!!.body()!!.message!!)
                        }
                    }

                    response!!.body()!!.success!! -> { // Jika request berhasil
                        withContext(Dispatchers.Main) {
                            resultKehadiran = response!!.body()!!.data?.kehadiran!!
                        }
                    }

                    else -> {
                        withContext(Dispatchers.Main) {
                            Log.d(ContentValues.TAG, "getDetailPresensiMahasiswa: uncaught error!")
                            mView?.showSnackbar("Silahkan coba lagi...")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Log.d(ContentValues.TAG, "getDetailPresensiMahasiswa: exception catch is called!")
                    mView?.showSnackbar("Terjadi kesalahan...")
                }
            }
            resultKehadiran
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

    override fun onDestroy() {
        mView = null
    }
}