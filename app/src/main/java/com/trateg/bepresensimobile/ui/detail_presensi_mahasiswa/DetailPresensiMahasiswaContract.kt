package com.trateg.bepresensimobile.ui.detail_presensi_mahasiswa

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView
import com.trateg.bepresensimobile.model.Kehadiran

interface DetailPresensiMahasiswaContract {
    interface Presenter : BasePresenter {
        suspend fun getDetailPresensiMahasiswa(kdKehadiran: Int): Kehadiran?
        fun convertDate(date: String): String
    }

    interface View : BaseView<DetailPresensiMahasiswaContract.Presenter> {
        fun initView()
        fun getIntentExtraData()
        fun onGetDetailPresensiMahasiswaSuccess(data: Kehadiran)
        fun onCardSuratIzinClicked()
        fun showLayoutDetail(show: Boolean)
        fun showSnackbar(msg: String)
        fun showToast(msg: String)
        fun showBackButton(enabled: Boolean)
        fun setCardData(data: Kehadiran)
    }
}