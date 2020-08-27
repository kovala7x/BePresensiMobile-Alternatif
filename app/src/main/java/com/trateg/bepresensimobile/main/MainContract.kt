package com.trateg.bepresensimobile.main

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView

interface MainContract {
    interface Presenter: BasePresenter {
        fun onViewCreated(isRestored: Boolean)
        fun onBackPressed()
        fun onSaveInstanceState(outState: Bundle)
        fun onRestoreInstanceState(savedInstanceState: Bundle)
    }

    interface View: BaseView<Presenter> {
        fun getFragmentHolderId(): Int
        fun getMainFragmentManager(): FragmentManager
        fun getBottomNavigation(): BottomNavigationView
        fun finishActivity()
    }
}
