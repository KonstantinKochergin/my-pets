package com.example.bottomnavigationexample.ui.medicine.add

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.bottomnavigationexample.data.layer.database.AppDatabase
import com.example.bottomnavigationexample.data.layer.database.MedicineEntity
import com.example.bottomnavigationexample.data.layer.database.ProcedureEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MedicineAddViewModel : ViewModel() {

    suspend fun getMedicineById(context: Context, medicineId: Int) = withContext(Dispatchers.Default) {
        return@withContext AppDatabase.getDatabase(context).medicineDao().getMedicineById(medicineId)
    }

    suspend fun addMedicine(context: Context, medicineEntity: MedicineEntity) = withContext(
        Dispatchers.Default) {
        return@withContext AppDatabase.getDatabase(context).medicineDao().addMedicine(medicineEntity)
    }

    suspend fun updateMedicine(context: Context, medicineEntity: MedicineEntity) = withContext(
        Dispatchers.Default) {
        return@withContext AppDatabase.getDatabase(context).medicineDao().updateMedicine(medicineEntity)
    }
}
