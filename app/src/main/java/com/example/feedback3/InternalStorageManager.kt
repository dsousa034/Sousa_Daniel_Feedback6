package com.example.feedback3

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class InternalStorageManager(private val context: Context) {

    fun saveData(fileName: String, data: String) {
        val fileOutputStream: FileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        fileOutputStream.write(data.toByteArray())
        fileOutputStream.close()
    }

    fun readData(fileName: String): String {
        val fileInputStream: FileInputStream = context.openFileInput(fileName)
        val data = fileInputStream.readBytes().toString(Charsets.UTF_8)
        fileInputStream.close()
        return data
    }
}