package com.abudsystem.techaudit2.ui.equipo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abudsystem.techaudit2.databinding.ActivityEquipoBinding

class EquipoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEquipoBinding
    private val viewModel: EquipoViewModel by viewModels()
    private lateinit var adapter: EquipoAdapter

    private var laboratorioId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEquipoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener ID del laboratorio recibido (Ahora es String)
        laboratorioId = intent.getStringExtra("LAB_ID") ?: ""

        configurarRecyclerView()
        observarEquipos()
        configurarSwipeParaBorrar()

        binding.fabAgregarEquipo.setOnClickListener {
            val intent = Intent(this, AddEditEquipoActivity::class.java)
            intent.putExtra("LAB_ID", laboratorioId)
            startActivity(intent)
        }

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }

    private fun configurarRecyclerView() {
        adapter = EquipoAdapter(mutableListOf()) { equipoSeleccionado ->
            val intent = Intent(this, AddEditEquipoActivity::class.java)
            intent.putExtra("EQUIPO_ID", equipoSeleccionado.id)
            intent.putExtra("LAB_ID", laboratorioId)
            startActivity(intent)
        }

        binding.rvEquipos.layoutManager = LinearLayoutManager(this)
        binding.rvEquipos.adapter = adapter
    }

    private fun observarEquipos() {
        viewModel
            .getEquiposByLaboratorio(laboratorioId)
            .observe(this) { lista ->
                adapter.actualizarLista(lista)
            }
    }

    private fun configurarSwipeParaBorrar() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val posicion = viewHolder.adapterPosition
                val equipo = adapter.getEquipoAt(posicion)
                
                viewModel.delete(equipo)
                Toast.makeText(this@EquipoActivity, "Equipo eliminado", Toast.LENGTH_SHORT).show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvEquipos)
    }
}
