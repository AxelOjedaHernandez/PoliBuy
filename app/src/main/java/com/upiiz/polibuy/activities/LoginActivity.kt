package com.upiiz.polibuy.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.upiiz.polibuy.R

class LoginActivity : AppCompatActivity() {
    private val database = Firebase.database
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