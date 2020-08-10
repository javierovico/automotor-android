package com.example.automotor.utils.entities

import com.squareup.moshi.Json

class User {

    val rolString: String
        get() = when(this.rol_id){
            1 -> "Admin"
            2 -> "Vendedor"
            3 -> "Cajero"
            4 -> "Comprador"
            5 -> "Visitante"
            else -> "Desconocido"
        }

    @Json(name="id")
    var id: Int? = null

    @Json(name="email")
    var email: String? = null

    @Json(name="documento")
    var documento: String? = null

    @Json(name="name")
    var name: String? = null

    @Json(name="apellido")
    var apellido: String? = null

    @Json(name="email_verified_at")
    var email_verified_at: String? = null

    @Json(name="telefono")
    var telefono: String? = null

    @Json(name="rol_id")
    var rol_id: Int? = null

    @Json(name="created_at")
    var created_at: String? = null

    @Json(name="updated_at")
    var updated_at: String? = null

    public companion object{
        val ROLS = mutableListOf(
            "admin",
            "vendedor",
            "cajero",
            "comprador",
            "visitante"
        )

        const val admin = 1
        const val vendedor = 2
        const val cajero = 3
        const val comprador = 4
        const val visitante = 5

    }

}