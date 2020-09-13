package com.trateg.bepresensimobile.ui.profil

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView
import com.trateg.bepresensimobile.model.Rekapitulasi
import com.trateg.bepresensimobile.util.SessionManager

interface ProfilContract {
    interface Presenter : BasePresenter {
        fun setupSession()
        fun getUserData()
        fun doLogout()
        fun getRekap()
        fun onGetRekapSuccess(rekap: Rekapitulasi)
    }

    interface View : BaseView<Presenter> {
        fun initSession(): SessionManager
        fun setUserData(role: String, nama: String, kode: String, email: String)
        fun setRekap(sakit: Int, izin: Int, alfa: Int, status: String)
        fun onLogoutPressed()
        fun setMahasiswaMode()
        fun setDosenMode()
        fun onError(msg: String)
    }
}