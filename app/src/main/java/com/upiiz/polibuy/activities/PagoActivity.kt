package com.upiiz.polibuy.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.upiiz.polibuy.R
import com.upiiz.polibuy.dialogs.PaymentDialog

class PagoActivity : AppCompatActivity() {

    private lateinit var rgMetodoPago: RadioGroup
    private lateinit var rbEfectivo: RadioButton
    private lateinit var rbTarjeta: RadioButton
    private lateinit var btnConfirmarPago: Button
    private lateinit var etUserEmail: EditText
    private lateinit var etUserPhone: EditText
    private lateinit var edUserAddress: EditText
    private val database = Firebase.database
    private val carritosRef = database.getReference("Carritos")

    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pago)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicialización de vistas
        initializeViews()

        // Configuración de listeners
        setupListeners()
    }

    private fun initializeViews() {
        idUsuario = intent.getStringExtra("idUsuario")

        rgMetodoPago = findViewById(R.id.rgMetodoPago)
        rbEfectivo = findViewById(R.id.rbEfectivo)
        rbTarjeta = findViewById(R.id.rbTarjeta)
        btnConfirmarPago = findViewById(R.id.btnConfirmar)

        etUserEmail = findViewById<EditText>(R.id.etUserEmail)
        etUserPhone = findViewById<EditText>(R.id.etUserPhone)
        edUserAddress = findViewById<EditText>(R.id.edUserAddress)

    }

    private fun setupListeners() {
        btnConfirmarPago.setOnClickListener {
            if (etUserEmail.text.toString().isEmpty() || etUserPhone.text.toString().isEmpty() || edUserAddress.text.toString().isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            } else if (validatePaymentSelection()) {
                if (rbTarjeta.isChecked) {
                    showPaymentDialog()
                } else {
                    proceedToThanksActivity("Efectivo")
                }
            }
        }
    }

    private fun showPaymentDialog() {
        val paymentDialog = PaymentDialog { tarjetaValida ->
            if (tarjetaValida) {
                Toast.makeText(this, "Pago con tarjeta procesado", Toast.LENGTH_SHORT).show()
                proceedToThanksActivity("Tarjeta")
            } else {
                Toast.makeText(this, "Error en los datos de la tarjeta", Toast.LENGTH_SHORT).show()
            }
        }
        paymentDialog.show(supportFragmentManager, "PaymentDialog")
    }

    private fun validatePaymentSelection(): Boolean {
        return if (rgMetodoPago.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Por favor seleccione un método de pago", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    private fun proceedToThanksActivity(metodoPago: String) {
        // Eliminar todos los carritos del usuario
        carritosRef.orderByChild("usuarioId").equalTo(idUsuario).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (carritoSnapshot in snapshot.children) {
                        carritoSnapshot.ref.removeValue()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PagoActivity, "Error al eliminar carritos: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })

        val confirmarPagoIntent = Intent(this, ThanksActivity::class.java)
        confirmarPagoIntent.putExtra("idUsuario", idUsuario)
        confirmarPagoIntent.putExtra("metodoPago", metodoPago)
        startActivity(confirmarPagoIntent)
    }
}
