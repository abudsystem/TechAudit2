package com.abudsystem.techaudit2.ui.laboratorio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abudsystem.techaudit2.data.local.entity.Laboratorio
import com.abudsystem.techaudit2.databinding.ItemLaboratorioBinding

class LaboratorioAdapter (
    val listaLaboratorios: MutableList<Laboratorio>,
    private val onItemSelected: (Laboratorio) -> Unit
) : RecyclerView.Adapter<LaboratorioAdapter.LaboratorioViewHolder>() {

    inner class LaboratorioViewHolder(
        val binding: ItemLaboratorioBinding
    ) : RecyclerView.ViewHolder(binding.root)

    // 1️⃣ Crear el molde
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LaboratorioViewHolder {

        val binding = ItemLaboratorioBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return LaboratorioViewHolder(binding)
    }

    // 2️⃣ Cantidad de elementos
    override fun getItemCount(): Int {
        return listaLaboratorios.size
    }

    // 3️⃣ Actualizar lista
    fun actualizarLista(nuevaLista: List<Laboratorio>) {
        listaLaboratorios.clear()
        listaLaboratorios.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    // 4️⃣ Pintar datos
    override fun onBindViewHolder(
        holder: LaboratorioViewHolder,
        position: Int
    ) {

        val laboratorio = listaLaboratorios[position]

        holder.binding.tvNombreLaboratorio.text = laboratorio.nombre
        holder.binding.tvUbicacionLaboratorio.text = laboratorio.edificio

        // Click
        holder.itemView.setOnClickListener {
            onItemSelected(laboratorio)
        }
    }
}