package com.trateg.bepresensimobile.profil

import android.os.Bundle
import com.trateg.bepresensimobile.BaseFragment
import com.trateg.bepresensimobile.util.FragMan

class ProfilPresenter(var mView: ProfilContract.View?):
    ProfilContract.Presenter {
    companion object {
        const val KEY_TITLE: String = "KEY_TITLE"
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