package com.example.bottomnavigationexample.ui.edit.procedure

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.bottomnavigationexample.data.layer.database.AppDatabase
import com.example.bottomnavigationexample.data.layer.database.ProcedureEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EditProcedureViewModel : ViewModel() {

    suspend fun getProcedureById(context: Context, procedureId: Int) = withContext(Dispatchers.Default) {
        return@withContext AppDatabase.getDatabase(context).proceduresDao().getProcedureById(procedureId)
    }

    suspend fun addProcedure(context: Context, procedureEntity: ProcedureEntity) = withContext(Dispatchers.Default) {
        return@withContext AppDatabase.getDatabase(context).proceduresDao().addProcedure(procedureEntity)
    }

    suspend fun updateProcedure(context: Context, procedureEntity: ProcedureEntity) = withContext(Dispatchers.Default) {
        return@withContext AppDatabase.getDatabase(context).proceduresDao().updateProcedure(procedureEntity)
    }

}