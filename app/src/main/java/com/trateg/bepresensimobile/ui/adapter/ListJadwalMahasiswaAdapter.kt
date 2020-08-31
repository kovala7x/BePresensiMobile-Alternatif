package com.trateg.bepresensimobile.ui.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.model.Jadwal

class ListJadwalMahasiswaAdapter(val listJadwalMahasiswa: ArrayList<Jadwal>) :
    RecyclerView.Adapter<ListJadwalMahasiswaAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun OnItemClicked(data: Jadwal?)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvJenisPerkuliahan: TextView = itemView.findViewById(R.id.tvJenisPerkuliahan)
        var tvNamaMatakuliah: TextView = itemView.findViewById(R.id.tvNamaMatakuliah)
        var tvWaktuPerkuliahan: TextView = itemView.findViewById(R.id.tvWaktuPerkuliahan)
        var tvDosenPengajar: TextView = itemView.findViewById(R.id.tvDosenPengajar)
        var tvHadir: TextView = itemView.findViewById(R.id.tvHadir)
        var tvStatusHadir: TextView = itemView.findViewById(R.id.tvStatusHadir)
        var btnPresensi: Button = itemView.findViewById(R.id.btnPresensi)
        var btnDetailPresensi: Button = itemView.findViewById(R.id.btnDetailPresensi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_jadwal_mahasiswa,parent,false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listJadwalMahasiswa.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var jadwal = listJadwalMahasiswa[position]
        val waktu: String = jadwal.sesiMulai?.jamMulai + " - " + jadwal.sesiBerakhir?.jamBerakhir
        holder.tvJenisPerkuliahan.text = jadwal.jenisPerkuliahan
        holder.tvNamaMatakuliah.text = jadwal.matakuliah?.namaMatakuliah
        holder.tvWaktuPerkuliahan.text = waktu
        holder.tvDosenPengajar.text = jadwal.dosen?.namaDosen

        if(jadwal.kehadiran!=null){
            // Jika data kehadiran ada
            holder.tvStatusHadir.text = jadwal.kehadiran?.hadir
            holder.btnPresensi.visibility = View.GONE
            holder.btnDetailPresensi.visibility = View.VISIBLE
        }else{
            // Jika data kehadiran tidak ada
            holder.tvHadir.visibility = View.GONE
            holder.tvStatusHadir.visibility = View.GONE
            holder.btnDetailPresensi.visibility = View.GONE
            if(jadwal.sesiPresensiDibuka!!){
                // Jika sesi presensi dibuka
                holder.btnPresensi.visibility = View.VISIBLE
            }else holder.btnPresensi.visibility = View.GONE
        }
        holder.btnPresensi.setOnClickListener { onItemClickCallback.OnItemClicked(jadwal) }
        holder.btnDetailPresensi.setOnClickListener { onItemClickCallback.OnItemClicked(jadwal) }
    }
}