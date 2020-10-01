package com.trateg.bepresensimobile.ui.detail_berita_acara

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView
import com.trateg.bepresensimobile.model.BeritaAcara

interface DetailBeritaAcaraContract {
    interface Presenter : BasePresenter {
        suspend fun getDetailBeritaAcara(kdBeritaAcara: String): BeritaAcara?
        fun convertDate(date: String): String
        fun onGetDetailBeritaAcaraSuccess(data: BeritaAcara)
        fun navigateHome()
    }

    interface View : BaseView<DetailBeritaAcaraContract.Presenter> {
        fun initView()
        fun getIntentExtraData()
        fun showLayoutDetail(show: Boolean)
        fun showSnackbar(msg: String)
        fun showToast(msg: String)
        fun showBackButton(enabled: Boolean)
        fun setTextDate(date: String)
        fun setCardData(data: BeritaAcara)
        fun goToHome()
    }
}