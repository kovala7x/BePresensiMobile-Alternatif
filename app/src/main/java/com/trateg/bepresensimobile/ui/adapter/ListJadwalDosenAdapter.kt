package com.trateg.bepresensimobile.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.model.Jadwal
import java.text.SimpleDateFormat
import java.util.*

class ListJadwalDosenAdapter(val listJadwalDosen: ArrayList<Jadwal>) :
    RecyclerView.Adapter<ListJadwalDosenAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun OnBtnBukaSesiClicked(data: Jadwal?)
        fun OnBtnDetailPresensiClicked(data: Jadwal?)
        fun OnBtnTutupSesiClicked(data: Jadwal?)
        fun OnBtnLihatPresensiClicked(data: Jadwal?)
        fun OnBtnIsiBeritaAcaraClicked(data: Jadwal?)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvJenisPerkuliahan: TextView = itemView.findViewById(R.id.tvJenisPerkuliahan)
        var tvNamaMatakuliah: TextView = itemView.findViewById(R.id.tvNamaMatakuliah)
        var tvWaktuPerkuliahan: TextView = itemView.findViewById(R.id.tvWaktuPerkuliahan)
        var tvLokasi: TextView = itemView.findViewById(R.id.tvLokasi)
        var tvStatusPerkuliahan: TextView = itemView.findViewById(R.id.tvStatusPerkuliahan)
        var btnBukaSesi: Button = itemView.findViewById(R.id.btnBukaSesi)
        var btnDetailPresensi: Button = itemView.findViewById(R.id.btnDetailPresensi)
        var btnTutupSesi: Button = itemView.findViewById(R.id.btnTutupSesi)
        var btnLihatPresensi: Button = itemView.findViewById(R.id.btnLihatPresensi)
        var btnIsiBeritaAcara: Button = itemView.findViewById(R.id.btnIsiBeritaAcara)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jadwal_dosen, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listJadwalDosen.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val jadwal = listJadwalDosen[position]
        val waktu: String =
            jadwal.sesiMulai?.jamMulai?.dropLast(3) + " - " + jadwal.sesiBerakhir?.jamBerakhir?.dropLast(
                3
            )
        val lokasi: String = jadwal.ruang?.namaRuang + " - " + jadwal.ruang?.kdRuang
        var status = "Waktu perkuliahan belum dimulai"
        holder.tvJenisPerkuliahan.text = jadwal.jenisPerkuliahan
        holder.tvNamaMatakuliah.text = jadwal.matakuliah?.namaMatakuliah
        holder.tvWaktuPerkuliahan.text = waktu
        holder.tvLokasi.text = lokasi

        holder.btnDetailPresensi.visibility = View.GONE
        holder.btnBukaSesi.visibility = View.GONE
        holder.btnTutupSesi.visibility = View.GONE
        holder.btnLihatPresensi.visibility = View.GONE
        holder.btnIsiBeritaAcara.visibility = View.GONE


        if (jadwal.beritaAcara == null) { // Cek apakah berita acara belum diisi
            if (jadwal.sesiPresensiDibuka!!) { // Cek apakah sesi presensi dibuka
                status = jadwal.statusHadir ?: "Perkuliahan sedang berlangsung"
                holder.btnTutupSesi.visibility = View.VISIBLE
                holder.btnLihatPresensi.visibility = View.VISIBLE
            } else { // Jika sesi presensi ditutup
                if (isValidBukaPresensi(
                        jadwal.sesiMulai?.jamMulai!!,
                        jadwal.sesiBerakhir?.jamBerakhir!!
                    )
                ) { // Jika sesi presensi bisa dibuka
                    status = "Perkuliahan siap dilaksanakan"
                    holder.btnBukaSesi.visibility = View.VISIBLE
                }
                if(jadwal.statusHadir !=null){
                    // Jika sudah perkuliahan telah selesai dilaksanakan
                    status = "Berita acara belum diisi!"
                    holder.btnIsiBeritaAcara.visibility = View.VISIBLE
                }
            }

        } else { // Jika berita acara sudah diisi
            status = "Perkuliahan selesai dilaksanakan"
            holder.btnDetailPresensi.visibility = View.VISIBLE
        }

        holder.tvStatusPerkuliahan.text = status
        holder.btnDetailPresensi.setOnClickListener {
            onItemClickCallback.OnBtnDetailPresensiClicked(listJadwalDosen[holder.adapterPosition])
        }
        holder.btnBukaSesi.setOnClickListener {
            onItemClickCallback.OnBtnBukaSesiClicked(listJadwalDosen[holder.adapterPosition])
        }
        holder.btnTutupSesi.setOnClickListener {
            onItemClickCallback.OnBtnTutupSesiClicked(listJadwalDosen[holder.adapterPosition])
        }
        holder.btnLihatPresensi.setOnClickListener {
            onItemClickCallback.OnBtnLihatPresensiClicked(listJadwalDosen[holder.adapterPosition])
        }
        holder.btnIsiBeritaAcara.setOnClickListener {
            onItemClickCallback.OnBtnIsiBeritaAcaraClicked(listJadwalDosen[holder.adapterPosition])
        }
        holder.btnDetailPresensi.setOnClickListener {
            onItemClickCallback.OnBtnDetailPresensiClicked(listJadwalDosen[holder.adapterPosition])
        }
    }


    private fun isValidBukaPresensi(jamSesiMulai: String, jamSesiBerakhir: String): Boolean {
        val pattern = "HH:mm:ss"
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        val currentTime = sdf.format(Calendar.getInstance().time)

        try {
            val jamSekarang: Date = sdf.parse(currentTime)!!
            val jamMulai: Date = sdf.parse(jamSesiMulai)!!
            val jamBerakhir: Date = sdf.parse(jamSesiBerakhir)!!
            return jamSekarang.after(jamMulai) && jamSekarang.before(jamBerakhir)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}