package com.example.feedback3

import androidx.room.*
import androidx.room.Entity

@Entity(tableName = "novelas")
data class Novela(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String = "",
    val autor: String = "",
    val anoPublicacion: Int = 0,
    val sinopsis: String = "",
    var esFavorita: Boolean = false
) {
    constructor(titulo: String, autor: String, anoPublicacion: Int, sinopsis: String) : this(0, titulo, autor, anoPublicacion, sinopsis)
}