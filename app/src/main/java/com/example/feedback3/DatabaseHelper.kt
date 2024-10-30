package com.example.feedback3

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_NOVELAS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOVELAS")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "novelas.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NOVELAS = "novelas"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_DESCRIPTION = "description"

        private const val CREATE_TABLE_NOVELAS = "CREATE TABLE $TABLE_NOVELAS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TITLE TEXT, " +
                "$COLUMN_AUTHOR TEXT, " +
                "$COLUMN_DESCRIPTION TEXT)"
    }
}