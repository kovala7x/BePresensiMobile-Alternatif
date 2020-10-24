package com.trateg.bepresensimobile.ui.ajukan_surat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.util.Constants
import kotlinx.android.synthetic.main.activity_ajukan_surat.*

class AjukanSuratActivity : AppCompatActivity(), AjukanSuratContract.View {

    private var mPresenter: AjukanSuratContract.Presenter? = null
    private var actionType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajukan_surat)
        attachPresenter(AjukanSuratPresenter(this))
        initView()
    }

    override fun initView() {
        actionType = intent.getStringExtra(Constants.ACTION_TYPE)
        when(actionType){
            Constants.SURAT_IZIN ->{
                title = "Pengajuan Surat Izin"
                tvJenisSurat.text = title
            }
            Constants.SURAT_DISPEN -> {
                title ="Pengajuan Surat Dispen"
                tvJenisSurat.text = title
            }
            Constants.SURAT_SAKIT -> {
                title = "Pengajuan Surat Sakit"
                tvJenisSurat.text = title
            }
        }
    }

    override fun attachPresenter(presenter: AjukanSuratContract.Presenter) {
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