package com.example.feedback3

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class NovelaDao(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun addNovela(novela: Novela): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_TITLE, novela.titulo)
            put(DatabaseHelper.COLUMN_AUTHOR, novela.autor)
            put(DatabaseHelper.COLUMN_DESCRIPTION, novela.sinopsis)
        }
        return db.insert(DatabaseHelper.TABLE_NOVELAS, null, values)
    }

    fun getAllNovelas(): List<Novela> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(DatabaseHelper.TABLE_NOVELAS, null, null, null, null, null, null)
        val novelas = mutableListOf<Novela>()
        with(cursor) {
            while (moveToNext()) {
                val title = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE))
                val author = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_AUTHOR))
                val description = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION))
                novelas.add(Novela(title, author, 0, description)) // Assuming `anioPublicacion` is not stored in DB
            }
        }
        cursor.close()
        return novelas
    }

    fun deleteNovela(titulo: String): Int {
        val db = dbHelper.writableDatabase
        return db.delete(DatabaseHelper.TABLE_NOVELAS, "${DatabaseHelper.COLUMN_TITLE} = ?", arrayOf(titulo))
    }
}