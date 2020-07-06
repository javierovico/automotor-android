package com.example.automotor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.automotor.utils.ApiRest
import com.example.automotor.utils.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_create_user.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateUserActivity : AppCompatActivity() {

    val service: ApiRest by lazy {
        RetrofitBuilder.createService(ApiRest::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        av_crear.setOnClickListener {
            val name = iv_name.text.toString()
            val apellido = iv_apellido.text.toString()
            val email = iv_correo.text.toString()
            val password = iv_password.text.toString()
            val telefono = iv_telefono.text.toString()
            val documento = iv_documento.text.toString()
            service.signup(email,password,password,name,apellido,documento,telefono).enqueue(object : Callback<Any?> {
                override fun onFailure(call: Call<Any?>, t: Throwable) {
                    Toast.makeText(applicationContext,t.message.toString(), Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<Any?>, response: Response<Any?>) {
                    if(!response.isSuccessful){
                        val responseJson = JSONObject(response.errorBody()?.string()?:"{}")
                        Toast.makeText(applicationContext,responseJson.getString("message"),Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(applicationContext,"Usuario creado correctamente",Toast.LENGTH_LONG).show()
                        finish()
                    }
                }

            })
        }
    }
}
