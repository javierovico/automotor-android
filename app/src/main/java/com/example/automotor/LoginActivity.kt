package com.example.automotor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.automotor.utils.ApiRest
import com.example.automotor.utils.RetrofitBuilder
import com.example.automotor.utils.entities.AccessToken
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var tokenManager: AccessToken

    val service: ApiRest by lazy {
        RetrofitBuilder.createService(ApiRest::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tokenManager = AccessToken.getToken(getSharedPreferences("prefs", MODE_PRIVATE))
        if (tokenManager.accessToken != null) {
            onSuccess()
        }
        av_iniciar.setOnClickListener {
            val correo: String = iv_correo.text.toString()
            val password: String = iv_password.text.toString()
            service.login(correo,password).enqueue(object : Callback<AccessToken> {
                override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                    Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                    if(!response.isSuccessful){
                        val responseJson = JSONObject(response.errorBody()?.string()?:"{}")
                        Toast.makeText(applicationContext,responseJson.getString("message"),Toast.LENGTH_LONG).show()
                    }else{
                        tokenManager = response.body()!!
                        tokenManager.saveToken(getSharedPreferences("prefs", MODE_PRIVATE).edit())
                        onSuccess()
                    }
                }

            })
        }
        av_crear.setOnClickListener {
            startActivity(Intent(this@LoginActivity, CreateUserActivity::class.java))
        }
    }

    private fun onSuccess() {
        when(tokenManager.rolId){
            AccessToken.ROL_ADMIN -> {
                startActivity(Intent(this@LoginActivity, AdminActivity::class.java))
            }
            AccessToken.ROL_VISITANTE ->{
                startActivity(Intent(this@LoginActivity, AutomotorVisorActivity::class.java))
            }
            else ->{
                Toast.makeText(applicationContext,"Rol Desconocido",Toast.LENGTH_LONG).show()
            }
        }
        finish()
    }
}
