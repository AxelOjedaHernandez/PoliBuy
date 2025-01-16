package com.upiiz.polibuy.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.upiiz.polibuy.R

class PaymentDialog(private val onPaymentConfirmed: (Boolean) -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater: LayoutInflater = requireActivity().layoutInflater
        val dialogView: View = inflater.inflate(R.layout.dialog_payment, null)

        val etNumeroTarjeta: EditText = dialogView.findViewById(R.id.etNumeroTarjetaDialog)
        val etCVV: EditText = dialogView.findViewById(R.id.etCVVDialog)
        val etFechaVencimiento: EditText = dialogView.findViewById(R.id.etFechaVencimientoDialog)
        val btnConfirmar: Button = dialogView.findViewById(R.id.btnPagarDialog)

        builder.setView(dialogView)

        btnConfirmar.setOnClickListener {
            val numeroTarjeta = etNumeroTarjeta.text.toString()
            val cvv = etCVV.text.toString()
            val fechaVencimiento = etFechaVencimiento.text.toString()

            if (validateCardFields(numeroTarjeta, cvv, fechaVencimiento)) {
                onPaymentConfirmed(true)
                dismiss()
            } else {
                Toast.makeText(context, "Por favor, revise los datos de la tarjeta", Toast.LENGTH_SHORT).show()
            }
        }

        return builder.create()
    }

    private fun validateCardFields(numero: String, cvv: String, fecha: String): Boolean {
        return when {
            numero.isEmpty() || numero.length != 16 -> false
            cvv.isEmpty() || cvv.length != 3 -> false
            !fecha.matches(Regex("^(0[1-9]|1[0-2])/([0-9]{2})\$")) -> false
            else -> true
        }
    }
}