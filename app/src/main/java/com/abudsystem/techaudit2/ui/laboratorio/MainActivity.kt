package com.abudsystem.techaudit2.ui.laboratorio

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.abudsystem.techaudit2.databinding.ActivityMainBinding
//import kotlin.io.root

// importaciones

import android.content.Intent
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abudsystem.techaudit2.ui.equipo.EquipoActivity
import com.abudsystem.techaudit2.ui.laboratorio.AddEditLaboratorioActivity
import com.abudsystem.techaudit2.ui.laboratorio.LaboratorioViewModel
//import androidx.lifecycle.ViewModelProvider
import androidx.activity.viewModels

// fin imporataciones





class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //private lateinit var viewModel: LaboratorioViewModel
    private val viewModel: LaboratorioViewModel by viewModels ()
    private lateinit var adapter: LaboratorioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //viewModel = ViewModelProvider(this)[LaboratorioViewModel::class.java]


        viewModel.laboratorios.observe(this) { listaActualizada ->
            adapter.actualizarLista(listaActualizada)
        }

        setupRecyclerView()   // ← AGREGAR ESTO
        configurarDeslizarParaBorrar()

        viewModel.laboratorios.observe(this) { listaActualizada ->
            adapter.actualizarLista(listaActualizada)
        }

        binding.fabAgregar.setOnClickListener {
            val intent = Intent(this, AddEditLaboratorioActivity::class.java)
            startActivity(intent)
        }

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun setupRecyclerView() {

        adapter = LaboratorioAdapter(mutableListOf()) { laboratorioSeleccionado ->

            // Navegar a pantalla de Equipos
            val intent = Intent(this, EquipoActivity::class.java)
            intent.putExtra("LAB_ID", laboratorioSeleccionado.id)
            intent.putExtra("LAB_NOMBRE", laboratorioSeleccionado.nombre)
            startActivity(intent)
        }

        binding.rvAuditoria.layoutManager = LinearLayoutManager(this)
        binding.rvAuditoria.adapter = adapter
    }

    private fun configurarDeslizarParaBorrar() {

        val swipeHandler = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val posicion = viewHolder.adapterPosition
                val laboratorioABorrar = adapter.listaLaboratorios[posicion]

                viewModel.delete(laboratorioABorrar)

                Toast.makeText(
                    this@MainActivity,
                    "Laboratorio eliminado",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        ItemTouchHelper(swipeHandler)
            .attachToRecyclerView(binding.rvAuditoria)
    }
}