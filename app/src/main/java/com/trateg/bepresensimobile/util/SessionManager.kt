package com.trateg.bepresensimobile.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) : SessionManagerContract() {
    var sp: SharedPreferences = context.getSharedPreferences(Constants.PREF_NAME,Constants.PRIVATE_MODE)
    var editor: SharedPreferences.Editor  = sp.edit()

    override fun logout() {
        editor.also {
            it.clear()
            it.commit()
        }
    }

    override fun setLogin(status: Boolean) {
        editor.also {
            it.putBoolean(Constants.KEY_IS_LOGIN, status)
            it.commit()
        }
    }

    override fun setToken(token: String) {
        editor.also {
            it.putString(Constants.KEY_TOKEN, token)
            it.commit()
        }
    }

    override fun setNama(nama: String) {
        editor.also {
            it.putString(Constants.KEY_NAMA, nama)
            it.commit()
        }
    }

    override fun setEmail(email: String) {
        editor.also {
            it.putString(Constants.KEY_EMAIL, email)
            it.commit()
        }
    }

    override fun setIdUser(kdUser: Int) {
        editor.also {
            it.putInt(Constants.KEY_ID_USER, kdUser)
            it.commit()
        }
    }

    override fun setNIM(NIM: String) {
        editor.also {
            it.putString(Constants.KEY_NIM, NIM)
            it.commit()
        }
    }

    override fun setKdDosen(kdDosen: String) {
        editor.also {
            it.putString(Constants.KEY_KD_DOSEN, kdDosen)
            it.commit()
        }
    }

    override fun setRole(role: String) {
        editor.also {
            it.putString(Constants.KEY_ROLE, role)
            it.commit()
        }
    }

    override fun isLogin(): Boolean {
        return sp.getBoolean(Constants.KEY_IS_LOGIN, false)
    }

    override fun getToken(): String {
        return sp.getString(Constants.KEY_TOKEN, "") ?: ""
    }

    override fun getNama(): String {
        return sp.getString(Constants.KEY_NAMA, "") ?: ""
    }

    override fun getEmail(): String {
        return sp.getString(Constants.KEY_EMAIL, "") ?: ""
    }

    override fun getIdUser(): Int {
        return sp.getInt(Constants.KEY_ID_USER, 0)
    }

    override fun getNIM(): String {
        return sp.getString(Constants.KEY_NIM, "") ?: ""
    }

    override fun getKdDosen(): String {
        return sp.getString(Constants.KEY_KD_DOSEN, "") ?: ""
    }

    override fun getRole(): String {
        return sp.getString(Constants.KEY_ROLE, "") ?: ""
    }

}