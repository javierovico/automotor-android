package com.example.automotor.utils.entities

import com.squareup.moshi.Json

class Venta{

    @Json(name = "id")
    var id : String? = null
    @Json(name = "primary")
    var primary : String? = null
    @Json(name = "vehiculo_id")
    var vehiculo_id : Int? = null
    @Json(name = "comprador_id")
    var comprador_id : Int? = null
    @Json(name = "vendedor_id")
    var vendedor_id : Int? = null
    @Json(name = "aprobado")
    var aprobado : Boolean? = null
    @Json(name = "fecha_venta")
    var fecha_venta : String? = null
    @Json(name = "vendedor")
    var vendedor : User? = null
    @Json(name = "comprador")
    var comprador : User? = null
    @Json(name = "vehiculo")
    var vehiculo : Vehiculo? = null
    @Json(name = "fecturas")
    var facturas : MutableList<Factura>? = null

}