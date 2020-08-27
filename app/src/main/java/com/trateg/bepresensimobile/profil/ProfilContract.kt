package com.trateg.bepresensimobile.profil

import com.trateg.bepresensimobile.BasePresenter
import com.trateg.bepresensimobile.BaseView

interface ProfilContract{
    interface Presenter: BasePresenter {
        fun onViewCreated()
    }

    interface View:
        BaseView<Presenter> {
    }
}