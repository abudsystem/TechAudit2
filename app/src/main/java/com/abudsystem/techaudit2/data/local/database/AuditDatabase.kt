package com.abudsystem.techaudit2.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import androidx.room.TypeConverters
import com.abudsystem.techaudit2.data.local.entity.Equipo
import com.abudsystem.techaudit2.data.local.entity.Laboratorio

import com.abudsystem.techaudit2.util.Converters


import com.abudsystem.techaudit2.data.local.dao.EquipoDao
import com.abudsystem.techaudit2.data.local.dao.LaboratorioDao




@Database(entities = [Equipo::class, Laboratorio::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)

abstract class AuditDatabase: RoomDatabase() {

    // 🔹 Declaración de DAOs
    abstract fun laboratorioDao(): LaboratorioDao
    abstract fun equipoDao(): EquipoDao

    companion object {

        @Volatile
        private var INSTANCE: AuditDatabase? = null

        fun getDatabase(context: Context): AuditDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AuditDatabase::class.java,
                    "audit_database"
                )
                    .fallbackToDestructiveMigration()
                    // ⚠ Solo para desarrollo
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }

}
