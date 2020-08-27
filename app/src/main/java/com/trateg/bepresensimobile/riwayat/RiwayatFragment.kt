package com.trateg.bepresensimobile.riwayat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.trateg.bepresensimobile.BaseFragment
import com.trateg.bepresensimobile.R
import kotlinx.android.synthetic.main.fragment_home.*

class RiwayatFragment: BaseFragment(),
    RiwayatContract.View {
    private var mPresenter: RiwayatContract.Presenter? = null

    private lateinit var mRootView : View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.fragment_riwayat, container, false)
        attachPresenter(RiwayatPresenter(this))
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        detachPresenter()
        super.onDestroyView()
    }

    override fun attachPresenter(presenter: RiwayatContract.Presenter) {
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