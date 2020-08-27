package com.trateg.bepresensimobile.riwayat

import android.os.Bundle
import com.trateg.bepresensimobile.BaseFragment
import com.trateg.bepresensimobile.util.FragMan

class RiwayatPresenter(var mView: RiwayatContract.View?) :
    RiwayatContract.Presenter {

    companion object{
        const val KEY_TITLE: String = "KEY_TITLE"
        fun newInstance(args: Bundle? = null): BaseFragment {
            val fragment = RiwayatFragment()
            args?.let { fragment.arguments = it }
            return fragment
        }
    }

    override fun onViewCreated() {
    }

    override fun onDestroy() {
        mView = null
    }
}