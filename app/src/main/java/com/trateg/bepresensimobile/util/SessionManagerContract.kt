package com.trateg.bepresensimobile.util

import android.content.Context

abstract class SessionManagerContract {
    abstract fun logout()

    // setter
    abstract fun setLogin(status: Boolean)
    abstract fun setToken(token: String)
    abstract fun setNama(nama: String)
    abstract fun setEmail(email: String)
    abstract fun setIdUser(idUser: Int)
    abstract fun setNIM(NIM: String)
    abstract fun setKdDosen(kdDosen: String)
    abstract fun setRole(role: String)


    // getter
    abstract fun isLogin(): Boolean
    abstract fun getToken(): String
    abstract fun getNama(): String
    abstract fun getEmail():String
    abstract fun getIdUser(): Int
    abstract fun getNIM(): String
    abstract fun getKdDosen(): String
    abstract fun getRole(): String
}