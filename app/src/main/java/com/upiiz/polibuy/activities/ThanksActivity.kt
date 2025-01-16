package com.upiiz.polibuy.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.upiiz.polibuy.R

class ThanksActivity: AppCompatActivity() {
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_thanks)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        idUsuario = intent.getStringExtra("idUsuario")
        val btn_back_to_store = findViewById<Button>(R.id.btn_back_to_store)

        btn_back_to_store.setOnClickListener {
            val InicioIntent = Intent(this, MainActivity::class.java)
            InicioIntent.putExtra("idUsuario", idUsuario)
            startActivity(InicioIntent)
        }
    }

}