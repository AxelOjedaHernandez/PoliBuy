package com.example.proyectofinal.data

data class Producto(
    val id: String?=null,
    val nombre: String?=null,
    val descripcion: String?=null,
    val precio: Int?=null,
    var cantidad: Int?=null,
    val urlImagen: String?=null
)
