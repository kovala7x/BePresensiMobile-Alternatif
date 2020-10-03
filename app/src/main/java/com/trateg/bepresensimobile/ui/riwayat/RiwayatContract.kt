package com.trateg.bepresensimobile.ui.riwayat

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView
import com.trateg.bepresensimobile.model.BeritaAcara
import com.trateg.bepresensimobile.model.Jadwal
import com.trateg.bepresensimobile.model.RiwayatKehadiran
import com.trateg.bepresensimobile.util.SessionManager

interface RiwayatContract {
    interface Presenter : BasePresenter {
        fun setupSession()
        fun getUserRole()
        suspend fun getRiwayatMhs(): List<RiwayatKehadiran>?
        suspend fun getRiwayatDosen(): List<BeritaAcara>?
    }

    interface View : BaseView<Presenter> {
        fun initView()
        fun initSession(): SessionManager
        fun showSnackbar(msg: String)
        fun showToast(msg: String)
        fun onGetRiwayatMhsSuccess(data: List<RiwayatKehadiran>)
        fun onGetRiwayatDosenSuccess(data: List<BeritaAcara>)
        fun onItemJadwalClicked(data: Jadwal)
        fun setDosenMode()
        fun setMhsMode()
        fun goToDetailRiwayatMhs(data: Jadwal)
        fun goToDetailRiwayatDosen(data: BeritaAcara)
    }
}