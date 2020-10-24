package com.trateg.bepresensimobile.ui.pilih_jenis_surat

class PilihJenisSuratPresenter(private var mView: PilihJenisSuratContract.View?) :
    PilihJenisSuratContract.Presenter {
    override fun onDestroy() {
        mView = null
    }
}