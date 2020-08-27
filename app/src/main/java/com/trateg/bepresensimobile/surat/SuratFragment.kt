package com.trateg.bepresensimobile.surat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.trateg.bepresensimobile.BaseFragment
import com.trateg.bepresensimobile.R
import kotlinx.android.synthetic.main.fragment_profil.*

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
        val arg = arguments?.getString(SuratPresenter.KEY_TITLE,"")
        text_title.text = arg
        if(arg?.contains("Root") != true) {
            mRootView.setBackgroundColor(ContextCompat.getColor(mRootView.context, R.color.colorPrimaryDark))
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