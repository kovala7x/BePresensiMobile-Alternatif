package com.trateg.bepresensimobile.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.model.PersentaseKehadiran

class GridPersentaseKehadiranAdapter (var listPersentaseKehadiran: ArrayList<PersentaseKehadiran>) :
    RecyclerView.Adapter<GridPersentaseKehadiranAdapter.GridViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: PersentaseKehadiran)
    }

    class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvSesi: TextView = itemView.findViewById(R.id.tvSesi)
        var tvPersentase: TextView = itemView.findViewById(R.id.tvPersentase)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_grid_sesi, parent, false)
        return GridViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val persentaseKehadiran = listPersentaseKehadiran[position]
        val sesi = "Sesi " + persentaseKehadiran.kdSesi.toString()
        holder.tvSesi.text = sesi
        holder.tvPersentase.text = persentaseKehadiran.persentase
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listPersentaseKehadiran[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int {
        return listPersentaseKehadiran.count()
    }
}