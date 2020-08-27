package com.trateg.bepresensimobile.surat

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView

interface SuratContract{
    interface Presenter: BasePresenter {
        fun onViewCreated()
    }

    interface View:
        BaseView<Presenter> {
    }
}