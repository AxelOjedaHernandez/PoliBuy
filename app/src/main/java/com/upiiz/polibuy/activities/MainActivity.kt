package com.example.proyectofinal.activities

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.adapters.ProductAdapter
import com.example.proyectofinal.data.Producto
import com.example.proyectofinal.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.database.database

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private val productList =  mutableListOf<Producto>()
    private val idUsuario: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}