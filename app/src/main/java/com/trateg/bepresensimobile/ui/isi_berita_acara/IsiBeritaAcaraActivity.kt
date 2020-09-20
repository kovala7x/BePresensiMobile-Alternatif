package com.trateg.bepresensimobile.ui.isi_berita_acara

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.trateg.bepresensimobile.BaseActivity
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.model.BeritaAcara
import com.trateg.bepresensimobile.model.Jadwal
import com.trateg.bepresensimobile.util.Constants
import com.trateg.bepresensimobile.util.SessionManager
import kotlinx.android.synthetic.main.activity_isi_berita_acara.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IsiBeritaAcaraActivity : BaseActivity(), IsiBeritaAcaraContract.View {

    private var mPresenter: IsiBeritaAcaraContract.Presenter? = null
    private var dataJadwal: Jadwal? = null
    private var dataBeritaAcara: BeritaAcara? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_isi_berita_acara)
        attachPresenter(IsiBeritaAcaraPresenter(this))
        initView()
    }

    override fun initView() {
        enableInputForm(true)
        showBackButton(true)
        title = "Isi Berita Acara"
        mPresenter?.setupSession()
        mPresenter?.getCurrentDate()
        getIntentExtraData()
        setCardData(dataJadwal!!)
        btnSimpanBeritaAcara.setOnClickListener {
            hideSoftKeyboard()
            if (isFormValid()) {
                CoroutineScope(Dispatchers.Main).launch{
                    dataBeritaAcara = mPresenter?.simpanBeritaAcara(
                        data = dataJadwal!!,
                        deskTatapMuka = getDeskTatapMuka(),
                        deskPenugasan = getDeskPenugasan()
                    )
                    if(dataBeritaAcara!=null) mPresenter?.onSimpanBeritaAcaraSuccess(dataBeritaAcara!!)
                }
            } else {
                Snackbar.make(it, "Periksa kembali form!", Snackbar.LENGTH_SHORT).show()
            }
        }

        inputDeskTatapMuka.editText?.doOnTextChanged { text, start, before, count ->
            inputDeskTatapMuka.error = null
        }

        inputDeskPenugasan.editText?.doOnTextChanged { text, start, before, count ->
            inputDeskPenugasan.error = null
        }

        swipeRefreshBeritaAcara.isEnabled = false
    }

    override fun initSession(): SessionManager {
        return SessionManager(this)
    }

    override fun setCardData(data: Jadwal) {
        tvNamaMatakuliah.text = data.matakuliah?.namaMatakuliah
        tvRuang.text = data.ruang?.kdRuang
        tvSesiDibuka.text = data.jamPresensiDibuka?.dropLast(3)
        tvSesiDitutup.text = data.jamPresensiDitutup?.dropLast(3)
    }

    override fun getIntentExtraData() {
        dataJadwal = intent.getParcelableExtra(Constants.DATA_JADWAL)
        if (dataJadwal == null) {
            showToast("Gagal mengambil data jadwal!")
            finish()
        }
    }

    override fun getDeskTatapMuka(): String {
        return inputDeskTatapMuka.editText?.text.toString()
    }

    override fun getDeskPenugasan(): String {
        return inputDeskPenugasan.editText?.text.toString()
    }

    override fun showDialog(title: String, msg: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton("OKE") { it, which ->
                it.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    override fun showSnackbar(msg: String) {
        Snackbar.make(swipeRefreshBeritaAcara, msg, Snackbar.LENGTH_INDEFINITE)
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
        if(getDeskPenugasan().isNotEmpty() || getDeskTatapMuka().isNotEmpty()){
            MaterialAlertDialogBuilder(this)
                .setTitle("Perhatian")
                .setMessage("Apakah yakin keluar dan batalkan perubahan?")
                .setPositiveButton("Ya") { it, which ->
                    it.dismiss()
                    goToHome()
                }
                .setNegativeButton("Tidak"){ it, which ->
                    it.dismiss()
                }
                .setCancelable(false)
                .show()
        }else{
            goToHome()
        }
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if(getDeskPenugasan().isNotEmpty() || getDeskTatapMuka().isNotEmpty()){
            MaterialAlertDialogBuilder(this)
                .setTitle("Perhatian")
                .setMessage("Apakah yakin keluar dan batalkan perubahan?")
                .setPositiveButton("Ya") { it, which ->
                    it.dismiss()
                    super.onBackPressed()
                }
                .setNegativeButton("Tidak"){ it, which ->
                    it.dismiss()
                }
                .setCancelable(false)
                .show()
        }else{
            super.onBackPressed()
        }
    }

    override fun isFormValid(): Boolean {
        var valid = true
        inputDeskTatapMuka.error = null
        inputDeskPenugasan.error = null
        if (getDeskTatapMuka().isEmpty()) {
            // jika input deskripsi tatap muka kosong
            valid = false
            inputDeskTatapMuka.error = "Deskripsi tidak boleh kosong!"
        }else if(getDeskTatapMuka().length>255){
            // jika input deskripsi tatap muka lebih dari 255 karakter
            valid = false
            inputDeskTatapMuka.error = "Tidak boleh lebih dari 255 karakter!"
        }
        if (getDeskPenugasan().isEmpty()) {
            // jika input deskripsi penugasan kosong
            valid = false
            inputDeskPenugasan.error = "Deskripsi tidak boleh kosong!"
        }else if(getDeskPenugasan().length>255){
            // jika input deskripsi tatap muka lebih dari 255 karakter
            valid = false
            inputDeskPenugasan.error = "Tidak boleh lebih dari 255 karakter!"
        }
        return valid
    }

    override fun setTextDate(date: String) {
        tvTanggal.text = date
    }

    override fun hideSoftKeyboard() {
        val view = this.currentFocus
        view?.let { v ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    override fun enableInputForm(enabled: Boolean) {
        inputDeskPenugasan.editText?.isEnabled = enabled
        inputDeskTatapMuka.editText?.isEnabled = enabled
    }

    override fun goToHome() {
        detachPresenter()
        finish()
    }

    override fun attachPresenter(presenter: IsiBeritaAcaraContract.Presenter) {
        mPresenter = presenter
    }

    override fun detachPresenter() {
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun showProgress() {
        enableInputForm(false)
        btnSimpanBeritaAcara.isEnabled = false
        swipeRefreshBeritaAcara.let {
            it.isEnabled = true
            it.isRefreshing = true
        }
    }

    override fun hideProgress() {
        enableInputForm(true)
        btnSimpanBeritaAcara.isEnabled = true
        swipeRefreshBeritaAcara.let {
            it.isRefreshing = false
            it.isEnabled = false
        }
    }
}