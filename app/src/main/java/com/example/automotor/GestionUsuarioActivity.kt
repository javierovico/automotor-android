package com.example.automotor

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.automotor.utils.ApiRest
import com.example.automotor.utils.MyCall
import com.example.automotor.utils.RetrofitBuilder
import com.example.automotor.utils.entities.AccessToken
import com.example.automotor.utils.entities.User
import kotlinx.android.synthetic.main.activity_gestion_usuario.*
import retrofit2.Callback
import java.util.*


class GestionUsuarioActivity : AppCompatActivity(), InterfazAdapter {

    private val tokenManager: AccessToken by lazy{
        AccessToken.getToken(applicationContext?.getSharedPreferences("prefs", Context.MODE_PRIVATE)!!)
    }

    private val service: ApiRest by lazy {
        RetrofitBuilder.createServiceWithAuth(ApiRest::class.java, tokenManager)
    }

    private val viewAdapter: MyAdapter =  MyAdapter(mutableListOf(),this)


    override fun onClick(user: User, position: Int, view: View) {
        val popup = PopupMenu(applicationContext, view)
        popup.menuInflater.inflate(R.menu.popup_menu_user_admin, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.action_user_change_rol ->  {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this@GestionUsuarioActivity)
                    builder.setTitle("Seleccione Nuevo Rol")
                    builder.setItems(User.ROLS.toTypedArray()) { _, which ->
                        user.rol_id = which+1
                        service.editUser(user.id!!,user).enqueue(object : MyCall<User>(this@GestionUsuarioActivity){
                            override fun onSuccefull(valor: User?) {
                                viewAdapter.editItem(position,valor!!)
                            }
                        })
                        Toast.makeText(applicationContext,User.ROLS[which],Toast.LENGTH_LONG).show()

                    }
                    builder.show()
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
        setContentView(R.layout.activity_gestion_usuario)

        service.listUsers().enqueue(object : MyCall<MutableList<User>>(applicationContext) {
            override fun onSuccefull(valor: MutableList<User>?) {
                viewAdapter.setAll(valor)
            }

        })
        val viewManager = LinearLayoutManager(this)
        vw_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

    }

    class MyAdapter(private var myDataset: MutableList<User>,private val interfazAdapter: InterfazAdapter) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

        class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view){
            val name: TextView = view.findViewById(R.id.vw_name)
            val email = view.findViewById<TextView>(R.id.vw_email)
            val documento = view.findViewById<TextView>(R.id.vw_documento)
            val telefono = view.findViewById<TextView>(R.id.vw_telefono)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
            val textView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
            return MyViewHolder(textView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val user = myDataset[position]
            holder.name.text = Formatter().format("%s %s - Rol: %s",user.name, user.apellido,user.rolString).toString()
            holder.email.text = user.email
            holder.documento.text = user.documento
            holder.telefono.text = user.telefono
            holder.view.setOnClickListener {
                this.interfazAdapter.onClick(user,position,holder.view)
            }
        }


        override fun getItemCount() = myDataset.size

        fun setAll(valor: MutableList<User>?) {
            this.myDataset.clear()
            this.myDataset.addAll(valor!!)
            this.notifyDataSetChanged()
        }

        fun editItem(position: Int, valor: User) {
            this.myDataset[position] = valor
            this.notifyItemChanged(position)
        }
    }
}

public interface InterfazAdapter{
    abstract fun onClick(
        user: User,
        position: Int,
        view: View
    )
}
