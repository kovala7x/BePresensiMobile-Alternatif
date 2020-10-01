package com.trateg.bepresensimobile.ui.lihat_presensi_kelas

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView
import com.trateg.bepresensimobile.model.Jadwal
import com.trateg.bepresensimobile.model.PersentaseKehadiran

interface LihatPresensiKelasContract {
    interface Presenter : BasePresenter {
        suspend fun getListPersentaseKehadiran(kdJadwal: String): List<PersentaseKehadiran>?
        fun navigateHome()
    }

    interface View : BaseView<LihatPresensiKelasContract.Presenter> {
        fun initView()
        fun getIntentExtraData()
        fun showSnackbar(msg: String)
        fun showToast(msg: String)
        fun showBackButton(enabled: Boolean)
        fun onGetListPersentaseKehadiranSuccess(data: List<PersentaseKehadiran>)
        fun onSesiClicked(dtPersentase: PersentaseKehadiran, dtJadwal: Jadwal)
        fun setTextMatakuliah(matakuliah: String)
        fun goToHome()
    }
}