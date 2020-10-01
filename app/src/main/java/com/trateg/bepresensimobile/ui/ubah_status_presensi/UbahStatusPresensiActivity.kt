package com.trateg.bepresensimobile.ui.ubah_status_presensi

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.trateg.bepresensimobile.BaseActivity
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.model.Kehadiran
import com.trateg.bepresensimobile.util.Constants
import kotlinx.android.synthetic.main.activity_ubah_status_presensi.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class UbahStatusPresensiActivity : BaseActivity(), UbahStatusPresensiContract.View {

    private var initialHadirValue: Boolean = false
    private var mPresenter: UbahStatusPresensiContract.Presenter? = null
    private var dataKehadiran: Kehadiran? = null
    private var responseData: Kehadiran? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ubah_status_presensi)
        attachPresenter(UbahStatusPresensiPresenter(this))
        initView()
    }

    override fun initView() {
        showBackButton(true)
        showButtonSimpanPerubahan(false)
        title = "Ubah Status Presensi"
        getIntentExtraData()
        setNama(dataKehadiran!!.mahasiswa?.namaMahasiswa ?: "ErrNull")
        setNim(dataKehadiran!!.mahasiswa?.nim ?: "ErrNull")
        when (dataKehadiran!!.statusPresensi?.keteranganPresensi!!.toUpperCase(Locale.ROOT)) {
            "HADIR" -> {
                showStatusHadir(true)
                setStatusHadir(true)
                initialHadirValue = true
            }
            "ALFA" -> {
                showStatusHadir(true)
                setStatusHadir(false)
                initialHadirValue = false
            }
            else -> {
                showStatusHadir(false)
                setStatusHadir(false)
                initialHadirValue = false
            }
        }
        swipeRefreshUbahStatusPresensi.isEnabled = false
        switchHadir.setOnCheckedChangeListener { compoundButton, b ->
            if(initialHadirValue == getStatusHadir()){
                showButtonSimpanPerubahan(false)
            }else{
                showButtonSimpanPerubahan(true)
            }
        }
        btnSimpanPerubahan.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                responseData = mPresenter?.simpanPerubahanStatusPresensi(
                    kdKehadiran = dataKehadiran!!.kdKehadiran!!,
                    isHadir = getStatusHadir()
                )
                if (responseData != null) {
                    onSimpanPerubahanStatusPresensiSuccess(responseData!!)
                }
            }
        }
    }

    override fun getIntentExtraData() {
        dataKehadiran = intent.getParcelableExtra(Constants.DATA_KEHADIRAN)
        if (dataKehadiran == null) {
            showToast("Gagal mengambil data intent!")
            finish()
        }
    }

    override fun getStatusHadir(): Boolean {
        return switchHadir.isChecked
    }

    override fun showButtonSimpanPerubahan(show: Boolean) {
        if (show) {
            btnSimpanPerubahan.visibility = View.VISIBLE
        } else {
            btnSimpanPerubahan.visibility = View.GONE
        }
    }

    override fun showStatusHadir(show: Boolean) {
        if (show) {
            layoutSwitchHadir.visibility = View.VISIBLE
        } else {
            layoutSwitchHadir.visibility = View.GONE
        }
    }

    override fun showSnackbar(msg: String) {
        Snackbar.make(swipeRefreshUbahStatusPresensi, msg, Snackbar.LENGTH_INDEFINITE)
            .setAction("TUTUP") {
                // tutup snackbar
                // biarkan kosong
            }.show()
    }

    override fun showToast(msg: String) {
        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun showBackButton(enabled: Boolean) {
        if (supportActionBar != null) supportActionBar!!.setDisplayHomeAsUpEnabled(enabled)
    }

    override fun onSupportNavigateUp(): Boolean {
        detachPresenter()
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onSimpanPerubahanStatusPresensiSuccess(data: Kehadiran) {
        showToast("Berhasil merubah status presensi ${data.mahasiswa?.namaMahasiswa} !")
        detachPresenter()
        finish()
    }

    override fun setNama(nama: String) {
        tvNama.text = nama
    }

    override fun setNim(NIM: String) {
        tvNIM.text = NIM
    }

    override fun setStatusHadir(isHadir: Boolean) {
        switchHadir.isChecked = isHadir
    }

    override fun attachPresenter(presenter: UbahStatusPresensiContract.Presenter) {
        mPresenter = presenter
    }

    override fun detachPresenter() {
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun showProgress() {
        swipeRefreshUbahStatusPresensi.let {
            it.isEnabled = true
            it.isRefreshing = true
        }
    }

    override fun hideProgress() {
        swipeRefreshUbahStatusPresensi.let {
            it.isEnabled = false
            it.isRefreshing = false
        }
    }
}