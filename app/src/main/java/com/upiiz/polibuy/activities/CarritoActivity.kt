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
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.database
import com.upiiz.polibuy.R
import com.upiiz.polibuy.adapters.CarAdapter

class CarritoActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productCarAdapter: CarAdapter
    private val productList =  mutableListOf<Producto>()
    private var idUsuario: String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_carrito)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        idUsuario = intent.getStringExtra("idUsuario")

        recyclerView = findViewById(R.id.recycler_productos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        productCarAdapter = CarAdapter(
            productList
        )
        recyclerView.adapter = productCarAdapter
        cargarProductosDelCarrito(idUsuario)
    }

    private fun cargarProductosDelCarrito(idUsuario: String?) {
        val database = Firebase.database
        val carritosRef = database.getReference("Carritos")
        val productRef = database.getReference("Producto")

        productList.clear()

        carritosRef.get().addOnSuccessListener { dataSnapshot ->
            val productIds = mutableListOf<String>()

            for (carritoSnapshot in dataSnapshot.children) {
                val userIdInCarrito = carritoSnapshot.child("usuarioId").value.toString() //checar de la base de datos
                if (userIdInCarrito == idUsuario) {
                    val productId = carritoSnapshot.child("productoId").value.toString()
                    productIds.add(productId)
                }
            }

            val productTasks = productIds.map { productId ->
                productRef.child(productId).get()
            }

            Tasks.whenAllSuccess<DataSnapshot>(productTasks).addOnSuccessListener { snapshots ->
                for (snapshot in snapshots) {
                    val id = snapshot.child("id").value.toString()
                    val nombre = snapshot.child("nombre").value.toString()
                    val precio = snapshot.child("precio").getValue(Int::class.java)
                    val cantidad = snapshot.child("cantidad").getValue(Int::class.java)

                    val producto = Producto(
                        id = id,
                        nombre = nombre,
                        precio = precio,
                        cantidad = cantidad
                    )

                    productList.add(producto)
                }
                productCarAdapter.notifyDataSetChanged()
            }.addOnFailureListener {
                Toast.makeText(this, "Error al cargar productos", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error al cargar carritos", Toast.LENGTH_SHORT).show()
        }
    }

}