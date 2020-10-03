package com.trateg.bepresensimobile.ui.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trateg.bepresensimobile.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class RiwayatMahasiswaHeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val itemHeaderTanggal = itemView.findViewById(R.id.tvHeaderTanggal) as TextView
    fun bindContent(text: String) {
        itemHeaderTanggal.text = convertDate(text)
    }

    fun convertDate(date: String): String {
        var dateString = "-"
        try {
            val sourcePattern = "yyyy-M-d"
            val sdf = SimpleDateFormat(sourcePattern, Locale.getDefault())
            val tanggal: Date = sdf.parse(date)!!
            dateString = DateFormat.getDateInstance(DateFormat.FULL).format(tanggal)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dateString
    }
}