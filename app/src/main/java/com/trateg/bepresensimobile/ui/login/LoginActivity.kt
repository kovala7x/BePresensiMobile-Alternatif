package com.trateg.bepresensimobile.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricManager
import androidx.core.widget.doOnTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.trateg.bepresensimobile.BaseActivity
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.ui.main.MainActivity
import com.trateg.bepresensimobile.util.SessionManager
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity(), LoginContract.View {

    private var mPresenter: LoginContract.Presenter? = null
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        attachPresenter(LoginPresenter(this))
        init()
    }

    private fun init() {
        isBiometricAvailable()
        mPresenter?.setupSession()
        btnLogin.setOnClickListener {
            hideSoftKeyboard()
            if (isFormEmailValid() && isFormPaswordValid()) {
                mPresenter?.doLogin(
                    email = getEmail(),
                    password = getPassword()
                )
            } else {
                Snackbar.make(it, "Periksa kembali form!", Snackbar.LENGTH_SHORT).show()
            }
        }
        tvLupaPassword.setOnClickListener {
            Snackbar.make(it, "Silahkan hubungi staff tata usaha...", Snackbar.LENGTH_LONG).show()
        }

        inputEmail.editText?.doOnTextChanged { text, start, before, count ->
            inputEmail.error = null
        }

        inputPassword.editText?.doOnTextChanged { text, start, before, count ->
            inputPassword.error = null
        }

        inputPassword.editText?.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                btnLogin.callOnClick()
                true
            } else {
                false
            }
        }

        swipeRefreshLogin.isEnabled = false
    }

    override fun onDestroy() {
        detachPresenter()
        super.onDestroy()
    }

    override fun getEmail(): String {
        return inputEmail.editText?.text.toString()
    }

    override fun getPassword(): String {
        return inputPassword.editText?.text.toString()
    }

    override fun onError(msg: String) {
        Snackbar.make(swipeRefreshLogin, msg, Snackbar.LENGTH_INDEFINITE)
            .setAction("TUTUP") {
                // tutup snackbar
                // biarkan kosong
            }.show()
    }

    override fun showDialog(title: String, msg: String) {
        dialog = MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton("OKE") { it, which ->
                it.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    override fun isFormEmailValid(): Boolean {
        var valid = true
        inputEmail.error = null
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(getEmail())
                .matches()
        ) {
            // jika input email bukan merupakan email
            valid = false
            inputEmail.error = "Email tidak valid!"
        }
        return valid
    }

    override fun isFormPaswordValid(): Boolean {
        var valid = true
        inputPassword.error = null
        if (getPassword().length < 6) {
            // jika input password kurang dari 6 karakter
            valid = false
            inputPassword.error = "Password tidak boleh kurang dari 6 karakter!"
        }
        return valid
    }

    override fun hideSoftKeyboard() {
        val view = this.currentFocus
        view?.let { v ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    override fun goToHome() {
        startActivity(Intent(baseContext, MainActivity::class.java))
        detachPresenter()
        finish()
    }

    override fun isBiometricAvailable(): Boolean {
        var isAvailable = false
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                isAvailable = true
                Log.d("LoginActivity", "Perangkat ini mendukung fitur biometrik")
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                isAvailable = false
                Log.e("LoginActivity", "Tidak ada fitur biometrik pada perangkat ini.")
                showDialog(
                    "Biometrik tidak didukung!",
                    "Tidak ada fitur biometrik pada perangkat ini."
                )
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                isAvailable = false
                Log.e("LoginActivity", "Fitur biometrik sedang tidak tersedia.")
                showDialog(
                    "Biometrik tidak tersedia!",
                    "Fitur biometrik sedang tidak tersedia."
                )
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                isAvailable = false
                Log.e(
                    "LoginActivity",
                    "Belum ada identitas biometrik yang didaftarkan pada perangkat ini."
                )
                showDialog(
                    "Biometrik belum didaftarkan!",
                    "Belum ada identitas biometrik yang didaftarkan pada perangkat ini."
                )
            }
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                isAvailable = false
                Log.e(
                    "LoginActivity",
                    "Pembaharuan fitur keamanan perangkat perlu dilakukan."
                )
                showDialog(
                    "Perhatian!",
                    "Pembaharuan fitur keamanan perangkat perlu dilakukan."
                )
            }
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                isAvailable = false
                Log.e(
                    "LoginActivity",
                    "Fitur biometrik tidak didukung."
                )
                showDialog(
                    "Perhatian!",
                    "Fitur biometrik tidak didukung."
                )
            }
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                isAvailable = false
                Log.e(
                    "LoginActivity",
                    "Fitur biometrik mungkin tidak didukung atau tingkat keamanan rentan."
                )
                showDialog(
                    "Perhatian!",
                    "Fitur biometrik mungkin tidak didukung atau tingkat keamanan rendah."
                )
            }
        }

        return isAvailable
    }

    override fun initSession(): SessionManager {
        return SessionManager(baseContext)
    }

    override fun attachPresenter(presenter: LoginContract.Presenter) {
        mPresenter = presenter
    }

    override fun detachPresenter() {
        dialog?.dismiss()
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun showProgress() {
        btnLogin.isEnabled = false
        swipeRefreshLogin.let {
            it.isEnabled = true
            it.isRefreshing = true
        }
    }

    override fun hideProgress() {
        btnLogin.isEnabled = true
        swipeRefreshLogin.let {
            it.isRefreshing = false
            it.isEnabled = false
        }
    }
}