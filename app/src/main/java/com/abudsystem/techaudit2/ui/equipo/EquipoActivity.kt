package com.abudsystem.techaudit2.ui.equipo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.abudsystem.techaudit2.databinding.ActivityEquipoBinding

class EquipoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEquipoBinding
    private val viewModel: EquipoViewModel by viewModels()
    private lateinit var adapter: EquipoAdapter

    private var laboratorioId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEquipoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener ID del laboratorio recibido
        laboratorioId = intent.getIntExtra("LAB_ID", -1)

        configurarRecyclerView()
        observarEquipos()

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
}