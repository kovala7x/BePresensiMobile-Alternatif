package com.trateg.bepresensimobile.ui.ubah_toleransi_keterlambatan

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.trateg.bepresensimobile.BaseActivity
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.model.Jadwal
import com.trateg.bepresensimobile.util.Constants
import kotlinx.android.synthetic.main.activity_ubah_toleransi_keterlambatan.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UbahToleransiKeterlambatanActivity : BaseActivity(), UbahToleransiKeterlambatanContract.View {
    private var mPresenter: UbahToleransiKeterlambatanContract.Presenter? = null
    private var dataJadwal: Jadwal? = null
    private var responseJadwal: Jadwal? = null
    private var initialValue = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ubah_toleransi_keterlambatan)
        attachPresenter(UbahToleransiKeterlambatanPresenter(this))
        initView()
    }

    override fun initView() {
        title = "Ubah Toleransi Keterlambatan"
        showBackButton(true)
        showButtonSimpanPerubahan(false)
        getIntentExtras()
        setCardData(dataJadwal!!)
        swipeRefreshUbahToleransi.isEnabled = false
        sliderToleransi.addOnChangeListener { slider, value, fromUser ->
            tvToleransiTelat.text = "$value menit"
            showButtonSimpanPerubahan(value.toInt() != initialValue)
        }
        btnSimpanPerubahan.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                responseJadwal = mPresenter?.postUbahToleransiKeterlambatan(
                    kdJadwal = dataJadwal?.kdJadwal!!,
                    toleransi = getSliderToleransi()
                )
                if (responseJadwal != null) {
                    onPostUbahToleransiKeterlambatanSuccess(responseJadwal!!)
                }
            }
        }
    }

    override fun getIntentExtras() {
        dataJadwal = intent.getParcelableExtra(Constants.DATA_JADWAL)
        if (dataJadwal == null) {
            showToast("Gagal mengambil data jadwal!")
            finish()
        }
    }

    override fun getSliderToleransi(): Int {
        return sliderToleransi.value.toInt()
    }

    override fun onPostUbahToleransiKeterlambatanSuccess(data: Jadwal) {
        detachPresenter()
        finish()
    }

    override fun setCardData(data: Jadwal) {
        tvNamaMatakuliah.text = data.matakuliah?.namaMatakuliah
        tvKelas.text = data.kdKelas
        tvJenisPerkuliahan.text = data.jenisPerkuliahan
        tvHariPerkuliahan.text = data.hari?.namaHari
        tvKdRuangan.text = data.ruang?.kdRuang
        tvJamMulai.text = data.sesiMulai?.jamMulai?.dropLast(3)
        tvJamBerakhir.text = data.sesiBerakhir?.jamBerakhir?.dropLast(3)
        tvToleransiTelat.text = data.toleransiKeterlambatan.toString() + " menit"
        setSliderToleransi(data.toleransiKeterlambatan!!)
        initialValue = data.toleransiKeterlambatan
    }


    override fun setSliderToleransi(toleransi: Int) {
        sliderToleransi.value = toleransi.toFloat()
    }

    override fun showBackButton(show: Boolean) {
        if (supportActionBar != null) supportActionBar!!.setDisplayHomeAsUpEnabled(show)
    }

    override fun onSupportNavigateUp(): Boolean {
        detachPresenter()
        finish()
        return super.onSupportNavigateUp()
    }

    override fun showButtonSimpanPerubahan(show: Boolean) {
        if (show) {
            btnSimpanPerubahan.visibility = View.VISIBLE
        } else {
            btnSimpanPerubahan.visibility = View.GONE
        }
    }

    override fun showToast(msg: String) {
        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun showSnackbar(msg: String) {
        Snackbar.make(swipeRefreshUbahToleransi, msg, Snackbar.LENGTH_INDEFINITE)
            .setAction("TUTUP") {
                // tutup snackbar
                // biarkan kosong
            }.show()
    }

    override fun attachPresenter(presenter: UbahToleransiKeterlambatanContract.Presenter) {
        mPresenter = presenter
    }

    override fun detachPresenter() {
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun showProgress() {
        swipeRefreshUbahToleransi.let {
            it.isEnabled = true
            it.isRefreshing = true
        }
    }

    override fun hideProgress() {
        swipeRefreshUbahToleransi.let {
            it.isRefreshing = false
            it.isEnabled = false
        }
    }
}