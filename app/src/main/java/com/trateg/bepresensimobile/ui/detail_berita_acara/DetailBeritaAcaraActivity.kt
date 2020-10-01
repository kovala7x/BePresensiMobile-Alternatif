package com.trateg.bepresensimobile.ui.detail_berita_acara

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.trateg.bepresensimobile.BaseActivity
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.model.BeritaAcara
import com.trateg.bepresensimobile.model.Jadwal
import com.trateg.bepresensimobile.util.Constants
import kotlinx.android.synthetic.main.activity_detail_berita_acara.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailBeritaAcaraActivity : BaseActivity(), DetailBeritaAcaraContract.View {

    private var mPresenter: DetailBeritaAcaraContract.Presenter? = null
    private var dataJadwal: Jadwal? = null
    private var dataBeritaAcara: BeritaAcara? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_berita_acara)
        attachPresenter(DetailBeritaAcaraPresenter(this))
        initView()
    }

    override fun initView() {
        showBackButton(true)
        title = "Detail Berita Acara"
        getIntentExtraData()
        showLayoutDetail(false)
        CoroutineScope(Dispatchers.Main).launch {
            dataBeritaAcara = mPresenter?.getDetailBeritaAcara(
                kdBeritaAcara = dataJadwal!!.beritaAcara?.kdBeritaAcara!!
            )
            if (dataBeritaAcara != null){
                mPresenter?.onGetDetailBeritaAcaraSuccess(dataBeritaAcara!!)
                showLayoutDetail(true)
            }
        }
        swipeRefreshDetailBeritaAcara.setOnRefreshListener {
            CoroutineScope(Dispatchers.Main).launch {
                dataBeritaAcara = mPresenter?.getDetailBeritaAcara(
                    kdBeritaAcara = dataJadwal!!.beritaAcara?.kdBeritaAcara!!
                )
                if (dataBeritaAcara != null){
                    mPresenter?.onGetDetailBeritaAcaraSuccess(dataBeritaAcara!!)
                    showLayoutDetail(true)
                }
            }
        }
    }

    override fun getIntentExtraData() {
        dataJadwal = intent.getParcelableExtra(Constants.DATA_JADWAL)
        if (dataJadwal == null) {
            showToast("Gagal mengambil data jadwal!")
            finish()
        }
    }

    override fun showLayoutDetail(show: Boolean) {
        if(show){
            layoutDetailBeritaAcara.visibility = View.VISIBLE
        }else{
            layoutDetailBeritaAcara.visibility = View.INVISIBLE
        }
    }

    override fun showSnackbar(msg: String) {
        Snackbar.make(swipeRefreshDetailBeritaAcara, msg, Snackbar.LENGTH_INDEFINITE)
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

    override fun setTextDate(date: String) {
        tvTanggal.text = date
    }

    override fun setCardData(data: BeritaAcara) {
        tvNamaMatakuliah.text =  data.jadwal!!.matakuliah!!.namaMatakuliah
        tvTanggal.text = mPresenter?.convertDate(data.tglPertemuan.toString())
        tvRuang.text = data.jadwal.kdRuang
        tvJumlahMhsHadir.text = data.mhsHadir.toString()
        tvJumlahMhsTidakHadir.text = data.mhsTidakHadir.toString()
        tvSesiDibuka.text = data.jamPresensiDibuka.toString().dropLast(3)
        tvSesiDitutup.text = data.jamPresensiDitutup.toString().dropLast(3)
        inputDeskTatapMuka.editText?.isEnabled = false
        inputDeskTatapMuka.editText?.setText(data.deskPerkuliahan)
        inputDeskPenugasan.editText?.isEnabled = false
        inputDeskPenugasan.editText?.setText(data.deskPenugasan)
    }

    override fun goToHome() {
        detachPresenter()
        finish()
    }

    override fun attachPresenter(presenter: DetailBeritaAcaraContract.Presenter) {
        mPresenter = presenter
    }

    override fun detachPresenter() {
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun showProgress() {
        swipeRefreshDetailBeritaAcara.let {
            it.isRefreshing = true
        }
    }

    override fun hideProgress() {
        swipeRefreshDetailBeritaAcara.let {
            it.isRefreshing = false
        }
    }
}