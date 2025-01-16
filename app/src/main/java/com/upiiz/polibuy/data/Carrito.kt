package com.example.proyectofinal.data

data class Carrito(
    val carritoId: String?, // ID único del carrito
    val productoId: String?, // ID del producto agregado al carrito
    val usuarioId: String?   // ID del usuario al que pertenece el carrito
)