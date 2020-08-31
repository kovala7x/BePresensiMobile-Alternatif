package com.trateg.bepresensimobile.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.trateg.bepresensimobile.BaseFragment
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.model.Jadwal
import com.trateg.bepresensimobile.ui.adapter.ListJadwalMahasiswaAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.*

class HomeFragment : BaseFragment(), HomeContract.View {
    private var mPresenter: HomeContract.Presenter? = null
    private lateinit var mRootView: View
    private var listJadwal: ArrayList<Jadwal> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_home, container, false)
        attachPresenter(HomePresenter(this))
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun initView() {
        mPresenter?.getCurrentDate()
        tvKeteranganJadwal.visibility = TextView.INVISIBLE
        rvJadwalMahasiswa.setHasFixedSize(true)
        rvJadwalMahasiswa.layoutManager = LinearLayoutManager(requireContext())
        mPresenter?.getDataJadwal()
        swipeRefreshHome.setOnRefreshListener {
            mPresenter?.getDataJadwal()
        }
    }

    override fun onDestroyView() {
        detachPresenter()
        super.onDestroyView()
    }

    override fun updateTextDate(date: String) {
        tvTanggal.text = date
    }

    override fun onGetDataJadwalSuccess(jadwal: List<Jadwal>) {
        tvKeteranganJadwal.visibility = TextView.VISIBLE
        if (jadwal.size != 0) {
            tvKeteranganJadwal.text = "Terdapat ${jadwal.size.toString()} matakuliah hari ini"
        } else {
            tvKeteranganJadwal.text = "Tidak terdapat jadwal perkuliahan hari ini!"
        }
        listJadwal.clear()
        listJadwal.addAll(jadwal)
        rvJadwalMahasiswa.adapter = ListJadwalMahasiswaAdapter(listJadwal)
    }

    override fun onError(msg: String) {
        Snackbar.make(requireView(), "Terdapat Kesalahan!\n${msg}", Snackbar.LENGTH_LONG)
            .setAction("TUTUP") {
                // tutup snackbar
                // biarkan kosong
            }.show()
    }

    override fun attachPresenter(presenter: HomeContract.Presenter) {
        mPresenter = presenter
    }

    override fun detachPresenter() {
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun showProgress() {
        swipeRefreshHome.isRefreshing = true
    }

    override fun hideProgress() {
        swipeRefreshHome.isRefreshing = false
    }

}