package com.example.automotor.utils

import com.example.automotor.utils.entities.AccessToken
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

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
    fun listUsers(): Call<MutableList<MutableMap<String,Any?>>>
}