package com.trateg.bepresensimobile.ui.riwayat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.trateg.bepresensimobile.BaseFragment
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.model.BeritaAcara
import com.trateg.bepresensimobile.model.Jadwal
import com.trateg.bepresensimobile.model.RiwayatKehadiran
import com.trateg.bepresensimobile.ui.adapter.ListRiwayatKehadiranMahasiswaAdapter
import com.trateg.bepresensimobile.ui.lihat_presensi_mahasiswa.LihatPresensiMahasiswaActivity
import com.trateg.bepresensimobile.util.Constants
import com.trateg.bepresensimobile.util.SessionManager
import kotlinx.android.synthetic.main.fragment_riwayat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RiwayatFragment : BaseFragment(),
    RiwayatContract.View {
    private var mPresenter: RiwayatContract.Presenter? = null

    private lateinit var mRootView: View
    private var dataRiwayatMhs: List<RiwayatKehadiran>? = null
    private var dataRiwayatDosen: List<BeritaAcara>? = null
    private var listRiwayatMhs: ArrayList<Any> = arrayListOf()
    private var listRiwayatDosen: ArrayList<BeritaAcara> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_riwayat, container, false)
        attachPresenter(RiwayatPresenter(this))
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

    override fun onDestroyView() {
        detachPresenter()
        super.onDestroyView()
    }

    override fun initView() {
        mPresenter?.setupSession()
        rvRiwayat.setHasFixedSize(true)
        rvRiwayat.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun initSession(): SessionManager {
        return SessionManager(requireContext())
    }

    override fun showSnackbar(msg: String) {
        Snackbar.make(swipeRefreshRiwayat, msg, Snackbar.LENGTH_INDEFINITE)
            .setAction("TUTUP") {
                // tutup snackbar
                // biarkan kosong
            }.show()
    }

    override fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onGetRiwayatMhsSuccess(data: List<RiwayatKehadiran>) {
        tvKeteranganRiwayat.visibility = TextView.INVISIBLE
        if (data.isEmpty()) {
            tvKeteranganRiwayat.text = "Tidak ada riwayat presensi minggu ini!"
        }else{
            tvKeteranganRiwayat.text = "Menampilkan riwayat presensi minggu ini"
        }
        tvKeteranganRiwayat.visibility = TextView.VISIBLE

        listRiwayatMhs.clear()
        data.forEach {
            listRiwayatMhs.add(it.tanggal!!)
            it.jadwal!!.forEach {
                listRiwayatMhs.add(it)
            }
        }

        val listRiwayatKehadiranMahasiswaAdapter =
            ListRiwayatKehadiranMahasiswaAdapter(listRiwayatMhs)
        rvRiwayat.adapter = listRiwayatKehadiranMahasiswaAdapter
        listRiwayatKehadiranMahasiswaAdapter.setOnItemClickCallback(object :
            ListRiwayatKehadiranMahasiswaAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Jadwal) {
                onItemJadwalClicked(data)
            }
        })
    }

    override fun onGetRiwayatDosenSuccess(data: List<BeritaAcara>) {
        //TODO("Not yet implemented")
    }

    override fun onItemJadwalClicked(data: Jadwal) {
        val intent = Intent(requireContext(), LihatPresensiMahasiswaActivity::class.java)
        intent.putExtra(Constants.DATA_JADWAL, data)
        startActivity(intent)
    }

    override fun setDosenMode() {
        //TODO("Not yet implemented")
    }

    override fun setMhsMode() {
        CoroutineScope(Dispatchers.Main).launch {
            dataRiwayatMhs = mPresenter?.getRiwayatMhs()
            if (dataRiwayatMhs != null) {
                onGetRiwayatMhsSuccess(dataRiwayatMhs!!)
            }
        }
        swipeRefreshRiwayat.setOnRefreshListener {
            CoroutineScope(Dispatchers.Main).launch {
                dataRiwayatMhs = mPresenter?.getRiwayatMhs()
                if (dataRiwayatMhs != null) {
                    onGetRiwayatMhsSuccess(dataRiwayatMhs!!)
                }
            }
        }
    }

    override fun goToDetailRiwayatMhs(data: Jadwal) {
        //TODO("Not yet implemented")
        showToast("Riwayat jadwal " + data.kdJadwal + " dipanggil!")
    }

    override fun goToDetailRiwayatDosen(data: BeritaAcara) {
        //TODO("Not yet implemented")
        showToast("Riwayat berita acara " + data.kdBeritaAcara + " dipanggil!")
    }

    override fun attachPresenter(presenter: RiwayatContract.Presenter) {
        mPresenter = presenter
    }

    override fun detachPresenter() {
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun showProgress() {
        swipeRefreshRiwayat.isRefreshing = true
    }

    override fun hideProgress() {
        swipeRefreshRiwayat.isRefreshing = false
    }

}