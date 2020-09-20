package com.trateg.bepresensimobile.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Kehadiran(
    @SerializedName("jadwal")
    val jadwal: Jadwal?,
    @SerializedName("jam_presensi")
    val jamPresensi: String?,
    @SerializedName("jam_presensi_dibuka")
    val jamPresensiDibuka: String?,
    @SerializedName("kd_jadwal")
    val kdJadwal: String?,
    @SerializedName("kd_kehadiran")
    val kdKehadiran: Int?,
    @SerializedName("kd_sesi")
    val kdSesi: Int?,
    @SerializedName("kd_status_presensi")
    val kdStatusPresensi: String?,
    @SerializedName("kd_surat_izin")
    val kdSuratIzin: String?,
    @SerializedName("mahasiswa")
    val mahasiswa: Mahasiswa?,
    @SerializedName("nim")
    val nim: String?,
    @SerializedName("sesi")
    val sesi: Sesi?,
    @SerializedName("status_presensi")
    val statusPresensi: StatusPresensi?,
    @SerializedName("tgl_presensi")
    val tglPresensi: String?
) : Parcelable