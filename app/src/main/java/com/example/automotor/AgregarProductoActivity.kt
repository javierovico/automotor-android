package com.example.automotor

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.automotor.utils.ApiRest
import com.example.automotor.utils.MyCall
import com.example.automotor.utils.RetrofitBuilder
import com.example.automotor.utils.entities.AccessToken
import com.example.automotor.utils.entities.Vehiculo
import kotlinx.android.synthetic.main.activity_agregar_producto.*

class AgregarProductoActivity : AppCompatActivity() {

    private val tokenManager: AccessToken by lazy{
        AccessToken.getToken(applicationContext?.getSharedPreferences("prefs", Context.MODE_PRIVATE)!!)
    }

    private val service: ApiRest by lazy {
        RetrofitBuilder.createServiceWithAuth(ApiRest::class.java, tokenManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_producto)
        av_guardar.setOnClickListener {
            val vehiculo = Vehiculo()
            vehiculo.marca =  iv_marca.text.toString()
            vehiculo.modelo =  iv_modelo.text.toString()
            vehiculo.anho =  iv_anho.text.toString()
            vehiculo.tipo =  iv_tipo.text.toString()
            vehiculo.precio =  iv_precio.text.toString()
            vehiculo.kilometros =  iv_kilometros.text.toString()
            service.agregarVehiculo(vehiculo).enqueue(object : MyCall<Vehiculo>(this@AgregarProductoActivity){
                override fun onSuccefull(valor: Vehiculo?) {
                    Toast.makeText(this@AgregarProductoActivity,"Creado!",Toast.LENGTH_LONG).show()
                    finish()
                }

            })
        }
    }
}
