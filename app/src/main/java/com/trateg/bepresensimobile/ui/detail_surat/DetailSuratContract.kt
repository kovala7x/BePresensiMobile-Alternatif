package com.trateg.bepresensimobile.ui.detail_surat

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView
import com.trateg.bepresensimobile.model.SuratIzin
import com.trateg.bepresensimobile.util.SessionManager

interface DetailSuratContract {
    interface Presenter: BasePresenter{
        suspend fun getDetailSuratIzin(kdSuratIzin: String): SuratIzin?
        suspend fun postPersetujuanSurat(isApproved:Boolean, kdSuratIzin: String, catatanWaliDosen: String): SuratIzin?
        fun convertDate(date: String): String
        fun isDosen(): Boolean
        fun onGetDetailSuratIzinSuccess(data: SuratIzin)
        fun onPostPersetujuanSuratSuccess(data: SuratIzin)
        fun navigateHome()
        fun setupSession()
    }
    interface View: BaseView<Presenter>{
        fun initView()
        fun initSession(): SessionManager
        fun getIntentExtraData()
        fun getCatatanWaliDosen(): String
        fun onSwipeRefresh()
        fun onPersetujuanButtonClicked(isApproved: Boolean)
        fun showApprovalLayout(show: Boolean)
        fun showDetailLayout(show: Boolean)
        fun showSnackbar(msg: String)
        fun showToast(msg: String)
        fun showBackButton(enabled: Boolean)
        fun setCardData(data: SuratIzin)
        fun goToHome()
    }
}