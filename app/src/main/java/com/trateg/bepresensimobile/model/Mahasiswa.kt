package com.trateg.bepresensimobile.model


import com.google.gson.annotations.SerializedName

data class Mahasiswa(
    @SerializedName("device_imei")
    val deviceImei: String?,
    @SerializedName("foto_mahasiswa")
    val fotoMahasiswa: String?,
    @SerializedName("id_user")
    val idUser: Int?,
    @SerializedName("kd_kelas")
    val kdKelas: String?,
    @SerializedName("nama_mahasiswa")
    val namaMahasiswa: String?,
    @SerializedName("nim")
    val nim: String?
)