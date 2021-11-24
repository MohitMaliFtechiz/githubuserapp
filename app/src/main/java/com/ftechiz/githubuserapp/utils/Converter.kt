package com.ftechiz.githubuserapp.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
class Converter {

var gson = Gson()

@TypeConverter
fun fromAmy(item: Any): String {
    return gson.toJson(item)
}

@TypeConverter
fun toAny(item: String): Any {
    val data = object : TypeToken<Any>() {
    }.type
    return gson.fromJson(item, data)
}
}