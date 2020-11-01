package com.trateg.bepresensimobile.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.model.SuratIzin
import com.trateg.bepresensimobile.util.TextHelper
import java.text.SimpleDateFormat
import java.util.*

class ListSuratIzinAdapter(val listSuratIzin: ArrayList<SuratIzin>) :
    RecyclerView.Adapter<ListSuratIzinAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: SuratIzin)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvJenisSurat: TextView = itemView.findViewById(R.id.tvJenisSurat)
        var tvTglSurat: TextView = itemView.findViewById(R.id.tvTglSurat)
        var tvKodeSurat: TextView = itemView.findViewById(R.id.tvKodeSurat)
        var tvNamaMhs: TextView = itemView.findViewById(R.id.tvNamaMhs)
        var tvStatusSurat: TextView = itemView.findViewById(R.id.tvStatusSurat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_surat_mahasiswa, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val surat = listSuratIzin[position]
        holder.tvJenisSurat.text = "Surat " + surat.jenisIzin?.keteranganPresensi
        holder.tvTglSurat.text = convertDate(surat.tglMulai!!)
        holder.tvKodeSurat.text = surat.kdSuratIzin
        holder.tvNamaMhs.text = TextHelper.captEachWord(surat.mahasiswa?.namaMahasiswa!!)
        holder.tvStatusSurat.text = surat.statusSurat?.keteranganSurat
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(surat) }
    }

    override fun getItemCount(): Int {
        return listSuratIzin.size
    }

    fun convertDate(date: String): String {
        var dateString = "-"
        try {
            val sourcePattern = "yyyy-M-d"
            val sdf = SimpleDateFormat(sourcePattern, Locale.getDefault())
            val tanggal: Date = sdf.parse(date)!!

            val targetPattern = "d MMM yyyy"
            val sdf2 = SimpleDateFormat(targetPattern, Locale.getDefault())
            dateString = sdf2.format(tanggal)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dateString
    }
}