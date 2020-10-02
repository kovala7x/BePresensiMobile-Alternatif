package com.trateg.bepresensimobile.ui.detail_presensi_mahasiswa

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.trateg.bepresensimobile.BaseActivity
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.model.Jadwal
import com.trateg.bepresensimobile.model.Kehadiran
import com.trateg.bepresensimobile.util.Constants
import kotlinx.android.synthetic.main.activity_detail_presensi_mahasiswa.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailPresensiMahasiswaActivity : BaseActivity(), DetailPresensiMahasiswaContract.View {

    private var mPresenter: DetailPresensiMahasiswaContract.Presenter? = null
    private var dataJadwal: Jadwal? = null
    private var dataKehadiran: Kehadiran? = null
    private var dataDetailKehadiran: Kehadiran? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_presensi_mahasiswa)
        attachPresenter(DetailPresensiMahasiswaPresenter(this))
        initView()
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Main).launch {
            dataDetailKehadiran = mPresenter?.getDetailPresensiMahasiswa(
                kdKehadiran = dataKehadiran!!.kdKehadiran!!
            )
            if (dataDetailKehadiran != null){
                onGetDetailPresensiMahasiswaSuccess(dataDetailKehadiran!!)
                showLayoutDetail(true)
            }
        }
    }

    override fun initView() {
        showBackButton(true)
        title = "Detail Kehadiran"
        getIntentExtraData()
        showLayoutDetail(false)
        swipeRefreshDetailPresensiMahasiswa.setOnRefreshListener {
            CoroutineScope(Dispatchers.Main).launch {
                dataDetailKehadiran = mPresenter?.getDetailPresensiMahasiswa(
                    kdKehadiran = dataKehadiran!!.kdKehadiran!!
                )
                if (dataDetailKehadiran != null){
                    onGetDetailPresensiMahasiswaSuccess(dataDetailKehadiran!!)
                    showLayoutDetail(true)
                }
            }
        }
        cardKdSuratIzin.setOnClickListener { onCardSuratIzinClicked() }
    }

    override fun getIntentExtraData() {
        dataJadwal = intent.getParcelableExtra(Constants.DATA_JADWAL)
        dataKehadiran = intent.getParcelableExtra(Constants.DATA_KEHADIRAN)
        if (dataJadwal == null || dataKehadiran == null) {
            showToast("Gagal mengambil data intent!")
            finish()
        }
    }

    override fun onGetDetailPresensiMahasiswaSuccess(data: Kehadiran) {
        setCardData(data)
    }

    override fun onCardSuratIzinClicked() {
        //TODO("Not yet implemented")
        showToast("Surat izin " +tvKdSuratIzin.text.toString() + " dipanggil!")
    }

    override fun showLayoutDetail(show: Boolean) {
        if (show) {
            layoutDetailPresensiMahasiswa.visibility = View.VISIBLE
        } else {
            layoutDetailPresensiMahasiswa.visibility = View.INVISIBLE
        }
    }

    override fun showSnackbar(msg: String) {
        Snackbar.make(swipeRefreshDetailPresensiMahasiswa, msg, Snackbar.LENGTH_INDEFINITE)
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

    override fun setCardData(data: Kehadiran) {
        val sesi = "Sesi " + data.kdSesi
        tvNamaMatakuliah.text = dataJadwal!!.matakuliah?.namaMatakuliah?:"ErrNull"
        tvNamaDosen.text = dataJadwal!!.dosen?.namaDosen?:"ErrNull"
        tvJenisPerkuliahan.text = dataJadwal!!.jenisPerkuliahan
        tvSesi.text = sesi
        tvTglPresensi.text = mPresenter?.convertDate(data.tglPresensi!!)
        tvSesiDibuka.text = data.jamPresensiDibuka?.dropLast(3)
        tvJamPresensi.text = data.jamPresensi?.dropLast(3)
        tvStatusPresensi.text = data.statusPresensi?.keteranganPresensi
        if(data.kdSuratIzin!=null){
            cardKdSuratIzin.visibility = View.VISIBLE
            tvKdSuratIzin.text = data.kdSuratIzin
        }else{
            cardKdSuratIzin.visibility = View.INVISIBLE
        }
    }

    override fun attachPresenter(presenter: DetailPresensiMahasiswaContract.Presenter) {
        mPresenter = presenter
    }

    override fun detachPresenter() {
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun showProgress() {
        swipeRefreshDetailPresensiMahasiswa.isRefreshing = true
    }

    override fun hideProgress() {
        swipeRefreshDetailPresensiMahasiswa.isRefreshing = false
    }
}