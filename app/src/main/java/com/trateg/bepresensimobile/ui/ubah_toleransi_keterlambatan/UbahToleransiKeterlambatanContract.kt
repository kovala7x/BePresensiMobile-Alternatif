package com.trateg.bepresensimobile.ui.ubah_toleransi_keterlambatan

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView
import com.trateg.bepresensimobile.model.Jadwal

interface UbahToleransiKeterlambatanContract {
    interface Presenter: BasePresenter{
        suspend fun postUbahToleransiKeterlambatan(kdJadwal : String, toleransi: Int): Jadwal?
    }
    interface View: BaseView<Presenter>{
        fun initView()
        fun getIntentExtras()
        fun getSliderToleransi(): Int
        fun onPostUbahToleransiKeterlambatanSuccess(data: Jadwal)
        fun setCardData(data: Jadwal)
        fun setSliderToleransi(toleransi: Int)
        fun showBackButton(show: Boolean)
        fun showButtonSimpanPerubahan(show: Boolean)
        fun showToast(msg: String)
        fun showSnackbar(msg: String)
    }
}