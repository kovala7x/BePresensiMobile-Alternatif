package com.trateg.bepresensimobile.ui.surat_diajukan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.trateg.bepresensimobile.BaseFragment
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.model.SuratIzin
import com.trateg.bepresensimobile.ui.adapter.ListSuratIzinAdapter
import com.trateg.bepresensimobile.ui.detail_surat.DetailSuratActivity
import com.trateg.bepresensimobile.ui.pilih_jenis_surat.PilihJenisSuratActivity
import com.trateg.bepresensimobile.util.Constants
import com.trateg.bepresensimobile.util.SessionManager
import kotlinx.android.synthetic.main.fragment_surat_diajukan.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SuratDiajukanFragment : BaseFragment(),
    SuratDiajukanContract.View {
    private lateinit var mRootView: View
    private var mPresenter: SuratDiajukanContract.Presenter? = null
    private var dataSuratDiajukan: List<SuratIzin>? = null
    private var listSuratDiajukan: ArrayList<SuratIzin> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_surat_diajukan, container, false)
        attachPresenter(SuratDiajukanPresenter(this))
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun initView() {
        tvKeteranganSuratDiajukan.visibility = View.INVISIBLE
        mPresenter?.setupSession()
        rvSuratDiajukan.setHasFixedSize(true)
        rvSuratDiajukan.layoutManager = LinearLayoutManager(requireContext())
        if (mPresenter?.isMahasiswa()!!) {
            fabSuratBaru.setOnClickListener {
                onFabSuratBaruClicked()
            }
        } else {
            fabSuratBaru.visibility = View.GONE
        }

        onSwipeRefresh()
        swipeRefreshSuratDiajukan.setOnRefreshListener {
            onSwipeRefresh()
        }
    }

    override fun initSession(): SessionManager {
        return SessionManager(requireContext())
    }

    override fun showSnackbar(msg: String) {
        Snackbar.make(swipeRefreshSuratDiajukan, msg, Snackbar.LENGTH_INDEFINITE)
            .setAction("TUTUP") {
                // tutup snackbar
                // biarkan kosong
            }.show()
    }

    override fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onGetSuratDiajukanSuccess(data: List<SuratIzin>) {
        if (mPresenter?.isMahasiswa()!!) {
            if (data.isEmpty()) {
                tvKeteranganSuratDiajukan.text = "Tidak terdapat surat yang diajukan."
            } else {
                tvKeteranganSuratDiajukan.text = "Terdapat ${data.size} surat yang diajukan."
            }
        } else {
            if (data.isEmpty()) {
                tvKeteranganSuratDiajukan.text = "Tidak terdapat surat yang belum diproses."
            } else {
                tvKeteranganSuratDiajukan.text = "Terdapat ${data.size} surat yang belum diproses."
            }
        }
        tvKeteranganSuratDiajukan.visibility = View.VISIBLE
        listSuratDiajukan.clear()
        listSuratDiajukan.addAll(data)
        val listSuratIzinAdapter = ListSuratIzinAdapter(listSuratDiajukan)
        rvSuratDiajukan.adapter = listSuratIzinAdapter
        listSuratIzinAdapter.setOnItemClickCallback(object :
            ListSuratIzinAdapter.OnItemClickCallback {
            override fun onItemClicked(data: SuratIzin) {
                onItemSuratIzinClicked(data)
            }
        })
    }

    override fun onItemSuratIzinClicked(data: SuratIzin) {
        goToDetailSuratDiajukan(data)
    }

    override fun onFabSuratBaruClicked() {
        startActivity(Intent(context, PilihJenisSuratActivity::class.java))
    }

    override fun onSwipeRefresh() {
        CoroutineScope(Dispatchers.Main).launch {
            dataSuratDiajukan = mPresenter?.getSuratDiajukan()
            if (dataSuratDiajukan != null) {
                onGetSuratDiajukanSuccess(dataSuratDiajukan!!)
            }
        }
    }

    override fun goToDetailSuratDiajukan(data: SuratIzin) {
        val intent = Intent(requireContext(), DetailSuratActivity::class.java)
        intent.putExtra(Constants.DATA_SURAT, data)
        startActivity(intent)
    }

    override fun attachPresenter(presenter: SuratDiajukanContract.Presenter) {
        mPresenter = presenter
    }

    override fun detachPresenter() {
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun showProgress() {
        swipeRefreshSuratDiajukan.isRefreshing = true
    }

    override fun hideProgress() {
        swipeRefreshSuratDiajukan.isRefreshing = false
    }
}