package com.trateg.bepresensimobile.ui.jadwal_diampu

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView
import com.trateg.bepresensimobile.model.Jadwal
import com.trateg.bepresensimobile.util.SessionManager

interface JadwalDiampuContract {
    interface Presenter: BasePresenter{
        suspend fun getJadwalDiampu(): List<Jadwal>?
        fun setupSession(sessionManager: SessionManager)
    }
    interface View: BaseView<Presenter>{
        fun goToUbahToleransiWaktu(data: Jadwal)
        fun initView()
        fun onSwipeRefresh()
        fun onGetJadwalDiampuSuccess(data: List<Jadwal>)
        fun onJadwalClicked(data: Jadwal)
        fun showBackButton(show: Boolean)
        fun showSnackbar(msg: String)
        fun showToast(msg: String)
    }
}