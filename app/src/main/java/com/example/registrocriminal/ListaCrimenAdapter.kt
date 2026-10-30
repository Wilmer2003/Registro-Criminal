package com.example.registrocriminal

import androidx.recyclerview.widget.RecyclerView
import com.example.registrocriminal.databinding.ItemListaCrimenBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.registrocriminal.databinding.ItemListaCrimenMayorBinding
import android.text.format.DateFormat
import android.graphics.Color
import androidx.core.content.ContextCompat
class CrimenHolder(
    val binding: ItemListaCrimenBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun enlazar(crimen: Crimen) {
        binding.apply {
            tituloCrimen.text = crimen.titulo
            fechaCrimen.text = DateFormat.format("dd/MM/yyyy HH:mm", crimen.fecha).toString()
            root.setOnClickListener {
                Toast.makeText(
                    root.context,
                    "${crimen.titulo}",
                    Toast.LENGTH_LONG).show()
            }
            imgCrimenResuelto.visibility = if (crimen.resuelto) View.VISIBLE else View.GONE
            root.setBackgroundColor(
                if (crimen.resuelto) Color.TRANSPARENT
                else ContextCompat.getColor(root.context, R.color.durazno)
            )
        }
    }

}


class CrimenMayorHolder(
    val binding: ItemListaCrimenMayorBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun enlazar(crimen: Crimen) {
        binding.apply {
            tituloCrimen.text = crimen.titulo
            fechaCrimen.text = DateFormat.format("dd/MM/yyyy HH:mm", crimen.fecha).toString()
            btnContactarPolicia.setOnClickListener {
                Toast.makeText(
                    root.context,
                    "Contactando a la policía...",
                    Toast.LENGTH_SHORT
                ).show()
            }
            root.setBackgroundColor(
                if (crimen.resuelto) Color.TRANSPARENT
                else ContextCompat.getColor(root.context, R.color.durazno)
            )
        }
    }
}

class CrimenAdapter(
    private val crimenes: List<Crimen>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TIPO_NORMAL = 0
        private const val TIPO_MAYOR = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (crimenes[position].crimenMayor) TIPO_MAYOR else TIPO_NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TIPO_MAYOR) {
            CrimenMayorHolder(ItemListaCrimenMayorBinding.inflate(inflater, parent, false))
        } else {
            CrimenHolder(ItemListaCrimenBinding.inflate(inflater, parent, false))
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val crimen = crimenes[position]
        when (holder) {
            is CrimenMayorHolder -> holder.enlazar(crimen)
            is CrimenHolder -> holder.enlazar(crimen)
        }
    }


    override fun getItemCount() = crimenes.size
}