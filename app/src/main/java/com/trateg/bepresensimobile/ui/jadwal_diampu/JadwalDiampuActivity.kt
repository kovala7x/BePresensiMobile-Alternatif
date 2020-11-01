package com.trateg.bepresensimobile.ui.jadwal_diampu

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.trateg.bepresensimobile.BaseActivity
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.model.Jadwal
import com.trateg.bepresensimobile.ui.adapter.ListJadwalDiampuAdapter
import com.trateg.bepresensimobile.ui.ubah_toleransi_keterlambatan.UbahToleransiKeterlambatanActivity
import com.trateg.bepresensimobile.util.Constants
import com.trateg.bepresensimobile.util.SessionManager
import kotlinx.android.synthetic.main.activity_jadwal_diampu.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JadwalDiampuActivity : BaseActivity(), JadwalDiampuContract.View {
    private var mPresenter: JadwalDiampuContract.Presenter? = null
    private var dataJadwalDiampu: List<Jadwal>? = null
    private var listJadwalDiampu: ArrayList<Jadwal> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jadwal_diampu)
        attachPresenter(JadwalDiampuPresenter(this))
        initView()
    }

    override fun onResume() {
        super.onResume()
        onSwipeRefresh()
    }

    override fun goToUbahToleransiWaktu(data: Jadwal) {
        val intent = Intent(this, UbahToleransiKeterlambatanActivity::class.java)
        intent.putExtra(Constants.DATA_JADWAL, data)
        startActivity(intent)
    }

    override fun initView() {
        title = "Jadwal Diampu"
        showBackButton(true)
        tvKeteranganJadwalDiampu.visibility = View.INVISIBLE
        mPresenter?.setupSession(SessionManager(this))
        rvJadwalDiampu.setHasFixedSize(true)
        rvJadwalDiampu.layoutManager = LinearLayoutManager(this)
        swipeRefreshJadwalDiampu.setOnRefreshListener {
            onSwipeRefresh()
        }
    }

    override fun onSwipeRefresh() {
        CoroutineScope(Dispatchers.Main).launch {
            dataJadwalDiampu = mPresenter?.getJadwalDiampu()
            if (dataJadwalDiampu != null) {
                onGetJadwalDiampuSuccess(dataJadwalDiampu!!)
            }
        }
    }

    override fun onGetJadwalDiampuSuccess(data: List<Jadwal>) {
        if (data.isEmpty()) {
            tvKeteranganJadwalDiampu.text = "Anda tidak mengampu jadwal perkuliahan."
        } else {
            tvKeteranganJadwalDiampu.text = "Terdapat ${data.size} jadwal diampu."
        }
        tvKeteranganJadwalDiampu.visibility = View.VISIBLE
        listJadwalDiampu.clear()
        listJadwalDiampu.addAll(data)
        val listJadwalDiampuAdapter = ListJadwalDiampuAdapter(listJadwalDiampu)
        rvJadwalDiampu.adapter = listJadwalDiampuAdapter
        listJadwalDiampuAdapter.setOnItemClickCallback(object :
            ListJadwalDiampuAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Jadwal) {
                onJadwalClicked(data)
            }
        })
    }

    override fun onJadwalClicked(data: Jadwal) {
        goToUbahToleransiWaktu(data)
    }

    override fun showBackButton(show: Boolean) {
        if (supportActionBar != null) supportActionBar!!.setDisplayHomeAsUpEnabled(show)
    }

    override fun onSupportNavigateUp(): Boolean {
        detachPresenter()
        finish()
        return super.onSupportNavigateUp()
    }

    override fun showSnackbar(msg: String) {
        Snackbar.make(swipeRefreshJadwalDiampu, msg, Snackbar.LENGTH_INDEFINITE)
            .setAction("TUTUP") {
                // tutup snackbar
                // biarkan kosong
            }.show()
    }

    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun attachPresenter(presenter: JadwalDiampuContract.Presenter) {
        mPresenter = presenter
    }

    override fun detachPresenter() {
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun showProgress() {
        swipeRefreshJadwalDiampu.isRefreshing = true
    }

    override fun hideProgress() {
        swipeRefreshJadwalDiampu.isRefreshing = false
    }
}