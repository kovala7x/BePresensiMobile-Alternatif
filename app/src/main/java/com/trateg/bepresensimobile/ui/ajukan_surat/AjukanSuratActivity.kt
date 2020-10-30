package com.trateg.bepresensimobile.ui.ajukan_surat

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.model.SuratIzin
import com.trateg.bepresensimobile.util.Constants
import com.trateg.bepresensimobile.util.SessionManager
import kotlinx.android.synthetic.main.activity_ajukan_surat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class AjukanSuratActivity : AppCompatActivity(), AjukanSuratContract.View {

    private var mPresenter: AjukanSuratContract.Presenter? = null
    private var kdJenisIzin: String? = null
    private var tanggalMulai = Calendar.getInstance()
    private var tanggalSelesai = Calendar.getInstance()
    private var jamMulai = Calendar.getInstance()
    private var jamSelesai = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajukan_surat)
        attachPresenter(AjukanSuratPresenter(this))
        initView()
    }

    override fun initView() {
        mPresenter?.setupSession()
        showBackButton(true)
        swipeRefreshAjukanSurat.isEnabled = false
        setupFormType()
        setupDatePicker()
        setupTimePicker()

        cbSepanjangHari.setOnCheckedChangeListener { _, checked ->
            showFormWaktu(!checked)
        }
        btnAjukanSurat.setOnClickListener {
            onButtonAjukanSuratPressed()
        }
    }

    override fun initSession(): SessionManager {
        return SessionManager(baseContext)
    }

    override fun isDataValid(): Boolean {
        var isValid = false
        // Validasi kelengkapan form
        if (isFormValid()) {
            // Validasi tanggal izin
            if (mPresenter?.isWaktuValid(tanggalMulai, tanggalSelesai)!!) {
                // Cek apakah tidak sepanjang hari
                if (!cbSepanjangHari.isChecked) {
                    // Validasi waktu izin
                    if (mPresenter?.isWaktuValid(jamMulai, jamSelesai)!!) {
                        // Simpan surat dengan batasan waktu tertentu
                        // Surat Izin, Dispen
                        isValid = true
                    } else {
                        showToast("Waktu izin tidak valid!")
                        inputJamMulai.error = "Waktu tidak valid!"
                        inputJamSelesai.error = "Waktu tidak valid!"
                    }
                } else {
                    // Simpan Surat untuk izin sepanjang hari
                    // Surat Sakit, Izin, Dispen
                    isValid = true
                }
            } else {
                showToast("Tanggal izin tidak valid!")
                inputTglMulai.error = "Tanggal tidak valid!"
                inputTglSelesai.error = "Tanggal tidak valid!"
            }
        } else {
            showToast("Periksa kembali form!")
        }
        return isValid
    }

    override fun isFormValid(): Boolean {
        var isFormValid = true
        when {
            getTglMulai().isEmpty() -> {
                inputTglMulai.error = "Tidak boleh kosong!"
                isFormValid = false
            }
            getTglSelesai().isEmpty() -> {
                inputTglSelesai.error = "Tidak boleh kosong!"
                isFormValid = false
            }
            getCatatan().length > 255 -> {
                isFormValid = false
            }
        }
        if (!cbSepanjangHari.isChecked) {
            when {
                getJamMulai().isEmpty() -> {
                    inputJamMulai.error = "Tidak boleh kosong!"
                    isFormValid = false
                }
                getJamSelesai().isEmpty() -> {
                    inputJamSelesai.error = "Tidak boleh kosong!"
                    isFormValid = false
                }
            }
        }
        return isFormValid
    }

    override fun getTglMulai(): String {
        return inputTglMulai.editText?.text.toString()
    }

    override fun getTglSelesai(): String {
        return inputTglSelesai.editText?.text.toString()
    }

    override fun getJamMulai(): String {
        return inputJamMulai.editText?.text.toString()
    }

    override fun getJamSelesai(): String {
        return inputJamSelesai.editText?.text.toString()
    }

    override fun getCatatan(): String {
        return inputCatatan.editText?.text.toString()
    }

    override fun onAjukanSuratSuccess(data: SuratIzin) {
        showToast("Berhasil mengajukan surat!")
        detachPresenter()
        finish()
    }

    override fun onButtonAjukanSuratPressed() {
        if (isDataValid()) {
            MaterialAlertDialogBuilder(this)
                .setTitle("Ajukan surat?")
                .setMessage("Pastikan data yang Anda masukan sudah tepat.")
                .setPositiveButton("YA") { dialog, which ->
                    dialog.dismiss()
                    CoroutineScope(Dispatchers.Main).launch {
                        val response: SuratIzin? = mPresenter?.ajukanSurat(
                            kdJenisIzin = kdJenisIzin!!,
                            tglMulai = getTglMulai(),
                            tglSelesai = getTglSelesai(),
                            jamMulai = getJamMulai(),
                            jamSelesai = getJamSelesai(),
                            catatanSurat = getCatatan()
                        )
                        if (response != null) {
                            onAjukanSuratSuccess(response)
                        }
                    }
                }
                .setNegativeButton("TIDAK") { dialog, which ->
                    dialog.dismiss()
                }
                .setCancelable(false)
                .show()
        }
    }

    override fun setupFormType() {
        kdJenisIzin = intent.getStringExtra(Constants.ACTION_TYPE)
        when (kdJenisIzin) {
            Constants.SURAT_IZIN -> {
                showFormIzin()
            }
            Constants.SURAT_DISPEN -> {
                showFormDispen()
            }
            Constants.SURAT_SAKIT -> {
                showFormSakit()
            }
        }
    }

    override fun setupDatePicker() {
        val minDate: Calendar = Calendar.getInstance()
        minDate.add(Calendar.DAY_OF_YEAR, -7)
        inputTglMulai.editText?.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    inputTglMulai.error = null
                    tanggalMulai.set(year, month, dayOfMonth)
                    inputTglMulai.editText?.setText("$year-${month + 1}-$dayOfMonth")
                },
                tanggalMulai.get(Calendar.YEAR),
                tanggalMulai.get(Calendar.MONDAY),
                tanggalMulai.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate = minDate.timeInMillis
            datePickerDialog.show()
        }

        inputTglSelesai.editText?.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { datePicker, year, month, dayOfMonth ->
                    inputTglSelesai.error = null
                    tanggalSelesai.set(year, month, dayOfMonth)
                    inputTglSelesai.editText?.setText("$year-${month + 1}-$dayOfMonth")
                },
                tanggalSelesai.get(Calendar.YEAR),
                tanggalSelesai.get(Calendar.MONDAY),
                tanggalSelesai.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate = tanggalMulai.timeInMillis
            datePickerDialog.show()
        }
    }

    override fun setupTimePicker() {
        inputJamMulai.editText?.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                this,
                { p0, hourOfDay, minute ->
                    inputJamMulai.error = null
                    jamMulai.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    jamMulai.set(Calendar.MINUTE, minute)
                    val hours = if (hourOfDay < 10) {
                        "0$hourOfDay"
                    } else {
                        "$hourOfDay"
                    }
                    val minutes = if (minute < 10) {
                        ":0$minute:00"
                    } else {
                        ":$minute:00"
                    }
                    inputJamMulai.editText?.setText(hours + minutes)
                },
                jamMulai.get(Calendar.HOUR_OF_DAY),
                jamMulai.get(Calendar.MINUTE),
                true
            )
            timePickerDialog.show()
        }

        inputJamSelesai.editText?.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                this,
                { p0, hourOfDay, minute ->
                    inputJamSelesai.error = null
                    jamSelesai.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    jamSelesai.set(Calendar.MINUTE, minute)
                    val hours = if (hourOfDay < 10) {
                        "0$hourOfDay"
                    } else {
                        "$hourOfDay"
                    }
                    val minutes = if (minute < 10) {
                        ":0$minute:00"
                    } else {
                        ":$minute:00"
                    }
                    inputJamSelesai.editText?.setText(hours + minutes)
                },
                jamSelesai.get(Calendar.HOUR_OF_DAY),
                jamSelesai.get(Calendar.MINUTE),
                true
            )
            timePickerDialog.show()
        }
    }

    override fun showBackButton(enabled: Boolean) {
        if (supportActionBar != null) supportActionBar!!.setDisplayHomeAsUpEnabled(enabled)
    }

    override fun onSupportNavigateUp(): Boolean {
        detachPresenter()
        finish()
        return super.onSupportNavigateUp()
    }

    override fun showFormDispen() {
        title = "Pengajuan Surat Dispen"
    }

    override fun showFormIzin() {
        title = "Pengajuan Surat Izin"
    }

    override fun showFormSakit() {
        title = "Pengajuan Surat Sakit"
        cbSepanjangHari.isChecked = true
        cbSepanjangHari.visibility = View.GONE
        showFormWaktu(false)
    }

    override fun showFormWaktu(visible: Boolean) {
        if (visible) {
            inputJamMulai.visibility = View.VISIBLE
            inputJamSelesai.visibility = View.VISIBLE
        } else {
            inputJamMulai.visibility = View.GONE
            inputJamSelesai.visibility = View.GONE
        }
    }

    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun showSnackbar(msg: String) {
        Snackbar.make(swipeRefreshAjukanSurat, msg, Snackbar.LENGTH_INDEFINITE)
            .setAction("TUTUP") {
                // tutup snackbar
                // biarkan kosong
            }.show()
    }

    override fun attachPresenter(presenter: AjukanSuratContract.Presenter) {
        mPresenter = presenter
    }

    override fun detachPresenter() {
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun showProgress() {
        swipeRefreshAjukanSurat.isEnabled = true
        swipeRefreshAjukanSurat.isRefreshing = true
    }

    override fun hideProgress() {
        swipeRefreshAjukanSurat.isRefreshing = false
        swipeRefreshAjukanSurat.isEnabled = false
    }
}