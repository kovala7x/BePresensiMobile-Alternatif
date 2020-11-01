package com.trateg.bepresensimobile.ui.surat_diajukan

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView
import com.trateg.bepresensimobile.model.SuratIzin
import com.trateg.bepresensimobile.util.SessionManager

interface SuratDiajukanContract {
    interface Presenter: BasePresenter {
        fun setupSession()
        fun isMahasiswa(): Boolean
        suspend fun getSuratDiajukan(): List<SuratIzin>?
    }

    interface View: BaseView<Presenter> {
        fun initView()
        fun initSession(): SessionManager
        fun showSnackbar(msg: String)
        fun showToast(msg: String)
        fun onGetSuratDiajukanSuccess(data: List<SuratIzin>)
        fun onItemSuratIzinClicked(data: SuratIzin)
        fun onFabSuratBaruClicked()
        fun onSwipeRefresh()
        fun goToDetailSuratDiajukan(data: SuratIzin)
    }
}