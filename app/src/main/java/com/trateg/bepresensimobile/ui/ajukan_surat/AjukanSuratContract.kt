package com.trateg.bepresensimobile.ui.ajukan_surat

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView
import com.trateg.bepresensimobile.model.SuratIzin
import com.trateg.bepresensimobile.util.SessionManager
import java.util.*

interface AjukanSuratContract {

    interface Presenter: BasePresenter{
        fun setupSession()
        suspend fun ajukanSurat(
            kdJenisIzin: String,
            tglMulai: String,
            tglSelesai: String,
            jamMulai: String?,
            jamSelesai: String?,
            catatanSurat: String?
        ): SuratIzin?
        fun isWaktuValid(startTime: Calendar, endTime: Calendar): Boolean
    }

    interface View: BaseView<Presenter>{
        fun initView()
        fun initSession(): SessionManager
        fun isDataValid(): Boolean
        fun isFormValid(): Boolean
        fun getTglMulai(): String
        fun getTglSelesai(): String
        fun getJamMulai(): String
        fun getJamSelesai(): String
        fun getCatatan(): String
        fun onAjukanSuratSuccess(data: SuratIzin)
        fun onButtonAjukanSuratPressed()
        fun setupFormType()
        fun setupDatePicker()
        fun setupTimePicker()
        fun showBackButton(enabled: Boolean)
        fun showFormDispen()
        fun showFormIzin()
        fun showFormSakit()
        fun showFormWaktu(visible : Boolean)
        fun showToast(msg: String)
        fun showSnackbar(msg: String)
    }
}