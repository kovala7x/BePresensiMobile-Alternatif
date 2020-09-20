package com.trateg.bepresensimobile.ui.profil

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.trateg.bepresensimobile.BaseFragment
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.ui.login.LoginActivity
import com.trateg.bepresensimobile.util.SessionManager
import kotlinx.android.synthetic.main.fragment_profil.*

class ProfilFragment : BaseFragment(),
    ProfilContract.View {
    private lateinit var mRootView: View
    private var mPresenter: ProfilContract.Presenter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_profil, container, false)
        attachPresenter(ProfilPresenter(this))
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        mPresenter?.setupSession()
        mPresenter?.getUserData()
        btnLogout.setOnClickListener {
            onLogoutPressed()
        }
    }

    override fun onDestroyView() {
        detachPresenter()
        super.onDestroyView()
    }

    override fun initSession(): SessionManager {
        return SessionManager(requireContext())
    }

    override fun setUserData(role: String, nama: String, kode: String, email: String) {
        tvRole.text = role
        tvNama.text = nama
        chipIDUser.text = kode
        chipEmail.text = email
    }

    override fun setRekap(sakit: Int, izin: Int, alfa: Int, status: String) {
        tvJumlahSakit.text = sakit.toString()
        tvJumlahIzin.text = izin.toString()
        tvJumlahAlfa.text = alfa.toString()
        tvStatus.text = status
    }

    override fun onLogoutPressed() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Keluar?")
            .setMessage("Apakah Anda yakin ingin keluar aplikasi ini ?")
            .setPositiveButton("YAKIN") { dialog, which ->
                dialog.dismiss()
                mPresenter?.doLogout()
                startActivity(Intent(activity, LoginActivity::class.java))
                activity?.finish()
            }
            .setNegativeButton("TIDAK") { dialog, which ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    override fun setDosenMode() {
        tvRekap.visibility = View.GONE
        layoutRekap.visibility = View.GONE
        btnAturToleransi.visibility = View.VISIBLE
        swipeRefreshProfil.isEnabled = false
    }

    override fun setMahasiswaMode() {
        tvRekap.visibility = View.VISIBLE
        layoutRekap.visibility = View.VISIBLE
        btnAturToleransi.visibility = View.GONE
        setRekap(0,0,0,"-")
        swipeRefreshProfil.setOnRefreshListener {
            mPresenter?.getRekap()
        }
        mPresenter?.getRekap()
    }

    override fun onError(msg: String) {
        Snackbar.make(swipeRefreshProfil, msg, Snackbar.LENGTH_INDEFINITE)
            .setAction("TUTUP") {
                // tutup snackbar
                // biarkan kosong
            }.show()
    }

    override fun attachPresenter(presenter: ProfilContract.Presenter) {
        mPresenter = presenter
    }

    override fun detachPresenter() {
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun showProgress() {
        swipeRefreshProfil.isRefreshing = true
    }

    override fun hideProgress() {
        swipeRefreshProfil.isRefreshing = false
    }

}