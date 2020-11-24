package com.code93.linkcoop.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.code93.juliopaprika_menuvirtual.data.Converters
import com.code93.linkcoop.models.Cooperativa

@Database(entities = [Cooperativa::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CooperativaDatabase : RoomDatabase() {

    abstract fun cooperativaDao() : CooperativaDao

    companion object {

        @Volatile
        private var INSTANCE: CooperativaDatabase? = null

        fun getDatabase(context: Context): CooperativaDatabase {
            val temInstance = INSTANCE
            if (temInstance != null) {
                return temInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        CooperativaDatabase::class.java,
                        "cooperativa_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}