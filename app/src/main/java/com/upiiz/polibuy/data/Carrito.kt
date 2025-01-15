package com.example.proyectofinal.data

data class Carrito(
    val carritoId: Int, // ID único del carrito
    val productoId: Int, // ID del producto agregado al carrito
    val usuarioId: Int   // ID del usuario al que pertenece el carrito
)