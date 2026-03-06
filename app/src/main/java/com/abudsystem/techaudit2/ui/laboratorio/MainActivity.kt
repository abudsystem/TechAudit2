package com.abudsystem.techaudit2.ui.laboratorio

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abudsystem.techaudit2.databinding.ActivityMainBinding
import com.abudsystem.techaudit2.ui.equipo.EquipoActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: LaboratorioViewModel by viewModels()
    private lateinit var adapter: LaboratorioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        configurarDeslizarParaBorrar()
        observarViewModel()

        binding.fabAgregar.setOnClickListener {
            val intent = Intent(this, AddEditLaboratorioActivity::class.java)
            startActivity(intent)
        }

        binding.btnSincronizar.setOnClickListener {
            viewModel.sincronizar()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun observarViewModel() {
        viewModel.laboratorios.observe(this) { listaActualizada ->
            adapter.actualizarLista(listaActualizada)
        }

        viewModel.loading.observe(this) { cargando ->
            // Mostramos el contenedor de la barra de progreso
            binding.progressContainer.visibility = if (cargando) View.VISIBLE else View.GONE
            binding.btnSincronizar.isEnabled = !cargando
        }

        viewModel.progreso.observe(this) { p ->
            // Actualizamos la barra de 0 a 100
            binding.progressSync.progress = p
            binding.tvProgressPercent.text = "Sincronizando: $p%"
        }

        viewModel.mensaje.observe(this) { mensaje ->
            mensaje?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                viewModel.limpiarMensaje()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = LaboratorioAdapter(mutableListOf()) { laboratorioSeleccionado ->
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
                val posicion = viewHolder.bindingAdapterPosition
                if (posicion != RecyclerView.NO_POSITION) {
                    val laboratorioABorrar = adapter.listaLaboratorios[posicion]
                    viewModel.delete(laboratorioABorrar)
                    Toast.makeText(this@MainActivity, "Laboratorio eliminado", Toast.LENGTH_SHORT).show()
                }
            }
        }

        ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.rvAuditoria)
    }
}
