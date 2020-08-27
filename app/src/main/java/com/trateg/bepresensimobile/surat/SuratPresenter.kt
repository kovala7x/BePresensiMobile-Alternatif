package com.trateg.bepresensimobile.surat

import android.os.Bundle
import com.trateg.bepresensimobile.BaseFragment
import com.trateg.bepresensimobile.util.FragMan

class SuratPresenter(var mView: SuratContract.View?):
    SuratContract.Presenter {
    companion object {
        fun newInstance(args: Bundle? = null): BaseFragment {
            val fragment = SuratFragment()
            args?.let {  fragment.arguments = it }
            return fragment
        }
    }

    override fun onDestroy() {
        mView = null
    }

    override fun onViewCreated() {

    }
}