package com.upiiz.polibuy.activities


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
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
    private val productList = mutableListOf<Producto>()
    private var idUsuario: String? = ""
    private lateinit var tvTotal: TextView // TextView para mostrar el total
    private lateinit var tvArticulos: TextView

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
        tvTotal = findViewById(R.id.tv_total) // Vincula el TextView con su ID
        tvArticulos = findViewById(R.id.tv_articulos_carrito)

        val btnvolver = findViewById<Button>(R.id.btn_volver)
        val btnpago = findViewById<Button>(R.id.btn_proceder_pago)

        btnpago.setOnClickListener {
            val PagoIntent = Intent(this@CarritoActivity, PagoActivity::class.java)
            PagoIntent.putExtra("idUsuario", idUsuario)
            startActivity(PagoIntent)
        }
        btnvolver.setOnClickListener {
            val intent = Intent(this@CarritoActivity, MainActivity::class.java)
            intent.putExtra("idUsuario", idUsuario)
            startActivity(intent)
        }


        recyclerView = findViewById(R.id.recycler_productos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        productCarAdapter = CarAdapter(productList)
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
                val userIdInCarrito = carritoSnapshot.child("usuarioId").value.toString()
                if (userIdInCarrito == idUsuario) {
                    val productId = carritoSnapshot.child("productoId").value.toString()
                    productIds.add(productId)
                }
            }

            val productTasks = productIds.map { productId ->
                productRef.child(productId).get()
            }

            Tasks.whenAllSuccess<DataSnapshot>(productTasks).addOnSuccessListener { snapshots ->
                var totalPrice = 0
                var totalItems = 0

                for (snapshot in snapshots) {
                    val id = snapshot.child("id").value.toString()
                    val nombre = snapshot.child("nombre").value.toString()
                    val precio = snapshot.child("precio").getValue(Int::class.java) ?: 0
                    val cantidad = snapshot.child("cantidad").getValue(Int::class.java) ?: 1
                    val urlImagen = snapshot.child("urlImagen").value.toString()

                    val producto = Producto(
                        id = id,
                        nombre = nombre,
                        precio = precio,
                        cantidad = cantidad,
                        urlImagen = urlImagen
                    )

                    productList.add(producto)
                    totalPrice += precio * cantidad
                    totalItems += cantidad
                }
                productCarAdapter.notifyDataSetChanged()
                updateTotalText(totalPrice) // Actualiza el texto del total
                updateTotalItems(totalItems) // Actualiza el texto de los artículos
            }.addOnFailureListener {
                Toast.makeText(this, "Error al cargar productos", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error al cargar carritos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateTotalText(totalPrice: Int) {
        tvTotal?.let {
            val totalText = "Total: $${totalPrice}"
            it.text = totalText
        } ?: run {
            Toast.makeText(this, "Error al actualizar el total", Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateTotalItems(totalItems: Int) {
        val itemsText = "Artículos en carrito: $totalItems"
        tvArticulos.text = itemsText
    }
}