package com.trateg.bepresensimobile.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.model.Kehadiran
import java.util.*

class ListDaftarHadirAdapter (val listDaftarHadir: ArrayList<Kehadiran>) :
    RecyclerView.Adapter<ListDaftarHadirAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Kehadiran)
    }


    class ListViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNama: TextView = itemView.findViewById(R.id.tvNama)
        var tvStatusHadir: TextView = itemView.findViewById(R.id.tvStatusHadir)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_daftar_hadir, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val kehadiran = listDaftarHadir[position]
        holder.tvNama.text = kehadiran.mahasiswa?.namaMahasiswa
        holder.tvStatusHadir.text = kehadiran.statusPresensi?.keteranganPresensi
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listDaftarHadir[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int {
        return listDaftarHadir.count()
    }
}