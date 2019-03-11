package com.hlandim.marvelheroes.database

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hlandim.marvelheroes.database.model.Participation
import java.util.Collections.emptyList


class Converter {

    private val gson = Gson()
    @TypeConverter
    fun stringToList(data: String?): List<Participation> {
        if (data == null) {
            return emptyList()
        }

        val listType = object : TypeToken<List<Participation>>() {

        }.type

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun ListToString(someObjects: List<Participation>): String {
        return gson.toJson(someObjects)
    }
}