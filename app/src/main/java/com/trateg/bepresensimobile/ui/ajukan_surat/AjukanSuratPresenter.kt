package com.trateg.bepresensimobile.ui.ajukan_surat

class AjukanSuratPresenter(private var mView: AjukanSuratContract.View) :
    AjukanSuratContract.Presenter {
    override fun onDestroy() {
        mView.detachPresenter()
    }
}