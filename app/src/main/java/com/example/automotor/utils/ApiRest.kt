package com.example.automotor.utils

import com.example.automotor.utils.entities.AccessToken
import com.example.automotor.utils.entities.User
import com.example.automotor.utils.entities.Vehiculo
import com.example.automotor.utils.entities.Venta
import retrofit2.Call
import retrofit2.http.*

interface ApiRest {

    @POST("auth/login")
    @FormUrlEncoded
    fun login(@Field("email") username: String?, @Field("password") password: String?): Call<AccessToken>


    @POST("auth/signup")
    @FormUrlEncoded
    fun signup(
        @Field("email") email: String?,
        @Field("password") password: String?,
        @Field("password_confirmation") passwordConfirmation: String?,
        @Field("name") nombre: String?,
        @Field("apellido") apellido: String?,
        @Field("documento") documento: String?,
        @Field("telefono") telefono: String?
    ): Call<Any?>

    @GET("admin/user")
    fun listUsers(): Call<MutableList<User>>

    @PUT("admin/user/{id}")
    fun editUser(@Path("id") id: Int, @Body user: User): Call<User>

    @POST("vehiculo")
    fun agregarVehiculo(@Body vehiculo: Vehiculo): Call<Vehiculo>

    @GET("vehiculo")
    fun listVehiculos(): Call<MutableList<Vehiculo>>

    @GET("venta")
    fun listVentas(@Query("vendedor_id") vendedorId: Int? = null): Call<MutableList<Venta>>

    @GET("vendedor")
    fun listVendedores(): Call<MutableList<User>>

    @POST("venta")
    fun solicitar(@Body venta: Venta): Call<Any>
}