package com.example.mypets

import android.app.Application

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