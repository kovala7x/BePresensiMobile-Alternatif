package com.trateg.bepresensimobile.ui.atur_toleransi

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView

interface AturToleransiContract {
    interface Presenter: BasePresenter {
        fun onBackPressed()
    }

    interface View: BaseView<Presenter> {
        fun finishActivity()
    }
}
