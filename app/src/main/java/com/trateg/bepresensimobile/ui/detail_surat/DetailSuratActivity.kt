package com.trateg.bepresensimobile.ui.detail_surat

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.trateg.bepresensimobile.BaseActivity
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.model.SuratIzin
import com.trateg.bepresensimobile.util.Constants
import com.trateg.bepresensimobile.util.SessionManager
import com.trateg.bepresensimobile.util.TextHelper
import kotlinx.android.synthetic.main.activity_detail_surat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailSuratActivity : BaseActivity(), DetailSuratContract.View {
    private var mPresenter: DetailSuratContract.Presenter? = null
    private var dataSurat: SuratIzin? = null
    private var detailSurat: SuratIzin? = null
    private var responsePersetujuan: SuratIzin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_surat)
        attachPresenter(DetailSuratPresenter(this))
        initView()
    }

    override fun onResume() {
        super.onResume()
        onSwipeRefresh()
    }

    override fun initView() {
        title = "Detail Surat"
        getIntentExtraData()
        swipeRefreshDetailSurat.isEnabled = true
        showBackButton(true)
        showApprovalLayout(false)
        showDetailLayout(false)
        mPresenter?.setupSession()
        swipeRefreshDetailSurat.setOnRefreshListener {
            onSwipeRefresh()
        }
    }

    override fun initSession(): SessionManager {
        return SessionManager(this)
    }

    override fun getIntentExtraData() {
        dataSurat = intent.getParcelableExtra(Constants.DATA_SURAT)
        if (dataSurat == null) {
            showToast("Gagal mengambil data surat!")
            finish()
        }
    }

    override fun getCatatanWaliDosen(): String {
        return inputCatatanWaliDosen?.editText?.text.toString()
    }

    override fun onSwipeRefresh() {
        CoroutineScope(Dispatchers.Main).launch {
            detailSurat = mPresenter?.getDetailSuratIzin(
                kdSuratIzin = dataSurat!!.kdSuratIzin!!
            )
            if (detailSurat != null) {
                mPresenter?.onGetDetailSuratIzinSuccess(detailSurat!!)
                showDetailLayout(true)
                if(mPresenter?.isDosen()!!) {
                    layoutDataMhs.visibility = View.VISIBLE
                    showApprovalLayout(show = dataSurat!!.kdStatusSurat?.equals(1)!!)
                }else{
                    layoutDataMhs.visibility = View.GONE
                    showApprovalLayout(show = false)
                }
            }
        }
    }

    override fun onPersetujuanButtonClicked(isApproved: Boolean) {
        val msg = if(isApproved){
            "Setujui surat ini?"
        }else{
            "Tolak surat ini?"
        }
        MaterialAlertDialogBuilder(this)
            .setTitle(msg)
            .setMessage("Persetujuan yang telah dilakukan tidak dapat dirubah.")
            .setPositiveButton("Ya") { it, which ->
                it.dismiss()
                CoroutineScope(Dispatchers.Main).launch {
                    responsePersetujuan = mPresenter?.postPersetujuanSurat(
                        isApproved = isApproved,
                        kdSuratIzin = detailSurat!!.kdSuratIzin!!,
                        catatanWaliDosen = getCatatanWaliDosen()
                    )
                    if (responsePersetujuan != null) {
                        mPresenter?.onPostPersetujuanSuratSuccess(responsePersetujuan!!)
                    }
                }            }
            .setNegativeButton("Tidak"){ it, which ->
                it.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    override fun showApprovalLayout(show: Boolean) {
        if (show) {
            inputCatatanWaliDosen.isEnabled = true
            layoutApprovalButton.visibility = View.VISIBLE
            btnSetuju.setOnClickListener {
                onPersetujuanButtonClicked(isApproved = true)
            }
            btnTolak.setOnClickListener {
                onPersetujuanButtonClicked(isApproved = false)
            }
        } else {
            inputCatatanWaliDosen.isEnabled = false
            layoutApprovalButton.visibility = View.GONE
        }
    }

    override fun showDetailLayout(show: Boolean) {
        if (show) {
            layoutDetailSurat.visibility = View.VISIBLE
        } else {
            layoutDetailSurat.visibility = View.GONE
        }
    }

    override fun showSnackbar(msg: String) {
        Snackbar.make(swipeRefreshDetailSurat, msg, Snackbar.LENGTH_INDEFINITE)
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
        goToHome()
        return super.onSupportNavigateUp()
    }

    override fun setCardData(data: SuratIzin) {
        tvNIM.text = data.mahasiswa?.nim
        tvNama.text = TextHelper.captEachWord(data.mahasiswa?.namaMahasiswa!!)
        tvJenisSurat.text = "SURAT " + data.jenisIzin?.keteranganPresensi
        tvKodeSurat.text = data.kdSuratIzin
        chipStatusSurat.text = data.statusSurat?.keteranganSurat
        tvTglMulai.text = mPresenter?.convertDate(data.tglMulai!!)
        tvTglSelesai.text = mPresenter?.convertDate(data.tglSelesai!!)
        tvJamMulai.text = data.jamMulai?.dropLast(3)
        tvJamBerakhir.text = data.jamSelesai?.dropLast(3)
        inputCatatan.editText?.setText(data.catatanSurat)
        inputCatatanWaliDosen.editText?.setText(data.catatanWaliDosen)
    }

    override fun goToHome() {
        detachPresenter()
        finish()
    }

    override fun attachPresenter(presenter: DetailSuratContract.Presenter) {
        mPresenter = presenter
    }

    override fun detachPresenter() {
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun showProgress() {
        swipeRefreshDetailSurat.isRefreshing = true
    }

    override fun hideProgress() {
        swipeRefreshDetailSurat.isRefreshing = false
    }
}