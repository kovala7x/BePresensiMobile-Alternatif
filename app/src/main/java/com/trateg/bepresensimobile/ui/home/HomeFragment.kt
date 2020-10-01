package com.trateg.bepresensimobile.ui.home

import android.content.Intent
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
import com.trateg.bepresensimobile.ui.adapter.ListJadwalDosenAdapter
import com.trateg.bepresensimobile.ui.adapter.ListJadwalMahasiswaAdapter
import com.trateg.bepresensimobile.ui.detail_berita_acara.DetailBeritaAcaraActivity
import com.trateg.bepresensimobile.ui.isi_berita_acara.IsiBeritaAcaraActivity
import com.trateg.bepresensimobile.ui.otentikasi_presensi.OtentikasiPresensiActivity
import com.trateg.bepresensimobile.util.Constants
import com.trateg.bepresensimobile.util.SessionManager
import kotlinx.android.synthetic.main.fragment_home.*

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

    override fun onResume() {
        super.onResume()
        mPresenter?.getUserRole()
    }

    override fun initView() {
        mPresenter?.setupSession()
        mPresenter?.getCurrentDate()
        tvKeteranganJadwal.visibility = TextView.INVISIBLE
        rvJadwal.setHasFixedSize(true)
        rvJadwal.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun initSession(): SessionManager {
        return SessionManager(requireContext())
    }

    override fun onDestroyView() {
        detachPresenter()
        super.onDestroyView()
    }

    override fun updateTextDate(date: String) {
        tvTanggal.text = date
    }

    override fun onGetJadwalMhsSuccess(jadwal: List<Jadwal>) {
        tvKeteranganJadwal.visibility = TextView.VISIBLE
        if (jadwal.size != 0) {
            tvKeteranganJadwal.text = "Terdapat ${jadwal.size} matakuliah hari ini"
        } else {
            tvKeteranganJadwal.text = "Tidak terdapat jadwal perkuliahan hari ini!"
        }
        listJadwal.clear()
        listJadwal.addAll(jadwal)
        val listJadwalMahasiswaAdapter = ListJadwalMahasiswaAdapter(listJadwal)
        rvJadwal.adapter = listJadwalMahasiswaAdapter
        listJadwalMahasiswaAdapter.setOnItemClickCallback(object :
            ListJadwalMahasiswaAdapter.OnItemClickCallback {
            override fun OnBtnPresensiClicked(data: Jadwal?) {
                goToCatatPresensi(data!!)
            }

            override fun OnBtnDetailPresensiClicked(data: Jadwal?) {
                goToDetailPresensiMhs(data!!)
            }
        })
    }

    override fun onGetJadwalDosenSuccess(jadwal: List<Jadwal>) {
        tvKeteranganJadwal.visibility = TextView.VISIBLE
        if (jadwal.size != 0) {
            tvKeteranganJadwal.text =
                "Terdapat ${jadwal.size} matakuliah diampu hari ini"
        } else {
            tvKeteranganJadwal.text = "Tidak terdapat jadwal perkuliahan hari ini!"
        }
        listJadwal.clear()
        listJadwal.addAll(jadwal)
        val listJadwalDosenAdapter = ListJadwalDosenAdapter(listJadwal)
        rvJadwal.adapter = listJadwalDosenAdapter
        listJadwalDosenAdapter.setOnItemClickCallback(object :
            ListJadwalDosenAdapter.OnItemClickCallback {
            override fun OnBtnBukaSesiClicked(data: Jadwal?) {
                goToBukaSesiPresensi(data!!)
            }

            override fun OnBtnLihatBeritaAcaraClicked(data: Jadwal?) {
                goToLihatBeritaAcara(data!!)
            }

            override fun OnBtnTutupSesiClicked(data: Jadwal?) {
                goToTutupSesiPresensi(data!!)
            }

            override fun OnBtnLihatPresensiClicked(data: Jadwal?) {
                goToLihatPresensi(data!!)
            }

            override fun OnBtnIsiBeritaAcaraClicked(data: Jadwal?) {
                goToIsiBeritaAcara(data!!)
            }

        })

    }

    override fun onError(msg: String) {
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_LONG)
            .setAction("TUTUP") {
                // tutup snackbar
                // biarkan kosong
            }.show()
    }

    override fun setDosenMode() {
        mPresenter?.getJadwalDosen()
        swipeRefreshHome.setOnRefreshListener {
            mPresenter?.getJadwalDosen()
        }
    }

    override fun setMhsMode() {
        mPresenter?.getJadwalMhs()
        swipeRefreshHome.setOnRefreshListener {
            mPresenter?.getJadwalMhs()
        }
    }

    override fun goToBukaSesiPresensi(data: Jadwal) {
        val intent = Intent(requireContext(), OtentikasiPresensiActivity::class.java)
        intent.putExtra(Constants.DATA_JADWAL, data)
        intent.putExtra(
            Constants.ACTION_TYPE,
            Constants.BUKA_PRESENSI
        )
        startActivity(intent)
    }

    override fun goToTutupSesiPresensi(data: Jadwal) {
        val intent = Intent(requireContext(), OtentikasiPresensiActivity::class.java)
        intent.putExtra(Constants.DATA_JADWAL, data)
        intent.putExtra(
            Constants.ACTION_TYPE,
            Constants.TUTUP_PRESENSI
        )
        startActivity(intent)
    }

    override fun goToCatatPresensi(data: Jadwal) {
        val intent = Intent(requireContext(), OtentikasiPresensiActivity::class.java)
        intent.putExtra(Constants.DATA_JADWAL, data)
        intent.putExtra(
            Constants.ACTION_TYPE,
            Constants.CATAT_PRESENSI
        )
        startActivity(intent)
    }

    override fun goToLihatBeritaAcara(data: Jadwal) {
        val intent = Intent(requireContext(), DetailBeritaAcaraActivity::class.java)
        intent.putExtra(Constants.DATA_JADWAL, data)
        startActivity(intent)
    }

    override fun goToDetailPresensiMhs(data: Jadwal) {
        //TODO("Not yet implemented")
        onError("Detail presensi mahasiswa ${data.matakuliah?.namaMatakuliah}!")
    }

    override fun goToLihatPresensi(data: Jadwal) {
        //TODO("Not yet implemented")
        onError("Lihat presensi ${data.matakuliah?.namaMatakuliah}!")
    }

    override fun goToIsiBeritaAcara(data: Jadwal) {
        val intent = Intent(requireContext(), IsiBeritaAcaraActivity::class.java)
        intent.putExtra(Constants.DATA_JADWAL, data)
        startActivity(intent)
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