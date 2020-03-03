package com.example.firstkt

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class MainRepository private constructor() : MainContract.Repository {

    private lateinit var dbHelper: DBHelper
    private lateinit var database: SQLiteDatabase
    private lateinit var contentValues: ContentValues
    private lateinit var cursor: Cursor

    private object Holder {
        val INSTANCE = MainRepository()
    }

    companion object {
        val instance: MainRepository by lazy { Holder.INSTANCE }
    }


    override fun initial(context: Context) {
        dbHelper = DBHelper(context)
        database = dbHelper.writableDatabase
        contentValues = ContentValues()
    }

    override fun loadCityListNames(): ArrayList<String>? {
        val listOfNames = mutableListOf<String>()
        cursor =
            database.query(DBHelper.TABLE_NAME, null, null, null, null, null, DBHelper.KEY_NAME)
        return if (cursor.moveToFirst()) {
            val nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME)
            do {
                listOfNames.add(cursor.getString(nameIndex))
            } while (cursor.moveToNext())
            cursor.close()
            listOfNames as ArrayList<String>
        } else {
            cursor.close()
            null
        }
    }

    override fun loadCityInfo(name: String, season: String): Map<String, Any> {
        val cityInfo: Map<String, Any>
        cursor = database.query(
            DBHelper.TABLE_NAME,
            null,
            DBHelper.KEY_NAME + " = ?",
            arrayOf(name),
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            val nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME)
            val typeIndex = cursor.getColumnIndex(DBHelper.KEY_TYPE)
            val tempIndex = when (season) {
                "Зима" ->
                    cursor.getColumnIndex(DBHelper.KEY_WIN)
                "Весна" ->
                    cursor.getColumnIndex(DBHelper.KEY_SPR)
                "Лето" ->
                    cursor.getColumnIndex(DBHelper.KEY_SUM)
                "Осень" ->
                    cursor.getColumnIndex(DBHelper.KEY_AUT)
                else ->
                    throw IllegalStateException("Unexpected value: $season")
            }
            cityInfo = mapOf(
                "name" to cursor.getString(nameIndex),
                "type" to cursor.getString(typeIndex),
                "season" to season,
                "midTemp" to cursor.getFloat(tempIndex)
            )
            cursor.close()
        } else throw IllegalStateException("Some with DB (cursor)")
        return cityInfo
    }

    override fun writeCityInfo(
        nameCity: String,
        typeCity: String,
        winterT: Float,
        springT: Float,
        summerT: Float,
        autumnT: Float
    ) {
        var isEq = false
        if (contentValues.size() > 0) {
            contentValues.clear()
        }
        contentValues.put(DBHelper.KEY_NAME, nameCity)
        contentValues.put(DBHelper.KEY_TYPE, typeCity)
        contentValues.put(DBHelper.KEY_WIN, winterT)
        contentValues.put(DBHelper.KEY_SPR, springT)
        contentValues.put(DBHelper.KEY_SUM, summerT)
        contentValues.put(DBHelper.KEY_AUT, autumnT)

        cursor = database.query(
            DBHelper.TABLE_NAME,
            null,
            DBHelper.KEY_NAME + " = ?",
            arrayOf(nameCity),
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            val nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME)
            isEq = cursor.getString(nameIndex) == nameCity
            cursor.close()
        }
        if (isEq) database.update(
            DBHelper.TABLE_NAME,
            contentValues,
            DBHelper.KEY_NAME + " = ?",
            arrayOf(nameCity)
        )
        else database.insert(DBHelper.TABLE_NAME, null, contentValues)
    }

    override fun onDestroy() {
        dbHelper.close()
        database.close()
        contentValues.clear()
        if (!cursor.isClosed) cursor.close()
    }
}