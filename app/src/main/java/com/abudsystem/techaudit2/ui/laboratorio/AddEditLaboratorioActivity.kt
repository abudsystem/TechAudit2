package com.abudsystem.techaudit2.ui.laboratorio

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.abudsystem.techaudit2.data.local.database.AuditDatabase
import com.abudsystem.techaudit2.data.local.entity.Laboratorio
import com.abudsystem.techaudit2.data.repository.LaboratorioRepository
import com.abudsystem.techaudit2.databinding.ActivityAddEditBinding
import kotlinx.coroutines.launch
import java.util.UUID

class AddEditLaboratorioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBinding
    private lateinit var repository: LaboratorioRepository

    private var laboratorioEditar: Laboratorio? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔹 Inicializar database y repository correctamente
        val database = AuditDatabase.getDatabase(this)
        repository = LaboratorioRepository(database.laboratorioDao(), database.equipoDao())

        obtenerLaboratorioSiExiste()
        configurarBoton()
        configurarInsets()
    }

    private fun obtenerLaboratorioSiExiste() {
        if (intent.hasExtra("EXTRA_LAB_EDITAR")) {
            laboratorioEditar =
                if (Build.VERSION.SDK_INT >= 33) {
                    intent.getParcelableExtra(
                        "EXTRA_LAB_EDITAR",
                        Laboratorio::class.java
                    )
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra("EXTRA_LAB_EDITAR")
                }
        }

        laboratorioEditar?.let { lab ->
            binding.etNombre.setText(lab.nombre)
            binding.etEdificio.setText(lab.edificio)
        }
    }

    private fun configurarBoton() {
        binding.btnGuardar.setOnClickListener {
            guardarOActualizar()
        }
    }

    private fun guardarOActualizar() {

        val nombre = binding.etNombre.text.toString().trim()
        val edificio = binding.etEdificio.text.toString().trim()

        if (nombre.isBlank() || edificio.isBlank()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {

            if (laboratorioEditar == null) {

                val nuevoLaboratorio = Laboratorio(
                    id = UUID.randomUUID().toString(),
                    nombre = nombre,
                    edificio = edificio
                )

                repository.insert(nuevoLaboratorio)

                Toast.makeText(
                    this@AddEditLaboratorioActivity,
                    "Laboratorio creado",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                val laboratorioActualizado = laboratorioEditar!!.copy(
                    nombre = nombre,
                    edificio = edificio
                )

                repository.update(laboratorioActualizado)

                Toast.makeText(
                    this@AddEditLaboratorioActivity,
                    "Laboratorio actualizado",
                    Toast.LENGTH_SHORT
                ).show()
            }

            finish()
        }
    }

    private fun configurarInsets() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
