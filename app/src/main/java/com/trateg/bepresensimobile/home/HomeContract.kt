package com.trateg.bepresensimobile.home

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView

interface HomeContract{
    interface Presenter: BasePresenter {
        fun assignCurrentDate()
    }

    interface View: BaseView<Presenter> {
        fun updateTextDate(date: String)
    }
}