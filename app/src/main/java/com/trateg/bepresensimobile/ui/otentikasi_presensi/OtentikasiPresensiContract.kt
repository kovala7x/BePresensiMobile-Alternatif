package com.trateg.bepresensimobile.ui.otentikasi_presensi

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView
import com.trateg.bepresensimobile.model.BaseResponse
import com.trateg.bepresensimobile.model.Beacon
import com.trateg.bepresensimobile.model.Jadwal
import com.trateg.bepresensimobile.util.SessionManager

class OtentikasiPresensiContract {
    interface Presenter: BasePresenter{
        fun setupSession()
        fun onUserAuthenticated(actionType: Int, data: Jadwal)
        fun bukaSesiPresensi(data: Jadwal)
        fun tutupSesiPresensi(data: Jadwal)
        fun catatPresensi(data: Jadwal)
    }

    interface View: BaseView<Presenter>{
        fun initView()
        fun getIntentExtraData()
        fun setActionBarTitle(type: Int)
        fun setLokasiPresensi(visible: Boolean, lokasi: String)
        fun setActionStatus(visible: Boolean, action: String)
        suspend fun scanBeacon(dataBeacon: Beacon): Boolean
        fun initAuthenticator()
        fun initSession(): SessionManager
        fun onError(msg: String)
        fun onBukaSesiPresensiSuccess(data: BaseResponse)
        fun onTutupSesiPresensiSuccess(data: BaseResponse)
        fun onCatatPresensiSuccess(data: BaseResponse)
        fun showBackButton(enabled: Boolean)
        fun showToast(msg: String)
        fun goToHome()
    }
}