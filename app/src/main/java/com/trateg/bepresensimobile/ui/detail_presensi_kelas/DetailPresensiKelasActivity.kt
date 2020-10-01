package com.trateg.bepresensimobile.ui.detail_presensi_kelas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.trateg.bepresensimobile.BaseActivity
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.model.Jadwal
import com.trateg.bepresensimobile.model.Kehadiran
import com.trateg.bepresensimobile.model.PersentaseKehadiran
import com.trateg.bepresensimobile.ui.adapter.ListDaftarHadirAdapter
import com.trateg.bepresensimobile.ui.ubah_status_presensi.UbahStatusPresensiActivity
import com.trateg.bepresensimobile.util.Constants
import kotlinx.android.synthetic.main.activity_detail_presensi_kelas.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailPresensiKelasActivity : BaseActivity(), DetailPresensiKelasContract.View {

    private var mPresenter: DetailPresensiKelasContract.Presenter? = null
    private var dataJadwal: Jadwal? = null
    private var dataPersentase: PersentaseKehadiran? = null
    private var listDaftarHadirKelas: List<Kehadiran>? = null
    private var listDaftarHadir: ArrayList<Kehadiran> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_presensi_kelas)
        attachPresenter(DetailPresensiKelasPresenter(this))
        initView()
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Main).launch {
            listDaftarHadirKelas = mPresenter?.getListDaftarHadirKelas(
                kdJadwal = dataJadwal!!.kdJadwal!!,
                kdSesi = dataPersentase!!.kdSesi!!
            )
            if (listDaftarHadirKelas != null) {
                onGetListDaftarHadirKelasSuccess(listDaftarHadirKelas!!)
            }
        }
    }

    override fun initView() {
        showBackButton(true)
        title = "Detail Presensi Kelas"
        getIntentExtraData()
        setTextMatakuliah(dataJadwal!!.matakuliah?.namaMatakuliah?:"ErrNull")
        setTextSubtitleSesi(dataPersentase!!.kdSesi?:0)
        swipeRefreshDetailPresensiKelas.setOnRefreshListener {
            CoroutineScope(Dispatchers.Main).launch {
                listDaftarHadirKelas = mPresenter?.getListDaftarHadirKelas(
                    kdJadwal = dataJadwal!!.kdJadwal!!,
                    kdSesi = dataPersentase!!.kdSesi!!
                )
                if (listDaftarHadirKelas != null) {
                    onGetListDaftarHadirKelasSuccess(listDaftarHadirKelas!!)
                }
            }
        }
    }

    override fun getIntentExtraData() {
        dataJadwal = intent.getParcelableExtra(Constants.DATA_JADWAL)
        dataPersentase = intent.getParcelableExtra(Constants.DATA_PERSENTASE)
        if (dataJadwal == null || dataPersentase == null) {
            showToast("Gagal mengambil data intent!")
            finish()
        }
    }

    override fun showSnackbar(msg: String) {
        Snackbar.make(swipeRefreshDetailPresensiKelas, msg, Snackbar.LENGTH_INDEFINITE)
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

    override fun onGetListDaftarHadirKelasSuccess(data: List<Kehadiran>) {
        listDaftarHadir.clear()
        listDaftarHadir.addAll(data)
        val listDaftarHadirAdapter = ListDaftarHadirAdapter(listDaftarHadir)
        rvDaftarHadir.setHasFixedSize(true)
        rvDaftarHadir.layoutManager = LinearLayoutManager(this)
        rvDaftarHadir.adapter = listDaftarHadirAdapter
        listDaftarHadirAdapter.setOnItemClickCallback(object :
            ListDaftarHadirAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Kehadiran) {
                onKehadiranClicked(data)
            }
        })
    }

    override fun onKehadiranClicked(data: Kehadiran) {
        val intent = Intent(this, UbahStatusPresensiActivity::class.java)
        intent.putExtra(Constants.DATA_KEHADIRAN, data)
        startActivity(intent)
    }

    override fun setTextMatakuliah(matakuliah: String) {
        tvNamaMatakuliah.text = matakuliah
    }

    override fun setTextSubtitleSesi(sesi: Int) {
        tvSesi.text = "Sesi $sesi perkuliahan"
    }

    override fun attachPresenter(presenter: DetailPresensiKelasContract.Presenter) {
        mPresenter = presenter
    }

    override fun detachPresenter() {
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun showProgress() {
        swipeRefreshDetailPresensiKelas.isRefreshing = true
    }

    override fun hideProgress() {
        swipeRefreshDetailPresensiKelas.isRefreshing = false
    }
}