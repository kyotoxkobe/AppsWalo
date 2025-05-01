package com.example.sobremi.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sobremi.data.IMCRecord
import kotlin.math.pow

class IMCViewModel : ViewModel() {
    val weight = mutableStateOf("")
    val height = mutableStateOf("")
    val imc = mutableStateOf(0f)
    val result = mutableStateOf("")
    val isCalculated = mutableStateOf(false)
    val errorMessage = mutableStateOf("")
    
    // Lista de registros de IMC
    private val _imcRecords = mutableStateListOf<IMCRecord>()
    val imcRecords: List<IMCRecord> get() = _imcRecords.toList()
    
    fun calculateIMC() {
        if (validateFields()) {
            val weightValue = weight.value.toFloat()
            val heightValue = height.value.toFloat()
            val imcValue = calculateIMCValue(weightValue, heightValue)
            
            imc.value = imcValue
            result.value = interpretIMC(imcValue)
            isCalculated.value = true
            errorMessage.value = ""
            
            // Guardar el registro
            saveIMCRecord(weightValue, heightValue, imcValue, result.value)
        } else {
            errorMessage.value = "Por favor, ingresa valores válidos"
        }
    }
    
    fun reset() {
        weight.value = ""
        height.value = ""
        imc.value = 0f
        result.value = ""
        isCalculated.value = false
        errorMessage.value = ""
    }
    
    private fun validateFields(): Boolean {
        return try {
            val weightValue = weight.value.toFloat()
            val heightValue = height.value.toFloat()
            weightValue > 0 && heightValue > 0
        } catch (e: Exception) {
            false
        }
    }
    
    private fun calculateIMCValue(weight: Float, height: Float): Float {
        return weight / (height.pow(2))
    }
    
    private fun interpretIMC(imc: Float): String {
        return when {
            imc < 18.5 -> "Bajo peso"
            imc < 25 -> "Peso normal"
            imc < 30 -> "Sobrepeso"
            imc < 35 -> "Obesidad grado I"
            imc < 40 -> "Obesidad grado II"
            else -> "Obesidad grado III"
        }
    }
    
    private fun saveIMCRecord(weight: Float, height: Float, imc: Float, category: String) {
        val record = IMCRecord(
            weight = weight,
            height = height,
            imc = imc,
            category = category
        )
        _imcRecords.add(0, record) // Añadir al principio para mostrar el más reciente primero
    }
    
    // Función para eliminar un registro
    fun deleteRecord(record: IMCRecord) {
        _imcRecords.remove(record)
    }
}