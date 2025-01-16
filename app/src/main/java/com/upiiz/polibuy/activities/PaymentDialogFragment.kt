package com.upiiz.polibuy.activities

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.upiiz.polibuy.R

class PaymentDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Crear el diálogo
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_payment, null)

        // Referencias a los campos del diálogo
        val cardNumberField: EditText = view.findViewById(R.id.etNumeroTarjetaDialog)
        val expiryDateField: EditText = view.findViewById(R.id.etFechaVencimientoDialog)
        val cvvField: EditText = view.findViewById(R.id.etCVVDialog)
        val payButton: Button = view.findViewById(R.id.btnPagarDialog)

        // Listener del botón de pagar
        payButton.setOnClickListener {
            val cardNumber = cardNumberField.text.toString()
            val expiryDate = expiryDateField.text.toString()
            val cvv = cvvField.text.toString()

            if (cardNumber.isNotEmpty() && expiryDate.isNotEmpty() && cvv.isNotEmpty()) {
                Toast.makeText(requireContext(), "Pago procesado exitosamente", Toast.LENGTH_SHORT).show()
                dismiss() // Cerrar el diálogo
            } else {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar el diálogo con el diseño personalizado
        builder.setView(view)
            .setTitle("Pago con Tarjeta")
            .setNegativeButton("Cancelar") { _, _ -> dismiss() }

        return builder.create()
    }
}