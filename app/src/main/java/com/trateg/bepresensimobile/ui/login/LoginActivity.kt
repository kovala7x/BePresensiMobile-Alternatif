package com.trateg.bepresensimobile.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.trateg.bepresensimobile.BaseActivity
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.ui.ajukan_surat.AjukanSuratActivity
import com.trateg.bepresensimobile.ui.main.MainActivity
import com.trateg.bepresensimobile.ui.main.MainContract
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.*

class LoginActivity : BaseActivity(), LoginContract.View {

    private var mPresenter: LoginContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        attachPresenter(LoginPresenter(this))
        init()
    }

    private fun init(){
        btnLogin.setOnClickListener {
            Snackbar.make(it,"Melakukan login...",Snackbar.LENGTH_LONG).show()
            GlobalScope.launch {
                delay(2000L)
                withContext(Dispatchers.Main){
                    startActivity(Intent(it.context, MainActivity::class.java))
                    finish()
                }
            }
        }
        tvLupaPassword.setOnClickListener {
            Snackbar.make(it,"Silahkan hubungi staff tata usaha...",Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        detachPresenter()
        super.onDestroy()
    }

    override fun attachPresenter(presenter: LoginContract.Presenter) {
        mPresenter = presenter
    }

    override fun detachPresenter() {
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }
}