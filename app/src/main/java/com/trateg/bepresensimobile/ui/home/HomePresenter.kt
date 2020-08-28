package com.trateg.bepresensimobile.ui.home

import android.os.Bundle
import com.trateg.bepresensimobile.BaseFragment
import java.text.DateFormat
import java.util.*


class HomePresenter(var mView: HomeContract.View?) :
    HomeContract.Presenter {

    companion object{
        fun newInstance(args: Bundle? = null): BaseFragment {
            val fragment = HomeFragment()
            args?.let { fragment.arguments = it }
            return fragment
        }
    }

    override fun assignCurrentDate() {
        //Menampilkan hari, tanggal, dan tahun
        //Format menyesuaikan bahasa device
        val calendar: Calendar = Calendar.getInstance()
        val currentDate:String = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime())
        mView?.updateTextDate(currentDate)
    }

    override fun onDestroy() {
        mView = null
    }
}