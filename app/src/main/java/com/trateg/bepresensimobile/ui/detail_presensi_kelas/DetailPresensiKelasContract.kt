package com.trateg.bepresensimobile.ui.detail_presensi_kelas

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView
import com.trateg.bepresensimobile.model.Kehadiran

interface DetailPresensiKelasContract {
    interface Presenter : BasePresenter {
        suspend fun getListDaftarHadirKelas(kdJadwal: String, kdSesi: Int): List<Kehadiran>?
    }

    interface View : BaseView<DetailPresensiKelasContract.Presenter> {
        fun initView()
        fun getIntentExtraData()
        fun showSnackbar(msg: String)
        fun showToast(msg: String)
        fun showBackButton(enabled: Boolean)
        fun onGetListDaftarHadirKelasSuccess(data: List<Kehadiran>)
        fun onKehadiranClicked(data: Kehadiran)
        fun setTextMatakuliah(matakuliah: String)
        fun setTextSubtitleSesi(sesi: Int)
    }
}