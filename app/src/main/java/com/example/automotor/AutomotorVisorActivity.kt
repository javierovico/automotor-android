package com.example.automotor

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
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
import com.example.automotor.utils.entities.Venta
import kotlinx.android.synthetic.main.activity_automotor_visor.*
import java.util.*

class AutomotorVisorActivity : AppCompatActivity(), InterfazAdapterVehiculo {

    lateinit var tokenManager: AccessToken

    private val service: ApiRest by lazy {
        RetrofitBuilder.createServiceWithAuth(ApiRest::class.java, tokenManager)
    }

    private val viewAdapter: MyAdapter = MyAdapter(mutableListOf(), this)

    override fun onClick(vehiculo: Vehiculo, position: Int, view: View) {
        val popup = PopupMenu(applicationContext, view)
        popup.menuInflater.inflate(R.menu.popup_menu_catalogo_vehiculo, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.action_vehiculo_solicitar ->  {
                    service.listVendedores().enqueue(object : MyCall<MutableList<User>>(this@AutomotorVisorActivity) {
                        override fun onSuccefull(valor: MutableList<User>?) {
                            if(valor?.size?:0 > 0){
                                val arrayString = mutableListOf<String>()
                                valor!!.forEach {
                                    arrayString.add((it.name?:"DESCONOCIDO") +" " + (it.apellido?:"DESCONOCIDO") +" (" + (it.telefono?:"DESCONOCIDO")+ ")")
                                }
                                val builder: AlertDialog.Builder = AlertDialog.Builder(this@AutomotorVisorActivity)
                                builder.setTitle("Seleccione un Vendedor")
                                builder.setItems(arrayString.toTypedArray()) { _, which ->
                                    val vendedorSelected = valor[which]
                                    val venta = Venta()
                                    venta.vehiculo_id = vehiculo.id
                                    venta.comprador_id = tokenManager.user.id
                                    venta.vendedor_id = vendedorSelected.id
                                    service.solicitar(venta).enqueue(object : MyCall<Any>(this@AutomotorVisorActivity){
                                        override fun onSuccefull(valor: Any?) {
                                            Toast.makeText(applicationContext, "Seleccionado el vendedor "+vendedorSelected.name + "\nEn breve se comunicaran con usted",Toast.LENGTH_LONG).show()
                                        }
                                    })
                                }
                                builder.show()
                            }else{
                                Toast.makeText(this@AutomotorVisorActivity,"No existe ningun vendedor actualmente",Toast.LENGTH_LONG).show()
                            }
                        }
                    })
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_automotor_visor)
        tokenManager = AccessToken.getToken(getSharedPreferences("prefs", MODE_PRIVATE))
        if (tokenManager.accessToken == null) {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.visor_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.av_cerrar_sesion -> {
                tokenManager.deleteToken(getSharedPreferences("prefs", MODE_PRIVATE).edit())
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    class MyAdapter(private var myDataset: MutableList<Vehiculo>, private val interfazAdapter: InterfazAdapterVehiculo) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

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

public interface InterfazAdapterVehiculo{
    abstract fun onClick(
        vehiculo: Vehiculo,
        position: Int,
        view: View
    )
}