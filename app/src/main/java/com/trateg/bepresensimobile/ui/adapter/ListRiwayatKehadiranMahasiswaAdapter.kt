package com.trateg.bepresensimobile.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trateg.bepresensimobile.R
import com.trateg.bepresensimobile.model.Jadwal
import com.trateg.bepresensimobile.ui.viewholder.RiwayatMahasiswaHeaderHolder
import com.trateg.bepresensimobile.ui.viewholder.RiwayatMahasiswaItemHolder

class ListRiwayatKehadiranMahasiswaAdapter(private val data: List<Any>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Jadwal)
    }

    companion object {
        private const val ITEM_HEADER = 0
        private const val ITEM_JADWAL = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is String -> ITEM_HEADER
            is Jadwal -> ITEM_JADWAL
            else -> throw IllegalArgumentException("Undefined view type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM_HEADER -> RiwayatMahasiswaHeaderHolder(
                inflater.inflate(
                    R.layout.item_header_riwayat_mahasiswa,
                    parent,
                    false
                )
            )
            ITEM_JADWAL -> RiwayatMahasiswaItemHolder(
                inflater.inflate(
                    R.layout.item_riwayat_mahasiswa,
                    parent,
                    false
                )
            )
            else -> throw throw IllegalArgumentException("Undefined view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ITEM_HEADER -> {
                val headerHolder = holder as RiwayatMahasiswaHeaderHolder
                headerHolder.bindContent(data[position] as String)
            }
            ITEM_JADWAL -> {
                val itemHolder = holder as RiwayatMahasiswaItemHolder
                itemHolder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(data[position] as Jadwal) }
                itemHolder.bindContent(data[position] as Jadwal)
            }
            else -> throw IllegalArgumentException("Undefined view type")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}