package com.trateg.bepresensimobile.home

import android.os.Bundle
import com.trateg.bepresensimobile.BaseFragment
import java.text.DateFormat
import java.util.*


class HomePresenter(var mView: HomeContract.View?) :
    HomeContract.Presenter {

    companion object{
        const val KEY_TITLE: String = "KEY_TITLE"

        fun newInstance(args: Bundle? = null): BaseFragment {
            val fragment = HomeFragment()
            args?.let { fragment.arguments = it }
            return fragment
        }
    }

//    override fun getCurrentDate(){
//        val calendar: Calendar = Calendar.getInstance()
//        val currentDate: String = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime())
//        textViewDate.setText(currentDate)
//    }

    override fun onViewCreated() {

    }

    override fun onDestroy() {
        mView = null
    }
}