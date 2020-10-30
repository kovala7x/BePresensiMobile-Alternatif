package com.trateg.bepresensimobile.util

class Constants {

    companion object {
        const val BASE_URL = "http://192.168.43.218:8000/api/"
        const val BEACON_TIMEOUT = 20000L
        const val REQUEST_TIMEOUT = 10000L
        const val PRIVATE_MODE = 0
        const val PREF_NAME = "UserManager"
        const val KEY_TOKEN = "token"
        const val KEY_IS_LOGIN = "isLogin"
        const val KEY_NAMA = "nama"
        const val KEY_EMAIL = "email"
        const val KEY_ID_USER = "idUser"
        const val KEY_NIM = "nim"
        const val KEY_KD_DOSEN = "kodeDosen"
        const val KEY_ROLE = "role"
        const val ACTION_TYPE = "action_type"
        const val BUKA_PRESENSI = 1
        const val TUTUP_PRESENSI = 2
        const val CATAT_PRESENSI = 3
        const val DATA_JADWAL = "data_jadwal"
        const val DATA_KEHADIRAN = "data_kehadiran"
        const val DATA_PERSENTASE = "data_persentase"
        const val FOUND = true
        const val NOT_FOUND = false
        const val BEACON_RANGE = 4.00
        const val SURAT_DISPEN = "D"
        const val SURAT_IZIN = "I"
        const val SURAT_SAKIT = "S"
    }
}