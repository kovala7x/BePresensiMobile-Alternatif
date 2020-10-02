package com.trateg.bepresensimobile.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.model.Kehadiran
import java.util.*

class ListKehadiranAdapter(val listKehadiran: ArrayList<Kehadiran>) :
    RecyclerView.Adapter<ListKehadiranAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: ListKehadiranAdapter.OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: ListKehadiranAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Kehadiran)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvSesi: TextView = itemView.findViewById(R.id.tvSesi)
        var tvStatusHadir: TextView = itemView.findViewById(R.id.tvStatusHadir)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListKehadiranAdapter.ListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kehadiran_sesi, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListKehadiranAdapter.ListViewHolder, position: Int) {
        val kehadiran = listKehadiran[position]
        val sesi = "Sesi " + kehadiran.kdSesi
        holder.tvSesi.text = sesi
        holder.tvStatusHadir.text = kehadiran.statusPresensi?.keteranganPresensi
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listKehadiran[holder.adapterPosition]) }    }

    override fun getItemCount(): Int {
        return listKehadiran.count()
    }
}