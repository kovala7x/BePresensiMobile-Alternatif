package com.trateg.bepresensimobile.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import com.google.android.material.snackbar.Snackbar
import com.trateg.bepresensimobile.BaseActivity
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.ui.main.MainActivity
import com.trateg.bepresensimobile.util.SessionManager
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), LoginContract.View {

    private var mPresenter: LoginContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        attachPresenter(LoginPresenter(this))
        init()
    }

    private fun init() {
        mPresenter?.setupSession()
        btnLogin.setOnClickListener {
            hideSoftKeyboard()
            // BELUM BERES
            if (isFormEmailValid() && isFormPaswordValid()) {
                mPresenter?.doLogin(
                    email = inputEmail.editText?.text.toString(),
                    password = inputPassword.editText?.text.toString()
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

    override fun isFormEmailValid(): Boolean {
        var valid = true
        inputEmail.error = null
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(inputEmail.editText?.text.toString())
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
        if (inputPassword.editText?.text!!.toString().length < 6) {
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
        finish()
    }

    override fun initSession(): SessionManager {
        return SessionManager(baseContext)
    }

    override fun attachPresenter(presenter: LoginContract.Presenter) {
        mPresenter = presenter
    }

    override fun detachPresenter() {
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