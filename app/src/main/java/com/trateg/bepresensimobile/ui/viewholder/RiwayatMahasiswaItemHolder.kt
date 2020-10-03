package com.trateg.bepresensimobile.ui.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.model.Jadwal

class RiwayatMahasiswaItemHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val itemNamaMatakuliah = itemView.findViewById(R.id.tvNamaMatakuliah) as TextView
    private val itemWaktuPerkuliahan = itemView.findViewById(R.id.tvWaktuPerkuliahan) as TextView
    private val itemJenisPerkuliahan = itemView.findViewById(R.id.tvJenisPerkuliahan) as TextView

    fun bindContent(jadwalItem: Jadwal){
        val waktuPerkuliahan =
            jadwalItem.sesiMulai?.jamMulai!!.dropLast(3) + " - " + jadwalItem.sesiBerakhir?.jamBerakhir!!.dropLast(3)
        itemNamaMatakuliah.text = jadwalItem.matakuliah?.namaMatakuliah
        itemWaktuPerkuliahan.text = waktuPerkuliahan
        itemJenisPerkuliahan.text = jadwalItem.jenisPerkuliahan
    }
}