package com.trateg.bepresensimobile.riwayat

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView

interface RiwayatContract{
    interface Presenter: BasePresenter {
        fun onViewCreated()
    }

    interface View:
        BaseView<Presenter> {
    }
}