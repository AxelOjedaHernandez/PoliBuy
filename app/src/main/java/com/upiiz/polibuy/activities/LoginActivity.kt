package com.upiiz.polibuy.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectofinal.data.Producto
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.upiiz.polibuy.R

class LoginActivity : AppCompatActivity() {
    private val database = Firebase.database
    private lateinit var storage: FirebaseStorage
    private val myRef = database.getReference("Usuarios")
    private val productRef = database.getReference("Producto")
    private var idUsuario: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        storage = FirebaseStorage.getInstance()

        val edtUsuario = findViewById<EditText>(R.id.et_email)
        val edtClave = findViewById<EditText>(R.id.et_password)
        val btnIngresar = findViewById<Button>(R.id.btn_login)
        val btnRegistrarse = findViewById<Button>(R.id.btn_registro)

        btnRegistrarse.setOnClickListener {
            val registrarseIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registrarseIntent)
        }

        btnIngresar.setOnClickListener {
            verificarUsuario(edtUsuario.text.toString().trim(), edtClave.text.toString().trim())
            //agregarProductos()
        }
    }

    private fun agregarProductos() {
        // Bucle para agregar 10 productos
        for (i in 1..10) {
            val productoId = productRef.push().key!! // Genera un nuevo ID único
            val nombre = "Producto $i" // Nombre dinámico para cada producto
            val descripcion = "Descripción del producto $i"
            val precio = (10..100).random() // Precio aleatorio entre 10 y 100
            val cantidad = (1..50).random() // Cantidad aleatoria entre 1 y 50

            // Obtener la referencia de la imagen en drawable
            val imageResource = resources.getIdentifier("img_$i", "drawable", packageName)
            val imageUri = Uri.parse("android.resource://$packageName/$imageResource")

            // Referencia a la ubicación en Storage para la imagen
            val storageRef = Firebase.storage.reference.child("productos/$productoId.jpg")

            // Cargar la imagen a Firebase Storage
            storageRef.putFile(imageUri).addOnSuccessListener { taskSnapshot ->
                // Obtener la URL de descarga de la imagen subida
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    val urlImagen = uri.toString()

                    // Crear el objeto Producto con los valores generados y la URL de imagen
                    val producto = Producto(productoId, nombre, descripcion, precio, cantidad, urlImagen)

                    // Agregar el producto a Firebase Database
                    productRef.child(productoId).setValue(producto).addOnSuccessListener {
                        if (i == 10) { // Mostrar el mensaje una vez, al finalizar el proceso
                            Toast.makeText(this, "10 productos agregados exitosamente", Toast.LENGTH_LONG).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, "Error al agregar el producto $i", Toast.LENGTH_LONG).show()
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error al subir la imagen del producto $i", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun verificarUsuario(usuario: String, clave: String) {
        // Validar que los campos no estén vacíos
        if (usuario.isEmpty() || clave.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese usuario y contraseña", Toast.LENGTH_LONG).show()
            return
        }

        // Consultar en la base de datos
        myRef.orderByChild("correo").equalTo(usuario)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var usuarioValido = false
                        for (userSnapshot in snapshot.children) {
                            val dbClave = userSnapshot.child("clave").value.toString()
                            if (dbClave == clave) {
                                usuarioValido = true
                                idUsuario = userSnapshot.child("id").value.toString()
                                break
                            }
                        }

                        if (usuarioValido) {
                            Toast.makeText(this@LoginActivity, "Ingreso exitoso", Toast.LENGTH_LONG).show()
                            // Redirigir a otra actividad después de un inicio exitoso
                            val menuIntent = Intent(this@LoginActivity, MainActivity::class.java)
                            menuIntent.putExtra("idUsuario", idUsuario)

                            startActivity(menuIntent)
                            finish() // Finalizar la actividad actual
                        } else {
                            Toast.makeText(this@LoginActivity, "Contraseña incorrecta", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Usuario no encontrado", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@LoginActivity, "Error al verificar usuario: ${error.message}", Toast.LENGTH_LONG).show()
                }
            })
    }
}