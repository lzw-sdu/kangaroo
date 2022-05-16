package com.faceunity.app_ptag.compat

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.faceunity.pta.pta_core.interfaces.FuStorageFieldInterface

/**
 *
 */
class SPStorageFieldImpl(context: Context, fileName: String) : FuStorageFieldInterface {

    private val sp by lazy {
        context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    }

    override fun set(key: String, value: String) {
        sp.edit {
            putString(key, value)
        }
    }

    override fun set(key: String, value: Int) {
        sp.edit {
            putInt(key, value)
        }
    }

    override fun set(key: String, value: Float) {
        sp.edit {
            putFloat(key, value)
        }
    }

    override fun set(key: String, value: Boolean) {
        sp.edit {
            putBoolean(key, value)
        }
    }

    override fun get(key: String): Any {
        TODO("Not yet implemented")
    }

    override fun getAsString(key: String): String {
        return sp.getString(key, "")!!
    }

    override fun getAsInt(key: String): Int {
        return sp.getInt(key, 0)
    }

    override fun getAsFloat(key: String): Float {
        return sp.getFloat(key, 0f)
    }

    override fun getAsBoolean(key: String): Boolean {
        return sp.getBoolean(key, false)
    }

    fun setStringSet(key: String, value: Set<String>) {
        sp.edit {
            putStringSet(key, value)
        }
    }

    fun getAsStringSet(key: String): Set<String> {
        return sp.getStringSet(key, emptySet())!!
    }
}