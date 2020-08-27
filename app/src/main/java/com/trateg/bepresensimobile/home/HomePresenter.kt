package com.trateg.bepresensimobile.home

import android.os.Bundle
import com.trateg.bepresensimobile.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*
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

    override fun getCurrentDate(): String {
        //Menampilkan hari, tanggal, dan tahun
        //Format menyesuaikan bahasa device
        val calendar: Calendar = Calendar.getInstance()
        return DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime())
    }

    override fun onDestroy() {
        mView = null
    }
}