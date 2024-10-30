package com.example.feedback3

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ExternalStorageManager(private val context: Context) {

    fun saveData(fileName: String, data: String) {
        if (isExternalStorageWritable()) {
            val file = File(context.getExternalFilesDir(null), fileName)
            val fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(data.toByteArray())
            fileOutputStream.close()
        }
    }

    fun readData(fileName: String): String {
        if (isExternalStorageReadable()) {
            val file = File(context.getExternalFilesDir(null), fileName)
            val fileInputStream = FileInputStream(file)
            val data = fileInputStream.readBytes().toString(Charsets.UTF_8)
            fileInputStream.close()
            return data
        }
        return ""
    }

    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    private fun isExternalStorageReadable(): Boolean {
        val state = Environment.getExternalStorageState()
        return state == Environment.MEDIA_MOUNTED || state == Environment.MEDIA_MOUNTED_READ_ONLY
    }
}