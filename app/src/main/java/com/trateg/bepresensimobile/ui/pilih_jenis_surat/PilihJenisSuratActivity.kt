package com.trateg.bepresensimobile.ui.pilih_jenis_surat

import android.content.Intent
import android.os.Bundle
import com.trateg.bepresensimobile.BaseActivity
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.ui.ajukan_surat.AjukanSuratActivity
import com.trateg.bepresensimobile.util.Constants
import kotlinx.android.synthetic.main.activity_pilih_jenis_surat.*

class PilihJenisSuratActivity : BaseActivity(), PilihJenisSuratContract.View {
    private var mPresenter: PilihJenisSuratContract.Presenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_jenis_surat)
        initView()
    }

    override fun initView() {
        title = "Ajukan Surat"
        showBackButton(true)
        cardDispen.setOnClickListener { goToAjukanSuratDispen() }
        cardIzin.setOnClickListener { goToAjukanSuratIzin() }
        cardSakit.setOnClickListener { goToAjukanSuratSakit() }
    }

    override fun goToAjukanSuratIzin() {
        val intent = Intent(baseContext, AjukanSuratActivity::class.java)
        intent.putExtra(Constants.ACTION_TYPE, Constants.SURAT_IZIN)
        startActivity(intent)
    }

    override fun goToAjukanSuratSakit() {
        val intent = Intent(baseContext, AjukanSuratActivity::class.java)
        intent.putExtra(Constants.ACTION_TYPE, Constants.SURAT_SAKIT)
        startActivity(intent)
    }

    override fun goToAjukanSuratDispen() {
        val intent = Intent(baseContext, AjukanSuratActivity::class.java)
        intent.putExtra(Constants.ACTION_TYPE, Constants.SURAT_DISPEN)
        startActivity(intent)
    }

    override fun showBackButton(enabled: Boolean) {
        if (supportActionBar != null) supportActionBar!!.setDisplayHomeAsUpEnabled(enabled)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun attachPresenter(presenter: PilihJenisSuratContract.Presenter) {
        mPresenter = presenter
    }

    override fun onDestroy() {
        super.onDestroy()
        detachPresenter()
    }

    override fun detachPresenter() {
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun showProgress() {
        // do nothing
    }

    override fun hideProgress() {
        // do nothing
    }
}