package com.trateg.bepresensimobile.ui.login

import com.trateg.bepresensimobile.BaseActivity

class LoginPresenter(private var mView: LoginContract.View?):
    LoginContract.Presenter{

    override fun onDestroy() {
        mView = null
    }
}