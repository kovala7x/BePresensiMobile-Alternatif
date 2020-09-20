package com.trateg.bepresensimobile.ui.isi_berita_acara

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView
import com.trateg.bepresensimobile.model.BeritaAcara
import com.trateg.bepresensimobile.model.Jadwal
import com.trateg.bepresensimobile.util.SessionManager

interface IsiBeritaAcaraContract {
    interface Presenter : BasePresenter {
        fun getCurrentDate()
        suspend fun simpanBeritaAcara(data: Jadwal, deskTatapMuka: String, deskPenugasan: String): BeritaAcara?
        fun onSimpanBeritaAcaraSuccess(data: BeritaAcara)
        fun navigateHome()
        fun setupSession()
    }

    interface View : BaseView<IsiBeritaAcaraContract.Presenter> {
        fun initView()
        fun initSession(): SessionManager
        fun getIntentExtraData()
        fun getDeskTatapMuka(): String
        fun getDeskPenugasan(): String
        fun showDialog(title: String, msg: String)
        fun showSnackbar(msg: String)
        fun showToast(msg: String)
        fun showBackButton(enabled: Boolean)
        fun isFormValid(): Boolean
        fun setTextDate(date: String)
        fun setCardData(data: Jadwal)
        fun hideSoftKeyboard()
        fun enableInputForm(enabled: Boolean)
        fun goToHome()
    }
}