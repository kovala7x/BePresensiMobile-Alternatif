package com.trateg.bepresensimobile.model


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("jadwal")
    val jadwal: Jadwal?,
    @SerializedName("auth")
    val auth: Auth?,
    @SerializedName("kehadiran")
    val kehadiran: Kehadiran?,
    @SerializedName("rekapitulasi")
    val rekapitulasi: Rekapitulasi?,
    @SerializedName("surat_izin")
    val suratIzin: SuratIzin?,
    @SerializedName("berita_acara")
    val beritaAcara: BeritaAcara?
)

data class ListData(
    @SerializedName("jadwal")
    val jadwal: List<Jadwal>?,
    @SerializedName("surat_izin")
    val suratIzin: List<SuratIzin>?,
    @SerializedName("kehadiran")
    val kehadiran: List<Kehadiran>?,
    @SerializedName("berita_acara")
    val beritaAcara: List<BeritaAcara>?,
    @SerializedName("persentase_kehadiran")
    val persentaseKehadiran: List<PersentaseKehadiran>?,
    @SerializedName("riwayat_kehadiran")
    val riwayatKehadiran: List<RiwayatKehadiran>?
)