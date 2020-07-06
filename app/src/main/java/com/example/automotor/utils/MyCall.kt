package com.example.automotor.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyCall<T : Any?>(private val context: Context) : Callback<T> {
    override fun onFailure(call: Call<T>, t: Throwable) {
        Toast.makeText(context,t.message.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if(!response.isSuccessful){
            val responseJson = JSONObject(response.errorBody()?.string()?:"{}")
            Toast.makeText(context,responseJson.getString("message"),Toast.LENGTH_LONG).show()
        }else{
            tokenManager = response.body()!!
            tokenManager.saveToken(context.getSharedPreferences("prefs",
                AppCompatActivity.MODE_PRIVATE
            ).edit())
            onSuccess()
        }
    }

}