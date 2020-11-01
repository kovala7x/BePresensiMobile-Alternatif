package com.trateg.bepresensimobile.ui.surat_diproses

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
import com.trateg.bepresensimobile.util.SessionManager
import kotlinx.android.synthetic.main.fragment_surat_diproses.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SuratDiprosesFragment : BaseFragment(), SuratDiprosesContract.View {
    private lateinit var mRootView: View
    private var mPresenter: SuratDiprosesContract.Presenter? = null
    private var dataSuratDiproses: List<SuratIzin>? = null
    private var listSuratDiproses: ArrayList<SuratIzin> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_surat_diproses, container, false)
        attachPresenter(SuratDiprosesPresenter(this))
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Main).launch {
            dataSuratDiproses = mPresenter?.getSuratDiproses()
            if (dataSuratDiproses != null) {
                onGetSuratDiprosesSuccess(dataSuratDiproses!!)
            }
        }
    }

    override fun initView() {
        tvKeteranganSuratDiproses.visibility = View.INVISIBLE
        mPresenter?.setupSession()
        rvSuratDiproses.setHasFixedSize(true)
        rvSuratDiproses.layoutManager = LinearLayoutManager(requireContext())
        swipeRefreshSuratDiproses.setOnRefreshListener {
            CoroutineScope(Dispatchers.Main).launch {
                dataSuratDiproses = mPresenter?.getSuratDiproses()
                if (dataSuratDiproses != null) {
                    onGetSuratDiprosesSuccess(dataSuratDiproses!!)
                }
            }
        }
    }

    override fun initSession(): SessionManager {
        return SessionManager(requireContext())
    }

    override fun showSnackbar(msg: String) {
        Snackbar.make(swipeRefreshSuratDiproses, msg, Snackbar.LENGTH_INDEFINITE)
            .setAction("TUTUP") {
                // tutup snackbar
                // biarkan kosong
            }.show()
    }

    override fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onGetSuratDiprosesSuccess(data: List<SuratIzin>) {
        if(mPresenter?.isMahasiswa()!!){
            if(data.isEmpty()){
                tvKeteranganSuratDiproses.text = "Tidak terdapat surat yang diproses."
            }else{
                tvKeteranganSuratDiproses.text = "Terdapat ${data.size} surat yang diproses."
            }
        }else{
            if(data.isEmpty()){
                tvKeteranganSuratDiproses.text = "Tidak terdapat surat yang sudah diproses."
            }else{
                tvKeteranganSuratDiproses.text = "Terdapat ${data.size} surat yang sudah diproses."
            }
        }
        tvKeteranganSuratDiproses.visibility = View.VISIBLE
        listSuratDiproses.clear()
        listSuratDiproses.addAll(data)
        val listSuratIzinAdapter = ListSuratIzinAdapter(listSuratDiproses)
        rvSuratDiproses.adapter = listSuratIzinAdapter
        listSuratIzinAdapter.setOnItemClickCallback(object :
            ListSuratIzinAdapter.OnItemClickCallback {
            override fun onItemClicked(data: SuratIzin) {
                onItemSuratIzinClicked(data)
            }
        })
    }

    override fun onItemSuratIzinClicked(data: SuratIzin) {
        goToDetailSuratDiproses(data)
    }

    override fun goToDetailSuratDiproses(data: SuratIzin) {
        //TODO("Not yet implemented")
        showToast("Surat ${data.kdSuratIzin} ditekan!")
    }

    override fun attachPresenter(presenter: SuratDiprosesContract.Presenter) {
        mPresenter = presenter
    }

    override fun detachPresenter() {
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun showProgress() {
        swipeRefreshSuratDiproses.isRefreshing = true
    }

    override fun hideProgress() {
        swipeRefreshSuratDiproses.isRefreshing = false
    }
}