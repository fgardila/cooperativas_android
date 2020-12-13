package com.code93.linkcoop.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.code93.juliopaprika_menuvirtual.data.Converters
import com.code93.linkcoop.models.Cooperativa
import com.code93.linkcoop.models.FieldsTrx
import com.code93.linkcoop.models.LogTransacciones

@Database(entities = [Cooperativa::class, FieldsTrx::class, LogTransacciones::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class LinkCoopDatabase : RoomDatabase() {

    abstract fun cooperativaDao() : CooperativaDao
    abstract fun fieldsTrxDao() : FieldsTrxDao
    abstract fun logTransaccionesDao() : LogTransaccionesDao

    companion object {

        @Volatile
        private var INSTANCE: LinkCoopDatabase? = null

        fun getDatabase(context: Context): LinkCoopDatabase {
            val temInstance = INSTANCE
            if (temInstance != null) {
                return temInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        LinkCoopDatabase::class.java,
                        "linkcoop_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}