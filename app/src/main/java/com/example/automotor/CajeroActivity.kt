package com.example.automotor

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.automotor.utils.entities.AccessToken

class CajeroActivity : AppCompatActivity() {

    private val tokenManager: AccessToken by lazy{
        AccessToken.getToken(applicationContext?.getSharedPreferences("prefs", Context.MODE_PRIVATE)!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cajero)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.admin_menu, menu)
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
}
