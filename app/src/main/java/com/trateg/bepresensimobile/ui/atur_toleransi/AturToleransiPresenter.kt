package com.trateg.bepresensimobile.ui.atur_toleransi

class AturToleransiPresenter(private var mView: AturToleransiContract.View?) :
    AturToleransiContract.Presenter {
    override fun onBackPressed() {
        mView?.finishActivity()
    }

    override fun onDestroy() {
        mView = null
    }
}