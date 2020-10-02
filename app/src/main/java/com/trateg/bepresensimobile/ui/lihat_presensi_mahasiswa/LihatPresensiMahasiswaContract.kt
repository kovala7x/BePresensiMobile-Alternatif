package com.trateg.bepresensimobile.ui.lihat_presensi_mahasiswa

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView
import com.trateg.bepresensimobile.model.Jadwal
import com.trateg.bepresensimobile.model.Kehadiran

interface LihatPresensiMahasiswaContract {
    interface Presenter : BasePresenter {
        fun convertDate(date: String): String
    }

    interface View : BaseView<Presenter> {
        fun initView()
        fun getIntentExtraData()
        fun showSnackbar(msg: String)
        fun showToast(msg: String)
        fun showBackButton(enabled: Boolean)
        fun setCardData(data: Jadwal)
        fun setRecyclerviewKehadiran(data: List<Kehadiran>)
    }
}