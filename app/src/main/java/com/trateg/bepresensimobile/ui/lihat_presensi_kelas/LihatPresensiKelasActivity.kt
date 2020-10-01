package com.trateg.bepresensimobile.ui.lihat_presensi_kelas

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.trateg.bepresensimobile.BaseActivity
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.model.Jadwal
import com.trateg.bepresensimobile.model.PersentaseKehadiran
import com.trateg.bepresensimobile.ui.adapter.GridPersentaseKehadiranAdapter
import com.trateg.bepresensimobile.util.Constants
import kotlinx.android.synthetic.main.activity_lihat_presensi_kelas.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LihatPresensiKelasActivity : BaseActivity(), LihatPresensiKelasContract.View {

    private var mPresenter: LihatPresensiKelasContract.Presenter? = null
    private var dataJadwal: Jadwal? = null
    private var listPersentaseKehadiran: List<PersentaseKehadiran>? = null
    private var listPersentase: ArrayList<PersentaseKehadiran> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lihat_presensi_kelas)
        attachPresenter(LihatPresensiKelasPresenter(this))
        initView()
    }

    override fun initView() {
        showBackButton(true)
        title = "Lihat Presensi Kelas"
        getIntentExtraData()
        CoroutineScope(Dispatchers.Main).launch {
            listPersentaseKehadiran = mPresenter?.getListPersentaseKehadiran(
                kdJadwal = dataJadwal!!.kdJadwal!!
            )
            if (listPersentaseKehadiran != null) {
                onGetListPersentaseKehadiranSuccess(listPersentaseKehadiran!!)
            }
        }
        swipeRefreshLihatPresensiKelas.setOnRefreshListener {
            CoroutineScope(Dispatchers.Main).launch {
                listPersentaseKehadiran = mPresenter?.getListPersentaseKehadiran(
                    kdJadwal = dataJadwal!!.kdJadwal!!
                )
                if (listPersentaseKehadiran != null) {
                    onGetListPersentaseKehadiranSuccess(listPersentaseKehadiran!!)
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

    override fun showSnackbar(msg: String) {
        Snackbar.make(swipeRefreshLihatPresensiKelas, msg, Snackbar.LENGTH_INDEFINITE)
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

    override fun onGetListPersentaseKehadiranSuccess(data: List<PersentaseKehadiran>) {
        listPersentase.clear()
        listPersentase.addAll(data)
        rvSesi.layoutManager = GridLayoutManager(this, 2)
        val gridPersentaseKehadiran = GridPersentaseKehadiranAdapter(listPersentase)
        rvSesi.adapter = gridPersentaseKehadiran
        gridPersentaseKehadiran.setOnClickCallback(object: GridPersentaseKehadiranAdapter.OnItemClickCallback{
            override fun onItemClicked(data: PersentaseKehadiran) {
                onSesiClicked(data)
            }
        })
    }

    override fun onSesiClicked(data: PersentaseKehadiran) {
        //TODO("Not yet implemented")
        showToast("Sesi " +data.kdSesi.toString() + " dipanggil !")
    }

    override fun setTextMatakuliah(matakuliah: String) {
        tvNamaMatakuliah.text = matakuliah
    }

    override fun goToHome() {
        detachPresenter()
        finish()
    }

    override fun attachPresenter(presenter: LihatPresensiKelasContract.Presenter) {
        mPresenter = presenter
    }

    override fun detachPresenter() {
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun showProgress() {
        swipeRefreshLihatPresensiKelas.isRefreshing = true
    }

    override fun hideProgress() {
        swipeRefreshLihatPresensiKelas.isRefreshing = false
    }
}