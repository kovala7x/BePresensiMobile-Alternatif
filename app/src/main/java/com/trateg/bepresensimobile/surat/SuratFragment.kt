package com.trateg.bepresensimobile.surat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trateg.bepresensimobile.BaseFragment
import com.trateg.bepresensimobile.R

/**
 * Created by hanilozmen on 8/24/2019.
 */
class SuratFragment: BaseFragment(),
    SuratContract.View {
    private lateinit var mRootView : View
    private var mPresenter: SuratContract.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.fragment_ajukan_surat, container, false)
        attachPresenter(SuratPresenter(this))
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        detachPresenter()
        super.onDestroyView()
    }

    override fun attachPresenter(presenter: SuratContract.Presenter) {
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