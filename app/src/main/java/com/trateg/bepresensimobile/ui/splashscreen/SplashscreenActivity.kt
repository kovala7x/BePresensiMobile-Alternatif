package com.trateg.bepresensimobile.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.trateg.bepresensimobile.BaseActivity
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.ui.login.LoginActivity
import com.trateg.bepresensimobile.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashscreenActivity : BaseActivity(), SplashscreenContract.View {
    private var mPresenter: SplashscreenContract.Presenter? = null
    lateinit var imgPhoto: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        // Hide Status Bar & Action Bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        imgPhoto = findViewById(R.id.img_item_photo)
        Glide.with(this)
            .load(R.drawable.bepresensi)
            .apply(RequestOptions().override(512,512))
            .into(imgPhoto)

        // Handler Delay SplashScreen
        CoroutineScope(Dispatchers.Main).launch {
            delay(Constants.SPLASHSCREEN_TIME)
            goToLoginActivity()
        }
    }

    override fun goToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun attachPresenter(presenter: SplashscreenContract.Presenter) {
        mPresenter = presenter
    }

    override fun detachPresenter() {
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun showProgress() {
        //TODO("Not implemented")
    }

    override fun hideProgress() {
        //TODO("Not implemented")
    }
}