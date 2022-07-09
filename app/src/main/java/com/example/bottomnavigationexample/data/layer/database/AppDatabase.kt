package com.example.bottomnavigationexample.data.layer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(PetEntity::class, MealEntity::class, ProcedureEntity::class, MedicineEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun petsDao() : PetsDao

    abstract fun mealsDao() : MealsDao

    abstract fun proceduresDao() : ProceduresDao

    abstract fun medicineDao() : MedicineDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        private const val DB_NAME = "pets.db"

        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME).build()
                    }
                }
            }

            return INSTANCE!!
        }
    }
}