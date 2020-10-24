package com.trateg.bepresensimobile.ui.pilih_jenis_surat

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView

class PilihJenisSuratContract {
    interface Presenter : BasePresenter

    interface View : BaseView<Presenter> {
        fun initView()
        fun goToAjukanSuratIzin()
        fun goToAjukanSuratSakit()
        fun goToAjukanSuratDispen()
        fun showBackButton(enabled: Boolean)
    }
}