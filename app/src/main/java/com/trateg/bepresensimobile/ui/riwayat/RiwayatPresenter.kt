package com.trateg.bepresensimobile.ui.riwayat

import android.os.Bundle
import com.trateg.bepresensimobile.BaseFragment

class RiwayatPresenter(var mView: RiwayatContract.View?) :
    RiwayatContract.Presenter {

    companion object{
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