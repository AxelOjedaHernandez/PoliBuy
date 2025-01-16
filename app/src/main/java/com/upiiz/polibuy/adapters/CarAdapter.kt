package com.upiiz.polibuy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.data.Producto
import com.upiiz.polibuy.R
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import android.widget.ArrayAdapter


class CarAdapter(
    private val productList: MutableList<Producto>
) : RecyclerView.Adapter<CarAdapter.ProductCarViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_producto_carrito, parent, false)
        return ProductCarViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductCarViewHolder, position: Int) {
        var producto = productList[position]

        holder.itemProductName.text = producto.nombre
        holder.itemProductPrice.text = "$${producto.precio ?: 0}"
        holder.itemProductDescription.text = "Descripción, color, tamaño" // Cambiar si tienes detalles específicos

        // Configurar opciones para el spinner
        val quantities = (1..10).toList() // Rango de cantidades disponibles
        val adapter = ArrayAdapter(holder.itemView.context, android.R.layout.simple_spinner_item, quantities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.itemQuantitySpinner.adapter = adapter

        // Seleccionar la cantidad actual
        val currentQuantity = producto.cantidad ?: 1
        val positionInAdapter = quantities.indexOf(currentQuantity)
        if (positionInAdapter != -1) {
            holder.itemQuantitySpinner.setSelection(positionInAdapter)
        }

        // Listener para manejar cambios de cantidad
        holder.itemQuantitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedQuantity = quantities[position]
                producto.cantidad = selectedQuantity
                // Aquí puedes actualizar el producto en la base de datos si es necesario
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hacer nada
            }
        }
    }


    override fun getItemCount(): Int = productList.size

    class ProductCarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemProductName: TextView = itemView.findViewById(R.id.nombre_producto)
        val itemProductPrice: TextView = itemView.findViewById(R.id.precio_producto)
        val itemProductDescription: TextView = itemView.findViewById(R.id.descripcion_producto)
        val itemProductquantity: TextView = itemView.findViewById(R.id.itemProductquantity)
        val itemQuantitySpinner: Spinner = itemView.findViewById(R.id.spinner_cantidad)

    }
}


