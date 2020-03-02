package com.example.firstkt

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.annotation.Nullable

class DBHelper(
    @Nullable
    context: Context?
) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "weatherDb"
        private const val TABLE_NAME = "cityinfo"
        private const val KEY_ID = "_id"
        private const val KEY_NAME = "name"
        private const val KEY_TYPE = "type"
        private const val KEY_WIN = "winter"
        private const val KEY_SPR = "spring"
        private const val KEY_SUM = "summer"
        private const val KEY_AUT = "autumn"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "create table $TABLE_NAME($KEY_ID integer primary key, $KEY_NAME text, $KEY_TYPE text, $KEY_WIN real, $KEY_SPR real, $KEY_SUM real, $KEY_AUT real)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}