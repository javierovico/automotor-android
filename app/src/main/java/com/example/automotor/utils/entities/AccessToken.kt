package com.example.automotor.utils.entities

import android.content.SharedPreferences
import com.google.gson.Gson
import com.squareup.moshi.Json
import org.json.JSONObject

class AccessToken {
    @Json(name = "access_token")
    var accessToken: String? = null
    @Json(name = "rol_id")
    public var rolId: Int? = null
    @Json(name = "user")
    lateinit var user: User


    fun saveToken(editor: SharedPreferences.Editor) {
        editor.putString("ACCESS_TOKEN", accessToken).commit()
        editor.putInt("ID", rolId?:0).commit()
        editor.putString("USER",Gson().toJson(this.user)).commit()
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
            token.user = Gson().fromJson(prefs.getString("USER","{}"),User::class.java)
            if(token.user?.id == null){
                token.accessToken = null
                token.rolId = 0
            }
            return token
        }
    }
}