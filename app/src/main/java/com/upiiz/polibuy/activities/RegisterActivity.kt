package com.example.proyectofinal.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectofinal.data.Usuario
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.upiiz.polibuy.R
import com.upiiz.polibuy.activities.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private val database = Firebase.database
    private val myRef = database.getReference("Usuarios")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnGuardar = findViewById<Button>(R.id.btn_register)

        btnGuardar.setOnClickListener{
            agregarUsuario()
        }
    }

    private fun agregarUsuario() {
        val usuarioId = myRef.push().key!!
        val edtClave = findViewById<EditText>(R.id.et_register_password)
        val edtNombre = findViewById<EditText>(R.id.et_name)
        val edtCorreo = findViewById<EditText>(R.id.et_register_email)

        // Obtén los valores de los EditText
        val clave = edtClave.text.toString().trim()
        val nombre = edtNombre.text.toString().trim()
        val correo = edtCorreo.text.toString().trim()

        // Validar que ningún campo esté vacío
        if (clave.isEmpty() || nombre.isEmpty() || correo.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_LONG).show()
            return
        }

        // Obtén el token de Firebase Messaging

            // Crea el objeto Usuario con los valores
            val empleado = Usuario(usuarioId, clave, nombre, correo)
            myRef.child(usuarioId).setValue(empleado).addOnSuccessListener {
                Toast.makeText(this, "Usuario agregado exitosamente", Toast.LENGTH_LONG).show()
                // Redirigir al MainActivity
                val mainIntent = Intent(this, LoginActivity::class.java)
                startActivity(mainIntent)
                finish() // Finalizar la actividad actual
            }.addOnFailureListener { exception ->
                Toast.makeText(this, "Error al agregar usuario", Toast.LENGTH_LONG).show()
            }

    }
}