package com.trateg.bepresensimobile.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.trateg.bepresensimobile.BaseFragment
import com.trateg.bepresensimobile.R
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.DateFormat
import java.util.*

class HomeFragment: BaseFragment(), HomeContract.View {
    private var mPresenter: HomeContract.Presenter? = null

    private lateinit var mRootView : View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.fragment_home, container, false)
        attachPresenter(HomePresenter(this))
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPresenter?.assignCurrentDate()
    }

    override fun onDestroyView() {
        detachPresenter()
        super.onDestroyView()
    }

    override fun updateTextDate(date: String) {
        tvTanggal.text = date
    }

    override fun attachPresenter(presenter: HomeContract.Presenter) {
        mPresenter = presenter
    }

    override fun detachPresenter() {
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

}