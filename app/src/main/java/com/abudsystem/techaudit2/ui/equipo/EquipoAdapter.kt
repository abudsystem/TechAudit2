package com.abudsystem.techaudit2.ui.equipo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abudsystem.techaudit2.R
import com.abudsystem.techaudit2.data.local.entity.Equipo

class EquipoAdapter(
    private var listaEquipos: MutableList<Equipo>,
    private val onClick: ((Equipo) -> Unit)? = null
) : RecyclerView.Adapter<EquipoAdapter.EquipoViewHolder>() {

    inner class EquipoViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val tvNombre: TextView =
            itemView.findViewById(R.id.tvNombreEquipo)

        private val tvEstado: TextView =
            itemView.findViewById(R.id.tvEstadoEquipo)

        fun bind(equipo: Equipo) {
            tvNombre.text = equipo.nombre
            tvEstado.text = equipo.estado.name

            itemView.setOnClickListener {
                onClick?.invoke(equipo)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EquipoViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_equipo, parent, false)

        return EquipoViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: EquipoViewHolder,
        position: Int
    ) {
        holder.bind(listaEquipos[position])
    }

    override fun getItemCount(): Int = listaEquipos.size

    fun getEquipoAt(position: Int): Equipo {
        return listaEquipos[position]
    }

    fun actualizarLista(nuevaLista: List<Equipo>) {
        listaEquipos.clear()
        listaEquipos.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}
