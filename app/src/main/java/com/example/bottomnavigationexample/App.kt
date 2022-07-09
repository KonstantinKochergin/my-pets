package com.example.bottomnavigationexample

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bottomnavigationexample.data.layer.database.AppDatabase

class App : Application() {
    /*
    companion object {

        private var db: AppDatabase? = null

        fun getDbInstance() : AppDatabase {
            return db!!
        }
    }
*/
    override fun onCreate() {
        super.onCreate()
        //db = Room.databaseBuilder(this, AppDatabase::class.java, "my-pets.db").build()
    }


}