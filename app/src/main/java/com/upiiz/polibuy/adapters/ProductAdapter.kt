package com.upiiz.polibuy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.data.Producto
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.upiiz.polibuy.R
import com.bumptech.glide.Glide

class ProductAdapter (
    private val productList: MutableList<Producto>,
private val addCar: (Producto) -> Unit,    // Callback para click corto
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val produ = productList[position]

        holder.itemProductName.text = produ.nombre
        holder.itemProductPrice.text = produ.precio?.toString() ?: "0"
        holder.itemProductquantity.text = produ.cantidad?.toString() ?: "0"

        // Cargar la imagen usando Glide
        Glide.with(holder.itemView.context)
            .load(produ.urlImagen)  // URL de la imagen
            .placeholder(R.drawable.carrito) // Imagen temporal mientras carga
            .error(R.drawable.carrito) // Imagen en caso de error
            .into(holder.itemProductImage)

        // Configurar eventos de click corto y largo
        holder.itemView.setOnClickListener {
            addCar(produ) // Llamar al callback de click corto
        }
    }

    override fun getItemCount(): Int = productList.size

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemProductName: TextView = itemView.findViewById(R.id.itemProductName)
        val itemProductPrice: TextView = itemView.findViewById(R.id.itemProductPrice)
        val itemProductquantity: TextView = itemView.findViewById(R.id.itemProductquantity)
        val itemProductImage: ImageView = itemView.findViewById(R.id.itemProductImage)
    }
}