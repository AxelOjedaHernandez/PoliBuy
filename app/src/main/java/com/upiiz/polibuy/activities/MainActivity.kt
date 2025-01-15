package com.upiiz.polibuy.activities

import android.os.Bundle
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
    private val idUsuario: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recycler_productos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        productAdapter = ProductAdapter(
            productList, addCar = {producto -> productoEspecifico(producto) }
        )
        recyclerView.adapter = productAdapter
        cargarProductos()
    }

    private fun productoEspecifico(producto: Producto){

    }

    private fun cargarProductos(){
        val database = Firebase.database
        val productRef = database.getReference("Productos")

        productRef.orderByKey().limitToLast(5).get().addOnSuccessListener { dataSnapshot ->
            productList.clear()
            for (snapshot in dataSnapshot.children) {
                val id = snapshot.child("id").value.toString()
                val nombre = snapshot.child("nombre").value.toString()
                val precio = snapshot.child("precio").value.toString().toIntOrNull()
                val cantidad = snapshot.child("cantidad").value.toString().toIntOrNull()

                val producto = Producto(
                    id = id,
                    nombre = nombre,
                    precio = precio,
                    cantidad = cantidad
                )

                productList.add(producto)
            }

            productAdapter.notifyDataSetChanged()
        }.addOnFailureListener {
            Toast.makeText(this, "Error al cargar contactos", Toast.LENGTH_SHORT).show()
        }
    }
}