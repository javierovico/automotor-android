package com.example.automotor

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.automotor.utils.ApiRest
import com.example.automotor.utils.MyCall
import com.example.automotor.utils.RetrofitBuilder
import com.example.automotor.utils.entities.AccessToken
import com.example.automotor.utils.entities.User
import com.example.automotor.utils.entities.Vehiculo
import kotlinx.android.synthetic.main.activity_mis_productos.*
import java.util.*

class MisProductosActivity : AppCompatActivity(), InterfazAdapterProducto {

    private val tokenManager: AccessToken by lazy{
        AccessToken.getToken(applicationContext?.getSharedPreferences("prefs", Context.MODE_PRIVATE)!!)
    }

    private val service: ApiRest by lazy {
        RetrofitBuilder.createServiceWithAuth(ApiRest::class.java, tokenManager)
    }

    private val viewAdapter: MyAdapter = MyAdapter(mutableListOf(), this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_productos)
        vw_agregar_nuevo_producto.setOnClickListener {
            startActivity(Intent(this,AgregarProductoActivity::class.java))
        }
        val viewManager = LinearLayoutManager(this)
        vw_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        service.listVehiculos().enqueue(object : MyCall<MutableList<Vehiculo>>(applicationContext) {
            override fun onSuccefull(valor: MutableList<Vehiculo>?) {
                viewAdapter.setAll(valor)
            }

        })
    }


    override fun onClick(vehiculo: Vehiculo, position: Int, view: View) {
        val popup = PopupMenu(applicationContext, view)
        popup.menuInflater.inflate(R.menu.popup_menu_mis_productos, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.action_user_change_rol ->  {

                }
            }
            Toast.makeText(
                applicationContext,
                "You Clicked : " + item.title,
                Toast.LENGTH_SHORT
            ).show()
            true
        }
        popup.show() //showing popup menu
    }


    class MyAdapter(private var myDataset: MutableList<Vehiculo>, private val interfazAdapter: InterfazAdapterProducto) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

        class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view){
            val name: TextView = view.findViewById(R.id.vw_name)
            val email = view.findViewById<TextView>(R.id.vw_email)
            val documento = view.findViewById<TextView>(R.id.vw_documento)
            val telefono = view.findViewById<TextView>(R.id.vw_telefono)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
            val textView = LayoutInflater.from(parent.context).inflate(R.layout.item_vehiculo, parent, false)
            return MyViewHolder(textView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val vehiculo = myDataset[position]
            holder.name.text = Formatter().format("%s - %s - anho: %s",vehiculo.marca, vehiculo.modelo,vehiculo.anho).toString()
            holder.email.text = vehiculo.precio
            holder.documento.text = vehiculo.kilometros
            holder.telefono.text = vehiculo.tipo
            holder.view.setOnClickListener {
                this.interfazAdapter.onClick(vehiculo,position,holder.view)
            }
        }


        override fun getItemCount() = myDataset.size

        fun setAll(valor: MutableList<Vehiculo>?) {
            this.myDataset.clear()
            this.myDataset.addAll(valor!!)
            this.notifyDataSetChanged()
        }

        fun editItem(position: Int, valor: Vehiculo) {
            this.myDataset[position] = valor
            this.notifyItemChanged(position)
        }
    }

}

public interface InterfazAdapterProducto{
    abstract fun onClick(
        vehiculo: Vehiculo,
        position: Int,
        view: View
    )
}
