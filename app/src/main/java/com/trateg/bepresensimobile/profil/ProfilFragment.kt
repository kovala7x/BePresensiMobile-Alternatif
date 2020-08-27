package com.trateg.bepresensimobile.profil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.trateg.bepresensimobile.BaseFragment
import com.trateg.bepresensimobile.R
import kotlinx.android.synthetic.main.fragment_profil.*

class ProfilFragment: BaseFragment(),
    ProfilContract.View {
    private lateinit var mRootView : View
    private var mPresenter: ProfilContract.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.fragment_profil, container, false)
        attachPresenter(ProfilPresenter(this))
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arg = arguments?.getString(ProfilPresenter.KEY_TITLE,"")
        text_title.text = arg
        if(arg?.contains("Root") != true) {
            mRootView.setBackgroundColor(ContextCompat.getColor(mRootView.context, R.color.colorPrimaryDark))
        }
    }

    override fun onDestroyView() {
        detachPresenter()
        super.onDestroyView()
    }

    override fun attachPresenter(presenter: ProfilContract.Presenter) {
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