package com.searchbuddy.searchbuddy.Utils

import android.content.Context
import android.content.SharedPreferences

class LocalSessionManager {
    companion object{


        @Synchronized
        fun saveSession(name:String,value:String,context: Context):Boolean
        {

            var sharedPreferences: SharedPreferences =GlobalObject.getSharedPreferences(context)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString(name, value)
            return editor.commit()

        }


        @Synchronized
        fun saveValue(name:String,value:String,context: Context):Boolean
        {

            var sharedPreferences: SharedPreferences =GlobalObject.getSharedPreferences(context)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString(name, value)
            return editor.commit()

        }

        @Synchronized
        fun saveValue(name:String,value:Float,context: Context):Boolean
        {

            var sharedPreferences: SharedPreferences =GlobalObject.getSharedPreferences(context)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putFloat(name, value)
            return editor.commit()

        }

        @Synchronized
        fun saveValue(name:String,value:Boolean,context: Context):Boolean
        {

            var sharedPreferences: SharedPreferences =GlobalObject.getSharedPreferences(context)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putBoolean(name, value)
            return editor.commit()

        }

        fun removeValue(key:String,context: Context):Boolean
        {
            var sharedPreferences: SharedPreferences =GlobalObject.getSharedPreferences(context)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.remove(key)
            return editor.commit()
        }


        fun deleteAll(context: Context)
        {
            var sharedPreferences: SharedPreferences =GlobalObject.getSharedPreferences(context)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

        }

        @Synchronized
        fun containsKey(key: String?,context: Context): Boolean {
            var sharedPreferences: SharedPreferences = GlobalObject.getSharedPreferences(context)
            return sharedPreferences.contains(key)
        }

        @Synchronized
        fun getStringValue(key: String?, defaultValue: String,context: Context): String? {
            var sharedPreferences: SharedPreferences =GlobalObject.getSharedPreferences(context)
            return sharedPreferences.getString(key, defaultValue)
        }

        @Synchronized
        fun getBoolValue(key: String?, defaultValue: Boolean,context: Context): Boolean? {
            var sharedPreferences: SharedPreferences =GlobalObject.getSharedPreferences(context)
            return sharedPreferences.getBoolean(key, defaultValue)
        }
        @Synchronized
        fun getFloatValue(key: String?, defaultValue: Float,context: Context): Float? {
            var sharedPreferences: SharedPreferences =GlobalObject.getSharedPreferences(context)
            return sharedPreferences.getFloat(key, defaultValue)
        }
        @Synchronized
        fun getIntValue(key: String?, defaultValue: Int,context: Context): Int? {
            var sharedPreferences: SharedPreferences =GlobalObject.getSharedPreferences(context)
            return sharedPreferences.getInt(key, defaultValue)
        }


    }
    enum class key{
        USER_ID,
        FCMTOKEN,
        DEVICEID,
        IPADDRESS,
        LOGGEDIN,
        HOMEDATA,
        PRODUCTCATDATA
    }
}