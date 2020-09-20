package com.trateg.bepresensimobile.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SuratIzin(
    @SerializedName("catatan_surat")
    val catatanSurat: String?,
    @SerializedName("catatan_wali_dosen")
    val catatanWaliDosen: String?,
    @SerializedName("foto_surat")
    val fotoSurat: String?,
    @SerializedName("jam_mulai")
    val jamMulai: String?,
    @SerializedName("jam_selesai")
    val jamSelesai: String?,
    @SerializedName("jenis_izin")
    val jenisIzin: JenisIzin?,
    @SerializedName("kd_jenis_izin")
    val kdJenisIzin: String?,
    @SerializedName("kd_status_surat")
    val kdStatusSurat: Int?,
    @SerializedName("kd_surat_izin")
    val kdSuratIzin: String?,
    @SerializedName("mahasiswa")
    val mahasiswa: Mahasiswa?,
    @SerializedName("nim")
    val nim: String?,
    @SerializedName("status_surat")
    val statusSurat: StatusSurat?,
    @SerializedName("tgl_mulai")
    val tglMulai: String?,
    @SerializedName("tgl_selesai")
    val tglSelesai: String?
) : Parcelable