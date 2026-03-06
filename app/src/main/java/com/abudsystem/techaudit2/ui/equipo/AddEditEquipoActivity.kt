package com.abudsystem.techaudit2.ui.equipo

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.abudsystem.techaudit2.data.local.entity.Equipo
import com.abudsystem.techaudit2.data.local.entity.EstadoEquipo
import com.abudsystem.techaudit2.databinding.ActivityAddEditEquipoBinding
import java.util.UUID

class AddEditEquipoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditEquipoBinding
    private val viewModel: EquipoViewModel by viewModels()

    private var laboratorioId: String = ""
    private var equipoId: String = ""
    private var modoEditar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddEditEquipoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        laboratorioId = intent.getStringExtra("LAB_ID") ?: ""
        equipoId = intent.getStringExtra("EQUIPO_ID") ?: ""
        modoEditar = equipoId.isNotEmpty()

        configurarSpinner()

        if (modoEditar) {
            cargarDatos()
        }

        binding.btnGuardar.setOnClickListener {
            guardar()
        }
    }

    private fun configurarSpinner() {
        val listaEstados = EstadoEquipo.values().map { it.name }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listaEstados
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spEstado.adapter = adapter
    }

    private fun cargarDatos() {
        viewModel.getById(equipoId) { equipo ->
            equipo?.let {
                binding.etNombre.setText(it.nombre)

                val posicion = EstadoEquipo.values().indexOf(it.estado)

                binding.spEstado.setSelection(posicion)
            }
        }
    }

    private fun guardar() {

        val nombre = binding.etNombre.text.toString().trim()

        if (nombre.isEmpty()) {
            Toast.makeText(
                this,
                "Debe ingresar un nombre",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val estadoSeleccionado = EstadoEquipo.valueOf(
            binding.spEstado.selectedItem.toString()
        )

        val equipo = Equipo(
            id = if (modoEditar) equipoId else UUID.randomUUID().toString(),
            nombre = nombre,
            estado = estadoSeleccionado,
            laboratorioId = laboratorioId
        )

        if (modoEditar) {
            viewModel.update(equipo)
            Toast.makeText(this, "Equipo actualizado", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.insert(equipo)
            Toast.makeText(this, "Equipo creado", Toast.LENGTH_SHORT).show()
        }

        finish()
    }
}
