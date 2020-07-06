package com.example.automotor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.automotor.utils.ApiRest
import com.example.automotor.utils.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_gestion_usuario.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GestionUsuarioActivity : AppCompatActivity() {


    val service: ApiRest by lazy {
        RetrofitBuilder.createService(ApiRest::class.java)
    }

    private var viewAdapter: MyAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_usuario)

        service.listUsers().enqueue(object : Callback<MutableList<MutableMap<String, Any?>>> {
            override fun onFailure(call: Call<MutableList<MutableMap<String, Any?>>>, t: Throwable) {

            }

            override fun onResponse(call: Call<MutableList<MutableMap<String, Any?>>>, response: Response<MutableList<MutableMap<String, Any?>>>) {

            }

        })

        val viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(mutableListOf("Hola","Chau"))

        vw_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

    }

    class MyAdapter(private val myDataset: MutableList<String>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

        class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view){
            val name: TextView = view.findViewById(R.id.vw_name)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
            val textView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
            return MyViewHolder(textView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.name.text = myDataset[position]
        }


        override fun getItemCount() = myDataset.size
    }
}
