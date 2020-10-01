package com.trateg.bepresensimobile.ui.ubah_status_presensi

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView
import com.trateg.bepresensimobile.model.Kehadiran

interface UbahStatusPresensiContract {
    interface Presenter : BasePresenter {
        suspend fun simpanPerubahanStatusPresensi(kdKehadiran: Int, isHadir: Boolean): Kehadiran?
    }

    interface View : BaseView<UbahStatusPresensiContract.Presenter> {
        fun initView()
        fun getIntentExtraData()
        fun getStatusHadir(): Boolean
        fun showButtonSimpanPerubahan(show: Boolean)
        fun showStatusHadir(show: Boolean)
        fun showSnackbar(msg: String)
        fun showToast(msg: String)
        fun showBackButton(enabled: Boolean)
        fun onSimpanPerubahanStatusPresensiSuccess(data: Kehadiran)
        fun setNama(nama: String)
        fun setNim(NIM: String)
        fun setStatusHadir(isHadir: Boolean)
    }
}