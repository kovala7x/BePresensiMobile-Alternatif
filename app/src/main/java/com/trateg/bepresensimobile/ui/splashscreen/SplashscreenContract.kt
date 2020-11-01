package com.trateg.bepresensimobile.ui.splashscreen

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView

interface SplashscreenContract {
    interface Presenter: BasePresenter
    interface View: BaseView<Presenter>{
        fun goToLoginActivity()
    }
}