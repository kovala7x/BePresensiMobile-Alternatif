package com.trateg.bepresensimobile.model


import com.google.gson.annotations.SerializedName

data class Jadwal(
    @SerializedName("dosen")
    val dosen: Dosen?,
    @SerializedName("jenis_perkuliahan")
    val jenisPerkuliahan: String?,
    @SerializedName("kd_jadwal")
    val kdJadwal: String?,
    @SerializedName("kehadiran")
    val kehadiran: Kehadiran?,
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
    @SerializedName("toleransi_keterlambatan")
    val toleransiKeterlambatan: Int?
)