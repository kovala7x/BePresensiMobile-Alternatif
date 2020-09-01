package com.trateg.bepresensimobile.ui.home

import android.os.Bundle
import com.trateg.bepresensimobile.BaseFragment
import com.trateg.bepresensimobile.data.api.ApiFactory
import com.trateg.bepresensimobile.model.BaseResponse
import com.trateg.bepresensimobile.model.BaseResponseList
import com.trateg.bepresensimobile.util.Constants
import kotlinx.coroutines.*
import retrofit2.await
import retrofit2.awaitResponse
import java.text.DateFormat
import java.util.*


class HomePresenter(var mView: HomeContract.View?) :
    HomeContract.Presenter {

    private val api = ApiFactory.apiService

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
            DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime())
        mView?.updateTextDate(currentDate)
    }

    override fun getDataJadwal() {
        val nim: String = "171511014" // statis belum dinamis dan harus didalam coroutine

        GlobalScope.launch(Dispatchers.IO) {
            try { // Menghindari exception not handled

                lateinit var response: BaseResponseList

                // Memastikan bahwa request jaringan tidak melebihi timeout
                withContext(Dispatchers.Main) {
                    mView?.showProgress()
                }
                val request = withTimeoutOrNull(Constants.REQUEST_TIMEOUT) {
                    response = api.getJadwal(nim)
                }
                withContext(Dispatchers.Main) {
                    mView?.hideProgress()
                }

                if (request != null) { // Memastikan bahwa waktu request tidak melebihi timeout
                    if (response.success!!) { // Memastikan request ke backend berhasil
                        withContext(Dispatchers.Main) {
                            mView?.onGetDataJadwalSuccess(response.data!!.jadwal!!)
                        }
                    } else {
                        // Request ke backend gagal
                        withContext(Dispatchers.Main) {
                            mView?.onError(response.message!!)
                        }
                    }
                } else {
                    // Waktu request melebihi timeout
                    withContext(Dispatchers.Main) {
                        mView?.onError("Waktu timeout telah habis!")
                    }
                }
            } catch (e: Exception) {
                // Terdapat exception yang di throw
                withContext(Dispatchers.Main) {
                    mView?.onError(e.message.toString())
                }
            }
        }
    }

    override fun onDestroy() {
        mView = null
    }
}