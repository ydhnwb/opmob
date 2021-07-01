package com.ydhnwb.opaku_app.infra

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.ydhnwb.opaku_app.domain.profile.entity.UserEntity

@Suppress("UNCHECKED_CAST")
class SharedPrefs (private val context: Context) {
    companion object {
        private const val PREF = "MyAppPrefName"
        private const val PREF_TOKEN = "user_token"
        private const val PREF_USER_DATA = "user_data"
    }

    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)

    fun saveUserData(user: UserEntity){
        val userAsJson = Gson().toJson(user)
        put(PREF_USER_DATA, userAsJson)
    }

    fun getUserData() : UserEntity? {
        val userAsJson =  get(PREF_USER_DATA, String::class.java)
        if (userAsJson.isNotEmpty()) {
            return Gson().fromJson(userAsJson, UserEntity::class.java)
        }
        return null
    }

    fun saveToken(token: String){
        put(PREF_TOKEN, token)
    }

    fun getToken() : String {
        return get(PREF_TOKEN, String::class.java)
    }

    private fun <T> get(key: String, clazz: Class<T>): T =
        when (clazz) {
            String::class.java -> sharedPref.getString(key, "")
            Boolean::class.java -> sharedPref.getBoolean(key, false)
            Float::class.java -> sharedPref.getFloat(key, -1f)
            Double::class.java -> sharedPref.getFloat(key, -1f)
            Int::class.java -> sharedPref.getInt(key, -1)
            Long::class.java -> sharedPref.getLong(key, -1L)
            else -> null
        } as T

    private fun <T> put(key: String, data: T) {
        val editor = sharedPref.edit()
        when (data) {
            is String -> editor.putString(key, data)
            is Boolean -> editor.putBoolean(key, data)
            is Float -> editor.putFloat(key, data)
            is Double -> editor.putFloat(key, data.toFloat())
            is Int -> editor.putInt(key, data)
            is Long -> editor.putLong(key, data)
        }
        editor.apply()
    }

    fun clear() {
        sharedPref.edit().run {
            remove(PREF_TOKEN)
            remove(PREF_USER_DATA)
        }.apply()
    }
}