package com.trateg.bepresensimobile.ui.main

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.trateg.bepresensimobile.BaseActivity
import com.trateg.bepresensimobile.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MainContract.View {

    private var mPresenter: MainContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        attachPresenter(MainPresenter(this))
        mPresenter?.onViewCreated(savedInstanceState != null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mPresenter?.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        mPresenter?.onRestoreInstanceState(savedInstanceState)
    }

    override fun onDestroy() {
        detachPresenter()
        super.onDestroy()
    }

    override fun onBackPressed() {
        mPresenter?.onBackPressed()
    }

    override fun attachPresenter(presenter: MainContract.Presenter) {
        mPresenter = presenter
    }

    override fun detachPresenter() {
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun finishActivity() {
        finish()
    }

    override fun getMainFragmentManager(): FragmentManager = supportFragmentManager

    override fun getFragmentHolderId(): Int = R.id.container_main

    override fun getBottomNavigation(): BottomNavigationView = view_navigation

    override fun showProgress() {}

    override fun hideProgress() {}


}
