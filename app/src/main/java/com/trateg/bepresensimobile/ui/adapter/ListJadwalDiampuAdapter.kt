package com.trateg.bepresensimobile.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.model.Jadwal

class ListJadwalDiampuAdapter(val listJadwalDiampu: ArrayList<Jadwal>)
    :RecyclerView.Adapter<ListJadwalDiampuAdapter.ListViewHolder>(){
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Jadwal)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNamaMatakuliah: TextView = itemView.findViewById(R.id.tvNamaMatakuliah)
        var tvKelasLokasi: TextView = itemView.findViewById(R.id.tvKelasLokasi)
        var tvWaktuPerkuliahan: TextView = itemView.findViewById(R.id.tvWaktuPerkuliahan)
        var tvToleransiTelat: TextView = itemView.findViewById(R.id.tvToleransiTelat)
        var tvJenisPerkuliahan: TextView = itemView.findViewById(R.id.tvJenisPerkuliahan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jadwal_diampu, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val jadwal = listJadwalDiampu[position]
        val kelasLokasi = jadwal.kdKelas + " - " + jadwal.ruang?.namaRuang + " " + jadwal.ruang?.kdRuang
        val waktu = jadwal.hari?.namaHari + ", " + jadwal.sesiMulai?.jamMulai?.dropLast(3) + " - " + jadwal.sesiBerakhir?.jamBerakhir?.dropLast(3)
        val toleransiWaktu = "Toleransi waktu " + jadwal.toleransiKeterlambatan.toString() + " menit"
        holder.tvNamaMatakuliah.text = jadwal.matakuliah?.namaMatakuliah
        holder.tvKelasLokasi.text = kelasLokasi
        holder.tvWaktuPerkuliahan.text = waktu
        holder.tvToleransiTelat.text = toleransiWaktu
        holder.tvJenisPerkuliahan.text = jadwal.jenisPerkuliahan
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listJadwalDiampu[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int {
        return listJadwalDiampu.size
    }
}