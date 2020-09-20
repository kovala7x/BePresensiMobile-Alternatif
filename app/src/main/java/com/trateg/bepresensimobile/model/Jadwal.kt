package com.trateg.bepresensimobile.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Jadwal(
    @SerializedName("berita_acara")
    val beritaAcara: BeritaAcara?,
    @SerializedName("dosen")
    val dosen: Dosen?,
    @SerializedName("jam_presensi_dibuka")
    val jamPresensiDibuka: String?,
    @SerializedName("jam_presensi_ditutup")
    val jamPresensiDitutup: String?,
    @SerializedName("jenis_perkuliahan")
    val jenisPerkuliahan: String?,
    @SerializedName("kd_jadwal")
    val kdJadwal: String?,
    @SerializedName("kehadiran")
    val kehadiran: String?,
    @SerializedName("matakuliah")
    val matakuliah: Matakuliah?,
    @SerializedName("ruang")
    val ruang: Ruang?,
    @SerializedName("sesi_berakhir")
    val sesiBerakhir: SesiBerakhir?,
    @SerializedName("sesi_mulai")
    val sesiMulai: SesiMulai?,
    @SerializedName("sesi_presensi_dibuka")
    val sesiPresensiDibuka: Boolean?,
    @SerializedName("sudah_presensi")
    val sudahPresensi: Boolean?,
    @SerializedName("toleransi_keterlambatan")
    val toleransiKeterlambatan: Int?,
    @SerializedName("kd_dosen_pengajar")
    val kdDosenPengajar: String?,
    @SerializedName("kd_hari")
    val kdHari: Int?,
    @SerializedName("kd_kelas")
    val kdKelas: String?,
    @SerializedName("kd_matakuliah")
    val kdMatakuliah: String?,
    @SerializedName("kd_ruang")
    val kdRuang: String?,
    @SerializedName("kd_sesi_berakhir")
    val kdSesiBerakhir: Int?,
    @SerializedName("kd_sesi_mulai")
    val kdSesiMulai: Int?
) : Parcelable