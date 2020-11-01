package com.trateg.bepresensimobile.ui.splashscreen

class SplashScreenPresenter(var mView : SplashscreenContract.View?) :
    SplashscreenContract.Presenter {
    override fun onDestroy() {
        mView = null
    }
}