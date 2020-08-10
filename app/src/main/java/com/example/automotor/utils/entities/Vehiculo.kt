package com.example.automotor.utils.entities

import com.squareup.moshi.Json

class Vehiculo {
    @Json(name = "id")
    var id: Int? = null

    @Json(name = "marca")
    var marca: String? = null

    @Json(name = "modelo")
    var modelo: String? = null

    @Json(name = "anho")
    var anho: String? = null

    @Json(name = "tipo")
    var tipo: String? = null

    @Json(name = "precio")
    var precio: String? = null

    @Json(name = "kilometros")
    var kilometros: String? = null

}