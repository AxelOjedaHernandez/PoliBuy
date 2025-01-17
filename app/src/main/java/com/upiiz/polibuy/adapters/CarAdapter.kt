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
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.upiiz.polibuy.activities.CarritoActivity


class CarAdapter(
    private val productList: MutableList<Producto>,
    private val onTotalUpdated: (Int) -> Unit
) : RecyclerView.Adapter<CarAdapter.ProductCarViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_producto_carrito, parent, false)
        return ProductCarViewHolder(view)
    }

    private val database = Firebase.database
    private val carritosRef = database.getReference("Carritos")

    override fun onBindViewHolder(holder: ProductCarViewHolder, position: Int) {
        var producto = productList[position]

        holder.itemProductName.text = producto.nombre
        holder.itemProductPrice.text = "$${producto.precio ?: 0}"
        holder.itemProductDescription.text = "Descripción, color, tamaño" // Cambiar si tienes detalles específicos

        // Cargar la imagen usando Glide
        Glide.with(holder.itemView.context)
            .load(producto.urlImagen)  // URL de la imagen
            .placeholder(R.drawable.carrito) // Imagen temporal mientras carga
            .error(R.drawable.carrito) // Imagen en caso de error
            .into(holder.itemProductImage)

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
                // Recalcula el total después de cambiar la cantidad
                recalcularTotal()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hacer nada
            }
        }

        holder.btn_eliminar_producto.setOnClickListener {
            eliminarProductoCarrito(producto.id)
        }
    }

    private fun eliminarProductoCarrito(idProducto: String?) {
        // Eliminar todos los carritos del usuario
        carritosRef.orderByChild("productoId").equalTo(idProducto).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (carritoSnapshot in snapshot.children) {
                        carritoSnapshot.ref.removeValue()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //Toast.makeText(this@CarritoActivity, "Error al eliminar producto del carrito: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun getItemCount(): Int = productList.size

    fun recalcularTotal() {
        var total = 0
        for (producto in productList) {
            total += (producto.precio ?: 0) * (producto.cantidad ?: 1)
        }

        // Comunica el total a la actividad
        onTotalUpdated(total)
        //(holder.itemView.context as? CarritoActivity)?.updateTotalText(total)

    }


    class ProductCarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemProductName: TextView = itemView.findViewById(R.id.nombre_producto)
        val itemProductPrice: TextView = itemView.findViewById(R.id.precio_producto)
        val itemProductDescription: TextView = itemView.findViewById(R.id.descripcion_producto)
        val itemQuantitySpinner: Spinner = itemView.findViewById(R.id.spinner_cantidad)
        val itemProductImage: ImageView = itemView.findViewById(R.id.imagen_producto)
        val btn_eliminar_producto: ImageButton = itemView.findViewById(R.id.btn_eliminar_producto)

    }

}