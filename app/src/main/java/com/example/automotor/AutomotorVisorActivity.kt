package com.example.automotor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.automotor.utils.entities.AccessToken

class AutomotorVisorActivity : AppCompatActivity() {

    lateinit var tokenManager: AccessToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_automotor_visor)
        tokenManager = AccessToken.getToken(getSharedPreferences("prefs", MODE_PRIVATE))
        if (tokenManager.accessToken == null) {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
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
}
