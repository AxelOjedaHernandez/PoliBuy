package com.upiiz.polibuy.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.upiiz.polibuy.R
import android.widget.Button
import android.widget.TextView


class ProductoEspecificoActivity : AppCompatActivity() {
    private var idProducto: String?=""
    private var nombreProducto: String?=""
    private var descripcioProducto: String?=""
    private var precioProducto: Int=0
    private var cantidadProducto: Int=0
    private var idUsuario: String?=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_producto_especifico)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        idProducto = intent.getStringExtra("idProducto")
        nombreProducto = intent.getStringExtra("nombreProducto")
        descripcioProducto = intent.getStringExtra("descripcioProducto")
        precioProducto = intent.getIntExtra("precioProducto", 0) // Valor predeterminado: 0
        cantidadProducto = intent.getIntExtra("cantidadProducto", 0) // Valor predeterminado: 0

        idUsuario = intent.getStringExtra("idUsuario")

        val tvProductName = findViewById<TextView>(R.id.productName)
        val tvProductDescription = findViewById<TextView>(R.id.productDescription)
        val tvProductPrice = findViewById<TextView>(R.id.productPrice)
        val tvProductQuantity = findViewById<TextView>(R.id.productQuantity)
        val btnaddToCart = findViewById<Button>(R.id.addToCartButton)

        // Asignar datos a los TextViews
        tvProductName.text = nombreProducto ?: "Producto no disponible"
        tvProductDescription.text = descripcioProducto ?: "Sin descripción"
        tvProductPrice.text = "Precio: ${precioProducto ?: "No disponible"}"
        tvProductQuantity.text = "Cantidad: ${cantidadProducto ?: "No disponible"}"

        // Botón para agregar al carrito (sin funcionalidad por ahora)
        val btnAddToCart = findViewById<Button>(R.id.addToCartButton)
        btnAddToCart.setOnClickListener {
            // Aquí podrías agregar la lógica para añadir al carrito
        }
    }
}