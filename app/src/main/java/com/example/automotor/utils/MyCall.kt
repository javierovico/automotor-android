package com.example.automotor.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class MyCall<T : Any?>(private val context: Context) : Callback<T> {

    abstract fun onSuccefull(valor: T?)

    override fun onFailure(call: Call<T>, t: Throwable) {
        Toast.makeText(context,t.message.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if(!response.isSuccessful){
            val responseJson = JSONObject(response.errorBody()?.string()?:"{}")
            Toast.makeText(context,responseJson.getString("message") + "\nCode: "+response.code() ,Toast.LENGTH_LONG).show()
        }else{
            this.onSuccefull(response.body())
        }
    }

}