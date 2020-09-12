package com.trateg.bepresensimobile.ui.login

import android.content.ContentValues.TAG
import android.util.Log
import com.auth0.android.jwt.JWT
import com.trateg.bepresensimobile.data.api.ApiFactory
import com.trateg.bepresensimobile.model.Auth
import com.trateg.bepresensimobile.model.BaseResponseList
import com.trateg.bepresensimobile.util.Constants
import com.trateg.bepresensimobile.util.ErrorUtils
import com.trateg.bepresensimobile.util.SessionManager
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

class LoginPresenter(private var mView: LoginContract.View?) :
    LoginContract.Presenter {
    private val api = ApiFactory.apiService
    lateinit var session: SessionManager
    override fun doLogin(email: String, password: String){

        val mEmail: RequestBody = email.toRequestBody(email.toMediaTypeOrNull())
        val mPassword: RequestBody = password.toRequestBody(password.toMediaTypeOrNull())
        var response: Response<BaseResponseList>? = null
        GlobalScope.launch(Dispatchers.IO){
        // Menampilkan loading
            withContext(Dispatchers.Main) {
                mView?.showProgress()
            }
            // Mencegah waktu request melebihi timeout
            val request = withTimeoutOrNull(Constants.REQUEST_TIMEOUT) {
                try {
                    response = api.postLogin(mEmail, mPassword)
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        mView?.onError(e.message.toString())
                        Log.d(TAG, "catch doLogin: " + e.message.toString())
                    }
                }
            }
            // Menghilangkan loading
            withContext(Dispatchers.Main) {
                mView?.hideProgress()
            }

            try {
                when {
                    request == null -> { // Jika waktu request melebihi timeout
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "doLogin: request is null!")
                            mView?.onError("Waktu tunggu telah habis...")
                        }
                    }

                    !response!!.isSuccessful -> { // Jika request gagal
                        val errorResponse = ErrorUtils.parseErrorList(response!!)
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "doLogin: " + errorResponse.message!!.toString())
                            mView?.onError(errorResponse.message!!.toString())
                        }
                    }

                    response!!.body() == null -> { // Jika body response kosong
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "doLogin: response body is null!")
                            mView?.onError("Silahkan coba lagi...")
                        }
                    }

                    !response!!.body()!!.success!! -> { // Jika request pada backend gagal
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "doLogin: " + response!!.body()!!.message!!)
                            mView?.onError(response!!.body()!!.message!!)
                        }
                    }

                    response!!.body()!!.success!! -> { // Jika request berhasil
                        withContext(Dispatchers.Main){
                            onLoginSuccess(response!!.body()!!.data?.auth?.first()!!)
                        }
                    }

                    else -> {
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "doLogin: uncaught error!")
                            mView?.onError("Silahkan coba lagi...")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Log.d(TAG, "doLogin: exception catch is called!")
                    mView?.onError("Terjadi kesalahan...")
                }
            }
        }
    }

    override fun onLoginSuccess(data: Auth) {
        val token = JWT(data.accessToken!!)
        val role: String = token.getClaim("role").asString() ?: "null"
        val idUser: String = token.getClaim("id_user").asInt().toString()
        var nama: String? = null
        var kdDosen: String? = null
        var NIM: String? = null
        val email: String? = token.getClaim("email").asString()

        session.also {
            it.setToken(token.toString())
            it.setRole(role)
            it.setIdUser(idUser.toInt())
            it.setEmail(email?:"null")
            it.setLogin(true)
        }
        when (role.toUpperCase()) {
            "STAFF TATA USAHA" -> { // Jika user adalah staff tata usaha
                mView?.onError("Maaf, fitur tidak tersedia...")
            }
            "DOSEN" -> { // Jika user adalah dosen
                nama = token.getClaim("nama_dosen").asString()
                kdDosen = token.getClaim("kd_dosen").asString()
                session.also {
                    it.setNama(nama?:"null")
                    it.setKdDosen(kdDosen?:"null")
                }
                navigateHome()
            }
            "MAHASISWA" -> {  // Jika user adalah mahasiswa
                nama = token.getClaim("nama_mahasiswa").asString()
                NIM = token.getClaim("nim").asString()
                session.also {
                    it.setNama(nama?:"null")
                    it.setNIM(NIM?:"null")
                }
                navigateHome()
            }
        }
    }

    override fun navigateHome() {
        mView?.goToHome()
    }

    override fun setupSession() {
        session = mView?.initSession()!!
        if(session.isLogin()) navigateHome() // Jika user sudah login
    }

    override fun onDestroy() {
        mView = null
    }
}