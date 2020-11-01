package com.trateg.bepresensimobile.ui.surat_diproses

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView
import com.trateg.bepresensimobile.model.SuratIzin
import com.trateg.bepresensimobile.util.SessionManager

interface SuratDiprosesContract {
    interface Presenter: BasePresenter{
        fun setupSession()
        fun isMahasiswa(): Boolean
        suspend fun getSuratDiproses(): List<SuratIzin>?
    }
    interface View: BaseView<Presenter>{
        fun initView()
        fun initSession(): SessionManager
        fun showSnackbar(msg: String)
        fun showToast(msg: String)
        fun onGetSuratDiprosesSuccess(data: List<SuratIzin>)
        fun onItemSuratIzinClicked(data: SuratIzin)
        fun goToDetailSuratDiproses(data: SuratIzin)
    }
}