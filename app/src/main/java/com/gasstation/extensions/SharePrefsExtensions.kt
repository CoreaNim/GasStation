package com.gasstation.extensions

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


fun SharedPreferences.string(
    key: (KProperty<*>) -> String = KProperty<*>::name,
    defaultValue: String = ""
): ReadWriteProperty<Any, String> = object : ReadWriteProperty<Any, String> {
    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) =
        edit().putString(key(property), value).apply()

    override fun getValue(thisRef: Any, property: KProperty<*>): String =
        getString(key(property), defaultValue)!!
}
