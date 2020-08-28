package com.trateg.bepresensimobile.ui.surat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trateg.bepresensimobile.BaseFragment
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.ui.ajukan_surat.AjukanSuratActivity
import kotlinx.android.synthetic.main.fragment_surat.*

/**
 * Created by hanilozmen on 8/24/2019.
 */
class SuratFragment: BaseFragment(),
    SuratContract.View {
    private lateinit var mRootView : View
    private var mPresenter: SuratContract.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.fragment_surat, container, false)
        attachPresenter(SuratPresenter(this))
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabSuratBaru.setOnClickListener {
            startActivity(Intent(it.context,AjukanSuratActivity::class.java))
        }
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