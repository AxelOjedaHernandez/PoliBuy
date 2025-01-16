package com.upiiz.polibuy.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.upiiz.polibuy.R
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.proyectofinal.data.Carrito
import com.example.proyectofinal.data.Usuario
import com.google.firebase.Firebase
import com.google.firebase.database.database


class ProductoEspecificoActivity : AppCompatActivity() {
    private val database = Firebase.database
    private val carritosRef = database.getReference("Carritos")

    private var idProducto: String?=""
    private var nombreProducto: String?=""
    private var descripcionProducto: String?=""
    private var precioProducto: Int=0
    private var cantidadProducto: Int=0
    private var idUsuario: String?=""
    private var urlImagen: String?=""

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
        descripcionProducto = intent.getStringExtra("descripcionProducto")
        precioProducto = intent.getIntExtra("precioProducto", 0) // Valor predeterminado: 0
        cantidadProducto = intent.getIntExtra("cantidadProducto", 0) // Valor predeterminado: 0
        urlImagen = intent.getStringExtra("urlImagen")
        idUsuario = intent.getStringExtra("idUsuario")

        val tvProductName = findViewById<TextView>(R.id.productName)
        val tvProductDescription = findViewById<TextView>(R.id.productDescription)
        val tvProductPrice = findViewById<TextView>(R.id.productPrice)
        val tvProductQuantity = findViewById<TextView>(R.id.productQuantity)
        val imgProducto = findViewById<ImageView>(R.id.productImage)

        val btnaddToCart = findViewById<Button>(R.id.addToCartButton)
        val btnBack = findViewById<Button>(R.id.backButton)

        // Asignar datos a los TextViews
        tvProductName.text = nombreProducto ?: "Producto no disponible"
        tvProductDescription.text = descripcionProducto ?: "Sin descripción"
        tvProductPrice.text = "Precio: ${precioProducto ?: "No disponible"}"
        tvProductQuantity.text = "Cantidad: ${cantidadProducto ?: "No disponible"}"

        // Cargar la imagen usando Glide
        Glide.with(this)
            .load(urlImagen) // URL de la imagen
            .placeholder(R.drawable.carrito) // Imagen temporal mientras carga
            .error(R.drawable.carrito) // Imagen en caso de error
            .into(imgProducto) // Asignar al ImageView correcto

        // Botón para agregar al carrito (sin funcionalidad por ahora)
        val btnAddToCart = findViewById<Button>(R.id.addToCartButton)
        btnAddToCart.setOnClickListener {
            agregarCarrito(idProducto, idUsuario)
        }
        btnBack.setOnClickListener {
            val intent = Intent(this@ProductoEspecificoActivity, MainActivity::class.java)
            intent.putExtra("idUsuario", idUsuario)
            startActivity(intent)
        }

    }

    private fun agregarCarrito(idProducto: String?, idUsuario: String?) {
        val carritoId = carritosRef.push().key!!

        // Crea el objeto Usuario con los valores
        val empleado = Carrito(carritoId, idProducto, idUsuario)
        carritosRef.child(carritoId).setValue(empleado).addOnSuccessListener {
            Toast.makeText(this, "Producto agregado exitosamente", Toast.LENGTH_LONG).show()
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Error al agregar Producto", Toast.LENGTH_LONG).show()
        }
    }
}