package com.trateg.bepresensimobile.ui.atur_toleransi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.trateg.bepresensimobile.BaseActivity
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.ui.main.MainContract
import com.trateg.bepresensimobile.ui.main.MainPresenter

class AturToleransiActivity : BaseActivity(), AturToleransiContract.View {

    private var mPresenter: AturToleransiContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atur_toleransi)
        attachPresenter(AturToleransiPresenter(this))
    }

    override fun finishActivity() {
        finish()
    }

    override fun attachPresenter(presenter: AturToleransiContract.Presenter) {
        mPresenter = presenter
    }

    override fun detachPresenter() {
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun showProgress() {
        //TODO("Not yet implemented")
    }

    override fun hideProgress() {
        //TODO("Not yet implemented")
    }
}