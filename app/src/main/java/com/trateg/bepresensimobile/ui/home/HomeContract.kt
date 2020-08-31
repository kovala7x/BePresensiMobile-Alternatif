package com.trateg.bepresensimobile.ui.home

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView
import com.trateg.bepresensimobile.model.Jadwal

interface HomeContract{
    interface Presenter: BasePresenter {
        fun getCurrentDate()
        fun getDataJadwal()
    }

    interface View: BaseView<Presenter> {
        fun initView()
        fun updateTextDate(date: String)
        fun onGetDataJadwalSuccess(jadwal: List<Jadwal>)
        fun onError(msg: String)
    }
}