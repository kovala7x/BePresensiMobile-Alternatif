package com.trateg.bepresensimobile.ui.lihat_presensi_mahasiswa

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.trateg.bepresensimobile.BaseActivity
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.model.Jadwal
import com.trateg.bepresensimobile.model.Kehadiran
import com.trateg.bepresensimobile.ui.adapter.ListKehadiranAdapter
import com.trateg.bepresensimobile.ui.detail_presensi_mahasiswa.DetailPresensiMahasiswaActivity
import com.trateg.bepresensimobile.util.Constants
import kotlinx.android.synthetic.main.activity_lihat_presensi_mahasiswa.*

class LihatPresensiMahasiswaActivity : BaseActivity(), LihatPresensiMahasiswaContract.View {

    private var mPresenter: LihatPresensiMahasiswaContract.Presenter? = null
    private var dataJadwal: Jadwal? = null
    private var listKehadiran: ArrayList<Kehadiran> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lihat_presensi_mahasiswa)
        attachPresenter(LihatPresensiMahasiswaPresenter(this))
        initView()
    }

    override fun initView() {
        showBackButton(true)
        title = "Detail Presensi"
        getIntentExtraData()
        setCardData(dataJadwal!!)
        setRecyclerviewKehadiran(dataJadwal!!.kehadiran!!)
    }

    override fun getIntentExtraData() {
        dataJadwal = intent.getParcelableExtra(Constants.DATA_JADWAL)
        if (dataJadwal == null) {
            showToast("Gagal mengambil data intent!")
            finish()
        }
    }

    override fun showSnackbar(msg: String) {
        Snackbar.make(scrollViewLihatPresensiMahasiswa, msg, Snackbar.LENGTH_INDEFINITE)
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

    override fun setCardData(data: Jadwal) {
        var jumlahSesi = 0
        var jumlahSesiDihadiri = 0
        data.kehadiran?.forEach {
            jumlahSesi++
            if(it.kdStatusPresensi.equals("H")) jumlahSesiDihadiri++
        }
        tvNamaMatakuliah.text = data.matakuliah?.namaMatakuliah?:"ErrNull"
        tvNamaDosen.text = data.dosen?.namaDosen?:"ErrNull"
        tvJenisPerkuliahan.text = data.jenisPerkuliahan
        tvKdRuangan.text = data.ruang?.kdRuang
        tvTglPresensi.text = mPresenter?.convertDate(data.kehadiran?.first()!!.tglPresensi!!)
        tvJumlahSesi.text = jumlahSesi.toString()
        tvJumlahSesiDihadiri.text = jumlahSesiDihadiri.toString()
    }

    override fun setRecyclerviewKehadiran(data: List<Kehadiran>) {
        listKehadiran.clear()
        listKehadiran.addAll(data)
        rvKehadiran.setHasFixedSize(true)
        rvKehadiran.layoutManager = LinearLayoutManager(this)
        val listKehadiranAdapter = ListKehadiranAdapter(listKehadiran)
        rvKehadiran.adapter = listKehadiranAdapter
        listKehadiranAdapter.setOnItemClickCallback(object :
            ListKehadiranAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Kehadiran) {
                onDetailPresensiClicked(data)
            }
        })
    }

    override fun onDetailPresensiClicked(data: Kehadiran) {
        val intent = Intent(this, DetailPresensiMahasiswaActivity::class.java)
        intent.putExtra(Constants.DATA_JADWAL, dataJadwal)
        intent.putExtra(Constants.DATA_KEHADIRAN, data)
        startActivity(intent)
    }

    override fun attachPresenter(presenter: LihatPresensiMahasiswaContract.Presenter) {
        mPresenter = presenter
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