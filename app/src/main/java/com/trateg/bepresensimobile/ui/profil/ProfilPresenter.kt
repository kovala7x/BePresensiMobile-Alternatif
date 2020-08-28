package com.trateg.bepresensimobile.ui.profil

import android.os.Bundle
import com.trateg.bepresensimobile.BaseFragment

class ProfilPresenter(var mView: ProfilContract.View?):
    ProfilContract.Presenter {
    companion object {
        fun newInstance(args: Bundle? = null): BaseFragment {
            val fragment = ProfilFragment()
            args?.let {  fragment.arguments = it }
            return fragment
        }
    }

    override fun onDestroy() {
        mView = null
    }

    override fun onViewCreated() {
        // TODO do network calls, db operations etc.
    }
}