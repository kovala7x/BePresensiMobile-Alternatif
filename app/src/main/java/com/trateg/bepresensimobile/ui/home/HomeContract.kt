package com.trateg.bepresensimobile.ui.home

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView
import com.trateg.bepresensimobile.model.Jadwal
import com.trateg.bepresensimobile.util.SessionManager

interface HomeContract{
    interface Presenter: BasePresenter {
        fun getCurrentDate()
        fun setupSession()
        fun getUserRole()
        fun getJadwalMhs()
        fun getJadwalDosen()
    }

    interface View: BaseView<Presenter> {
        fun initView()
        fun initSession(): SessionManager
        fun updateTextDate(date: String)
        fun onGetJadwalMhsSuccess(jadwal: List<Jadwal>)
        fun onGetJadwalDosenSuccess(jadwal: List<Jadwal>)
        fun onError(msg: String)
        fun setDosenMode()
        fun setMhsMode()
        fun goToBukaSesiPresensi(data: Jadwal)
        fun goToTutupSesiPresensi(data: Jadwal)
        fun goToCatatPresensi(data: Jadwal)
        fun goToDetailPresensiDosen(data: Jadwal)
        fun goToDetailPresensiMhs(data: Jadwal)
        fun goToLihatPresensi(data: Jadwal)
        fun goToIsiBeritaAcara(data: Jadwal)
    }
}