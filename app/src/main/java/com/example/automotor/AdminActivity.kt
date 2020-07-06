package com.example.automotor

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.automotor.utils.entities.AccessToken
import kotlinx.android.synthetic.main.activity_admin.*


class AdminActivity : AppCompatActivity() {

    lateinit var tokenManager: AccessToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        tokenManager = AccessToken.getToken(getSharedPreferences("prefs", MODE_PRIVATE))
        if (tokenManager.accessToken == null) {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
        av_gestion_usuarios.setOnClickListener {
            startActivity(Intent(this,GestionUsuarioActivity::class.java))
        }
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
