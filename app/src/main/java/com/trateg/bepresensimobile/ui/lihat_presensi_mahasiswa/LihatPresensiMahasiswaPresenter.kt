package com.trateg.bepresensimobile.ui.lihat_presensi_mahasiswa

import java.text.SimpleDateFormat
import java.util.*

class LihatPresensiMahasiswaPresenter (private var mView: LihatPresensiMahasiswaContract.View?) :
    LihatPresensiMahasiswaContract.Presenter {
    override fun convertDate(date: String): String {
        var dateString = "-"
        try {
            val sourcePattern = "yyyy-M-d"
            val sdf = SimpleDateFormat(sourcePattern, Locale.getDefault())
            val tanggal: Date = sdf.parse(date)!!

            val targetPattern = "dd/MM/yyyy"
            val sdf2 = SimpleDateFormat(targetPattern, Locale.getDefault())
            dateString = sdf2.format(tanggal)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dateString
    }

    override fun onDestroy() {
        mView = null
    }
}