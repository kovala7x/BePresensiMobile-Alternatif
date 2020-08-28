package com.trateg.bepresensimobile.ui.ajukan_surat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.trateg.bepresensimobile.R
import kotlinx.android.synthetic.main.activity_ajukan_surat.*

class AjukanSuratActivity : AppCompatActivity(), AjukanSuratContract.View {

    private var mPresenter: AjukanSuratContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajukan_surat)
        attachPresenter(AjukanSuratPresenter(this))
        cardIzin.setOnClickListener {
            Toast.makeText(this,"Card Surat Izin dipilih!",Toast.LENGTH_SHORT).show()
        }
        cardSakit.setOnClickListener {
            Toast.makeText(this,"Card Surat Sakit dipilih!",Toast.LENGTH_SHORT).show()
        }
        cardDispen.setOnClickListener {
            Toast.makeText(this,"Card Surat Dispen dipilih!",Toast.LENGTH_SHORT).show()
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