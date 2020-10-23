package com.trateg.bepresensimobile.ui.otentikasi_presensi

import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.eyro.cubeacon.*
import com.google.android.material.snackbar.Snackbar
import com.trateg.bepresensimobile.BaseActivity
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.model.BaseResponse
import com.trateg.bepresensimobile.model.BaseResponseList
import com.trateg.bepresensimobile.model.Beacon
import com.trateg.bepresensimobile.model.Jadwal
import com.trateg.bepresensimobile.util.Constants
import com.trateg.bepresensimobile.util.SessionManager
import kotlinx.android.synthetic.main.activity_otentikasi_presensi.*
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.Executor

class OtentikasiPresensiActivity : BaseActivity(), OtentikasiPresensiContract.View,
    CBServiceListener {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var cubeacon: Cubeacon
    private lateinit var region: CBRegion

    private var mPresenter: OtentikasiPresensiContract.Presenter? = null
    private var actionType: Int = 0
    private var dataJadwal: Jadwal? = null
    private var isBeaconFound: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otentikasi_presensi)
        attachPresenter(OtentikasiPresensiPresenter(this))
        initView()
    }

    override fun initView() {
        Logger.setLogLevel(LogLevel.DEBUG)
        Cubeacon.initialize(this)
        cubeacon = Cubeacon.getInstance()
        getIntentExtraData()
        setNamaMatakuliah(
            matakuliah = dataJadwal!!.matakuliah?.namaMatakuliah!!
        )
        setLokasiPresensi(
            lokasi = dataJadwal!!.ruang?.namaRuang + " - " + dataJadwal!!.ruang?.kdRuang,
            visible = true
        )
        setActionBarTitle(actionType)
        showBackButton(true)
        mPresenter?.setupSession()
        initAuthenticator()
        if (actionType == Constants.TUTUP_PRESENSI) {
            imgBeacon.visibility = View.GONE
            setLokasiPresensi(false, "-")
            setActionStatus(false, "-")
            biometricPrompt.authenticate(promptInfo)
        } else {
            GlobalScope.launch(Dispatchers.Main) {
                when (connectBeacon(dataBeacon = dataJadwal!!.ruang?.beacon!!)) {
                    Constants.FOUND ->{
                        disconnectBeacon()
                        biometricPrompt.authenticate(promptInfo)
                    }
                    Constants.NOT_FOUND -> {
                        disconnectBeacon()
                        onError("Beacon tidak ditemukan, silahkan coba lagi")
                    }
                }
            }
        }
        if (actionType != Constants.TUTUP_PRESENSI) {
            swipeRefreshOtentikasiPresensi.setOnRefreshListener {
                GlobalScope.launch(Dispatchers.Main) {
                    when (connectBeacon(dataBeacon = dataJadwal!!.ruang?.beacon!!)) {
                        Constants.FOUND ->{
                            disconnectBeacon()
                            biometricPrompt.authenticate(promptInfo)
                        }
                        Constants.NOT_FOUND -> {
                            disconnectBeacon()
                            onError("Beacon tidak ditemukan, silahkan coba lagi")
                        }
                    }
                }
            }
        } else {
            swipeRefreshOtentikasiPresensi.isEnabled = false
        }
    }

    override fun getIntentExtraData() {
        actionType = intent.getIntExtra(Constants.ACTION_TYPE, 0)
        dataJadwal = intent.getParcelableExtra(Constants.DATA_JADWAL)
        if (dataJadwal == null) {
            onError("Gagal mengambil data jadwal!")
            finish()
        }
    }

    override fun setActionBarTitle(type: Int) {
        when (type) {
            Constants.BUKA_PRESENSI -> title = "Buka Sesi Presensi"
            Constants.TUTUP_PRESENSI -> title = "Tutup Sesi Presensi"
            Constants.CATAT_PRESENSI -> title = "Catat Presensi"
            else -> {
                onError("Gagal mengambil tipe aksi!")
                finish()
            }
        }
    }

    override fun setNamaMatakuliah(matakuliah: String) {
        tvNamaMatakuliah.text = matakuliah
    }

    override fun setLokasiPresensi(visible: Boolean, lokasi: String) {
        if (visible) tvLokasi.visibility = View.VISIBLE else tvLokasi.visibility = View.GONE
        tvLokasi.text = "Silahkan masuk ke $lokasi."
    }

    override fun setActionStatus(visible: Boolean, action: String) {
        if (visible) tvActionStatus.visibility = View.VISIBLE else tvActionStatus.visibility =
            View.GONE
        tvActionStatus.text = action
    }

    override suspend fun connectBeacon(dataBeacon: Beacon): Boolean {
        var found = false
        if (SystemRequirementManager.checkAllRequirementUsingDefaultDialog(this)) {
            // connecting to Cubeacon service when all requirements completed
            region = CBRegion(
                "com.trateg.bepresensimobile.ui.otentikasi_presensi",
                UUID.fromString(dataBeacon.macAddress),
                dataBeacon.major!!.toInt(),
                dataBeacon.minor!!.toInt()
            )
            cubeacon.addRangingListener { p0, p1 ->
                if (p0?.size!! > 0) {
                    p0.forEach {
                        if (it.accuracy < Constants.BEACON_RANGE) {
                            isBeaconFound = true
                            Log.d("Otentikasi", "didRangeBeaconsInRegion: Beacon detected in " + it.accuracy + " meters.")
                        }
                    }
                }
            }
            cubeacon.connect(this)

            showProgress()
            setActionStatus(action = "Memindai beacon ${dataBeacon.kdBeacon}", visible = true)

            // Wait until timeout or beacon found
            val result = withTimeoutOrNull(Constants.REQUEST_TIMEOUT) {
                while (!found) {
                    if(isBeaconFound == true){
                        found = isBeaconFound
                    }
                    delay(500)
                }
            }
            hideProgress()
            if (result != null) {
                setActionStatus(action = "Beacon ${dataBeacon.kdBeacon} ditemukan", visible = true)
            }else{
                setActionStatus(action = "-", visible = false)
            }
        }
        return found
    }

    override fun disconnectBeacon() {
        cubeacon.disconnect(this)
    }

    override fun initAuthenticator() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    showToast("Otentikasi error: $errString")
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    mPresenter?.onUserAuthenticated(actionType, dataJadwal!!)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    showToast("Otentikasi gagal!")
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Otentikasi Biometrik")
            .setSubtitle("Lakukan otentikasi untuk melanjutkan")
            .setNegativeButtonText("Batalkan otentikasi")
            .build()
    }

    override fun initSession(): SessionManager {
        return SessionManager(baseContext)
    }

    override fun onError(msg: String) {
        Snackbar.make(swipeRefreshOtentikasiPresensi, msg, Snackbar.LENGTH_INDEFINITE)
            .setAction("TUTUP") {
                // tutup snackbar
                // biarkan kosong
            }.show()
    }

    override fun onBukaSesiPresensiSuccess(data: BaseResponse) {
        showToast("Berhasil membuka sesi presensi!")
        goToHome()
    }

    override fun onTutupSesiPresensiSuccess(data: BaseResponse) {
        showToast("Berhasil menutup sesi presensi!")
        goToHome()
    }

    override fun onCatatPresensiSuccess(data: BaseResponseList) {
        showToast("Berhasil catat presensi!")
        goToHome()
    }

    override fun showBackButton(enabled: Boolean) {
        if (supportActionBar != null) supportActionBar!!.setDisplayHomeAsUpEnabled(enabled)
    }

    override fun showToast(msg: String) {
        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun goToHome() {
        detachPresenter()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun attachPresenter(presenter: OtentikasiPresensiContract.Presenter) {
        mPresenter = presenter
    }

    override fun onDestroy() {
        super.onDestroy()
        disconnectBeacon()
        detachPresenter()
    }

    override fun detachPresenter() {
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun showProgress() {
        swipeRefreshOtentikasiPresensi.isRefreshing = true
    }

    override fun hideProgress() {
        swipeRefreshOtentikasiPresensi.isRefreshing = false
    }

    override fun onBeaconServiceConnect() {
        try {
            // start monitoring beacon using region
            cubeacon.startRangingBeaconsInRegion(region)
        } catch (e: RemoteException) {
            Log.e("onBeaconServiceConnect", "Error while start monitoring beacon, " + e)
        }
    }
}