package com.example.automotor.utils.entities

import android.content.SharedPreferences
import com.squareup.moshi.Json

class AccessToken {
    @Json(name = "access_token")
    var accessToken: String? = null
    @Json(name = "rol_id")
    public var rolId: Int? = null


    fun saveToken(editor: SharedPreferences.Editor) {
        editor.putString("ACCESS_TOKEN", accessToken).commit()
        editor.putInt("ID", rolId?:0).commit()
    }

    fun deleteToken(editor: SharedPreferences.Editor) {
        editor.remove("ACCESS_TOKEN").commit()
        editor.remove("ID").commit()
        editor.apply()
        accessToken = null
    }

    companion object{

        const val ROL_VISITANTE = 5
        const val ROL_ADMIN = 1

        @Synchronized
        fun getToken(prefs: SharedPreferences): AccessToken{
            val token = AccessToken()
            token.accessToken = prefs.getString("ACCESS_TOKEN", null)
            token.rolId = prefs.getInt("ID", 0)
            return token
        }
    }
}