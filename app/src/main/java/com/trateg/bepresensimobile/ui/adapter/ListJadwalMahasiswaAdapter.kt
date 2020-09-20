package com.trateg.bepresensimobile.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.model.Jadwal
import java.util.*

class ListJadwalMahasiswaAdapter(val listJadwalMahasiswa: ArrayList<Jadwal>) :
    RecyclerView.Adapter<ListJadwalMahasiswaAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun OnBtnPresensiClicked(data: Jadwal?)
        fun OnBtnDetailPresensiClicked(data: Jadwal?)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvJenisPerkuliahan: TextView = itemView.findViewById(R.id.tvJenisPerkuliahan)
        var tvNamaMatakuliah: TextView = itemView.findViewById(R.id.tvNamaMatakuliah)
        var tvWaktuPerkuliahan: TextView = itemView.findViewById(R.id.tvWaktuPerkuliahan)
        var tvDosenPengajar: TextView = itemView.findViewById(R.id.tvDosenPengajar)
        var tvLokasi: TextView = itemView.findViewById(R.id.tvLokasi)
        var tvHadir: TextView = itemView.findViewById(R.id.tvHadir)
        var tvStatusHadir: TextView = itemView.findViewById(R.id.tvStatusHadir)
        var btnPresensi: Button = itemView.findViewById(R.id.btnPresensi)
        var btnDetailPresensi: Button = itemView.findViewById(R.id.btnDetailPresensi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jadwal_mahasiswa, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listJadwalMahasiswa.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var jadwal = listJadwalMahasiswa[position]
        val waktu: String = jadwal.sesiMulai?.jamMulai?.dropLast(3) + " - " + jadwal.sesiBerakhir?.jamBerakhir?.dropLast(3)
        val lokasi: String = jadwal.ruang?.namaRuang + " - " + jadwal.ruang?.kdRuang
        holder.tvJenisPerkuliahan.text = jadwal.jenisPerkuliahan
        holder.tvNamaMatakuliah.text = jadwal.matakuliah?.namaMatakuliah
        holder.tvWaktuPerkuliahan.text = waktu
        holder.tvDosenPengajar.text = jadwal.dosen?.namaDosen
        holder.tvLokasi.text = lokasi

        holder.tvStatusHadir.visibility = View.GONE
        holder.tvHadir.visibility = View.GONE
        holder.btnDetailPresensi.visibility = View.GONE
        holder.btnPresensi.visibility = View.GONE

        if (jadwal.kehadiran != null) { // Cek apakah terdapat data kehadiran
            holder.tvHadir.visibility = View.VISIBLE
            holder.tvStatusHadir.visibility = View.VISIBLE
            holder.btnDetailPresensi.visibility = View.VISIBLE
            holder.tvStatusHadir.text = jadwal.kehadiran ?: "null"
            if (!jadwal.sudahPresensi!!) { // Cek apakah sudah melakukan presensi
                if (jadwal.sesiPresensiDibuka!!) { // Cek apakah sesi presensi dibuka
                    holder.btnPresensi.visibility = View.VISIBLE
                }
            }
        }

        holder.btnPresensi.setOnClickListener {
            onItemClickCallback.OnBtnPresensiClicked(listJadwalMahasiswa[holder.adapterPosition])
        }
        holder.btnDetailPresensi.setOnClickListener {
            onItemClickCallback.OnBtnDetailPresensiClicked(listJadwalMahasiswa[holder.adapterPosition])
        }
    }
}