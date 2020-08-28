package com.trateg.bepresensimobile.ajukan_surat

class AjukanSuratPresenter(private var mView: AjukanSuratContract.View) :
    AjukanSuratContract.Presenter {
    override fun onDestroy() {
        mView.detachPresenter()
    }
}