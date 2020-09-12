package com.trateg.bepresensimobile.ui.login

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView
import com.trateg.bepresensimobile.model.Auth
import com.trateg.bepresensimobile.util.SessionManager


interface LoginContract {
    interface Presenter: BasePresenter{
        fun doLogin(email: String, password: String)
        fun onLoginSuccess(data: Auth)
        fun navigateHome()
        fun setupSession()
    }

    interface View: BaseView<Presenter>{
        fun initSession(): SessionManager
        fun getEmail(): String
        fun getPassword(): String
        fun onError(msg: String)
        fun isFormEmailValid(): Boolean
        fun isFormPaswordValid(): Boolean
        fun hideSoftKeyboard()
        fun goToHome()
    }
}