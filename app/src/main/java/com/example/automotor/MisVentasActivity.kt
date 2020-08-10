package com.example.automotor

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.automotor.utils.ApiRest
import com.example.automotor.utils.MyCall
import com.example.automotor.utils.RetrofitBuilder
import com.example.automotor.utils.entities.AccessToken
import com.example.automotor.utils.entities.Venta
import kotlinx.android.synthetic.main.activity_mis_ventas.*
import java.util.*

class MisVentasActivity : AppCompatActivity(), InterfazAdapterMisVentas {

    private val tokenManager: AccessToken by lazy{
        AccessToken.getToken(applicationContext?.getSharedPreferences("prefs", Context.MODE_PRIVATE)!!)
    }

    private val service: ApiRest by lazy {
        RetrofitBuilder.createServiceWithAuth(ApiRest::class.java, tokenManager)
    }

    private val viewAdapter: MyAdapter = MyAdapter(mutableListOf(), this)


    override fun onClick(venta: Venta, position: Int, view: View) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_ventas)
        val viewManager = LinearLayoutManager(this)
        av_lista_venta.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        service.listVentas(tokenManager.user.id).enqueue(object : MyCall<MutableList<Venta>>(applicationContext) {
            override fun onSuccefull(valor: MutableList<Venta>?) {
                viewAdapter.setAll(valor)
                if((valor?.size?:0) == 0){
                    Toast.makeText(this@MisVentasActivity,"Ninguna Venta Registrada",Toast.LENGTH_LONG).show()
                }
            }
        })
    }


    class MyAdapter(private var myDataset: MutableList<Venta>, private val interfazAdapter: InterfazAdapterMisVentas) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

        class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view){
            val detalle: TextView = view.findViewById(R.id.vw_detalle)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
            val textView = LayoutInflater.from(parent.context).inflate(R.layout.item_venta, parent, false)
            return MyViewHolder(textView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val venta = myDataset[position]
            holder.detalle.text = Formatter().format("Vendedor: %s\n" +
                    "Comprador: %s\n" +
                    "Vehiculo: %s %s\n" +
                    "Aprobado: %s",venta.vendedor!!.name, venta.comprador!!.name,venta.vehiculo!!.marca,venta.vehiculo!!.modelo, if(venta.aprobado == true){"SI"}else{"NO"}).toString()

            holder.view.setOnClickListener {
                this.interfazAdapter.onClick(venta,position,holder.view)
            }
        }


        override fun getItemCount() = myDataset.size

        fun setAll(valor: MutableList<Venta>?) {
            this.myDataset.clear()
            this.myDataset.addAll(valor!!)
            this.notifyDataSetChanged()
        }

        fun editItem(position: Int, valor: Venta) {
            this.myDataset[position] = valor
            this.notifyItemChanged(position)
        }
    }


}

public interface InterfazAdapterMisVentas{
    abstract fun onClick(
        venta: Venta,
        position: Int,
        view: View
    )
}
