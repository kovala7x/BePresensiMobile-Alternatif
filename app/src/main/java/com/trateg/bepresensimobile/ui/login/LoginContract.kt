package com.trateg.bepresensimobile.ui.login

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView


interface LoginContract {
    interface Presenter: BasePresenter{
    }

    interface View: BaseView<Presenter>{
    }
}