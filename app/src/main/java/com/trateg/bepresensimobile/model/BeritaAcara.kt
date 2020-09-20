package com.trateg.bepresensimobile.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BeritaAcara(
    @SerializedName("desk_penugasan")
    val deskPenugasan: String?,
    @SerializedName("desk_perkuliahan")
    val deskPerkuliahan: String?,
    @SerializedName("jadwal")
    val jadwal: Jadwal?,
    @SerializedName("jam_presensi_dibuka")
    val jamPresensiDibuka: String?,
    @SerializedName("jam_presensi_ditutup")
    val jamPresensiDitutup: String?,
    @SerializedName("kd_berita_acara")
    val kdBeritaAcara: String?,
    @SerializedName("kd_jadwal")
    val kdJadwal: String?,
    @SerializedName("mhs_hadir")
    val mhsHadir: Int?,
    @SerializedName("mhs_tidak_hadir")
    val mhsTidakHadir: Int?,
    @SerializedName("tgl_pertemuan")
    val tglPertemuan: String?
) : Parcelable