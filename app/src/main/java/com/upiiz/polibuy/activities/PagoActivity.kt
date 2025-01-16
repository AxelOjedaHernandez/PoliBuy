package com.example.proyectofinal.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.upiiz.polibuy.R
import com.upiiz.polibuy.activities.ProductoActivity
import com.upiiz.polibuy.activities.ThanksActivity

class PagoActivity : AppCompatActivity() {

    // Declaramos vistas
    private lateinit var rgMetodoPago: RadioGroup
    private lateinit var rbEfectivo: RadioButton
    private lateinit var rbTarjeta: RadioButton
    private lateinit var llPagoEfectivo: LinearLayout
    private lateinit var llPagoTarjeta: LinearLayout
    private lateinit var etNumeroTarjeta: EditText
    private lateinit var etCVV: EditText
    private lateinit var etFechaVencimiento: EditText
    private lateinit var btnPagarTarjeta: Button
    private lateinit var btnConfirmarEfectivo: Button

    private var idUsuario: String? = null // Variable para almacenar el ID del usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pago)

        // Ajustamos el diseño
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Obtener el idUsuario desde el intent
        idUsuario = intent.getStringExtra("idUsuario")
        val btnConfirmarPago = findViewById<Button>(R.id.btnConfirmar)

        btnConfirmarPago.setOnClickListener {
           val confirmarPagoIntent = Intent(this@PagoActivity, ThanksActivity::class.java)
           confirmarPagoIntent.putExtra("idUsuario", idUsuario)
           startActivity(confirmarPagoIntent)

        }

        // Inicializamos las vistas
        rgMetodoPago = findViewById(R.id.rgMetodoPago)
        rbEfectivo = findViewById(R.id.rbEfectivo)
        rbTarjeta = findViewById(R.id.rbTarjeta)
        llPagoEfectivo = findViewById(R.id.llPagoEfectivo)
        llPagoTarjeta = findViewById(R.id.llPagoTarjeta)
        etNumeroTarjeta = findViewById(R.id.etNumeroTarjeta)
        etCVV = findViewById(R.id.etCVV)
        etFechaVencimiento = findViewById(R.id.etFechaVencimiento)
        btnPagarTarjeta = findViewById(R.id.btnPagarTarjeta)
        btnConfirmarEfectivo = findViewById(R.id.btnConfirmarEfectivo)

        // Listener para el RadioGroup
        rgMetodoPago.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbEfectivo -> {
                    // Mostrar la sección de pago en efectivo y ocultar la de tarjeta
                    llPagoEfectivo.visibility = View.VISIBLE
                    llPagoTarjeta.visibility = View.GONE
                    btnConfirmarEfectivo.visibility = View.VISIBLE
                }
                R.id.rbTarjeta -> {
                    // Mostrar la sección de pago con tarjeta y ocultar la de efectivo
                    llPagoEfectivo.visibility = View.GONE
                    llPagoTarjeta.visibility = View.VISIBLE
                    btnConfirmarEfectivo.visibility = View.GONE
                }
            }
        }

        // Listener para el botón de confirmar pago en efectivo
        btnConfirmarEfectivo.setOnClickListener {
            Toast.makeText(this, "Pago en efectivo confirmado", Toast.LENGTH_SHORT).show()
        }

        // Listener para el botón de pagar con tarjeta
        btnPagarTarjeta.setOnClickListener {
            val numeroTarjeta = etNumeroTarjeta.text.toString()
            val cvv = etCVV.text.toString()
            val fechaVencimiento = etFechaVencimiento.text.toString()

            // Validar los campos
            if (numeroTarjeta.isEmpty() || cvv.isEmpty() || fechaVencimiento.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Pago con tarjeta procesado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

