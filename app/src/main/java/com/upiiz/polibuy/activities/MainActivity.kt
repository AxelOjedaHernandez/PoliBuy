package com.upiiz.polibuy.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.data.Producto
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.upiiz.polibuy.R
import com.upiiz.polibuy.adapters.ProductAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private val productList =  mutableListOf<Producto>()
    private var idUsuario: String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        idUsuario = intent.getStringExtra("idUsuario")
        val btnProductos = findViewById<Button>(R.id.btnProducto)
        val btnCarrito = findViewById<Button>(R.id.btnCarrito)
        val btnInicio = findViewById<Button>(R.id.btnInicio)

        recyclerView = findViewById(R.id.rvListaProductos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        productAdapter = ProductAdapter(
            productList, addCar = {producto -> productoEspecifico(producto) }
        )
        recyclerView.adapter = productAdapter
        cargarProductos()

        btnProductos.setOnClickListener {
            val ProductosIntent = Intent(this@MainActivity, ProductoActivity::class.java)
            ProductosIntent.putExtra("idUsuario", idUsuario)
            startActivity(ProductosIntent)
        }
        btnCarrito.setOnClickListener {
            val CarritoIntent = Intent(this@MainActivity, CarritoActivity::class.java)
            CarritoIntent.putExtra("idUsuario", idUsuario)
            startActivity(CarritoIntent)
        }
        btnInicio.setOnClickListener {
            val InicioIntent = Intent(this@MainActivity, MainActivity::class.java)
            InicioIntent.putExtra("idUsuario", idUsuario)
            startActivity(InicioIntent)
        }
    }

    private fun productoEspecifico(producto: Producto){
        val productoIntent = Intent(this@MainActivity, ProductoEspecificoActivity::class.java)
        productoIntent.putExtra("idProducto", producto.id)
        productoIntent.putExtra("nombreProducto", producto.nombre)
        productoIntent.putExtra("descripcionProducto", producto.descripcion) // Corregido
        productoIntent.putExtra("precioProducto", producto.precio)
        productoIntent.putExtra("cantidadProducto", producto.cantidad)
        productoIntent.putExtra("idUsuario", idUsuario)
        productoIntent.putExtra("urlImagen", producto.urlImagen)
        startActivity(productoIntent)
    }

    private fun cargarProductos(){
        val database = Firebase.database
        val productRef = database.getReference("Producto")

        productRef.orderByKey().limitToLast(5).get().addOnSuccessListener { dataSnapshot ->
            productList.clear()
            for (snapshot in dataSnapshot.children) {
                val id = snapshot.child("id").value.toString()
                val nombre = snapshot.child("nombre").value.toString()
                val precio = snapshot.child("precio").getValue(Int::class.java)
                val cantidad = snapshot.child("cantidad").getValue(Int::class.java)
                val descripcion = snapshot.child("descripcion").value.toString()
                val urlImagen = snapshot.child("urlImagen").value.toString()

                val producto = Producto(
                    id = id,
                    nombre = nombre,
                    precio = precio,
                    cantidad = cantidad,
                    descripcion = descripcion,
                    urlImagen = urlImagen
                )

                productList.add(producto)
            }

            productAdapter.notifyDataSetChanged()
        }.addOnFailureListener {
            Toast.makeText(this, "Error al cargar contactos", Toast.LENGTH_SHORT).show()
        }
    }
}