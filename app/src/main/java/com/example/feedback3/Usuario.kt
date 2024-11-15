package com.example.feedback3

data class Usuario(
    val id: String = "",
    val nombre: String = "",
    val apellido: String = ""
) {
    constructor(nombre: String, apellido: String) : this("", nombre, apellido)
}