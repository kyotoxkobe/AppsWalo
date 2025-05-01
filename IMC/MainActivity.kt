import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.imcapp.ui.theme.IMCAppTheme
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IMCAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    IMCCalculator()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IMCCalculator() {
    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var imc by remember { mutableStateOf(0f) }
    var resultado by remember { mutableStateOf("") }
    var isCalculated by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = "Calculadora de IMC",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        // Campo de peso
        OutlinedTextField(
            value = peso,
            onValueChange = { peso = it },
            label = { Text("Peso (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = true
        )

        // Campo de altura
        OutlinedTextField(
            value = altura,
            onValueChange = { altura = it },
            label = { Text("Altura (m)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = true
        )

        // Mensaje de error
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Botones
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    if (validarCampos(peso, altura)) {
                        val pesoValue = peso.toFloat()
                        val alturaValue = altura.toFloat()
                        imc = calcularIMC(pesoValue, alturaValue)
                        resultado = interpretarIMC(imc)
                        isCalculated = true
                        errorMessage = ""
                    } else {
                        errorMessage = "Por favor, ingresa valores válidos"
                    }
                },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text("Calcular")
            }

            Button(
                onClick = {
                    peso = ""
                    altura = ""
                    imc = 0f
                    resultado = ""
                    isCalculated = false
                    errorMessage = ""
                },
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) {
                Text("Reiniciar")
            }
        }

        // Resultado
        if (isCalculated) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Tu IMC es:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Text(
                        text = String.format("%.2f", imc),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = obtenerColorIMC(imc),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    Text(
                        text = resultado,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = obtenerColorIMC(imc),
                        textAlign = TextAlign.Center
                    )
                    
                    Text(
                        text = recomendacionIMC(imc),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

fun validarCampos(peso: String, altura: String): Boolean {
    return try {
        val pesoValue = peso.toFloat()
        val alturaValue = altura.toFloat()
        pesoValue > 0 && alturaValue > 0
    } catch (e: Exception) {
        false
    }
}

fun calcularIMC(peso: Float, altura: Float): Float {
    return peso / (altura.pow(2))
}

fun interpretarIMC(imc: Float): String {
    return when {
        imc < 18.5 -> "Bajo peso"
        imc < 25 -> "Peso normal"
        imc < 30 -> "Sobrepeso"
        imc < 35 -> "Obesidad grado I"
        imc < 40 -> "Obesidad grado II"
        else -> "Obesidad grado III"
    }
}

fun obtenerColorIMC(imc: Float): Color {
    return when {
        imc < 18.5 -> Color(0xFF2196F3) // Azul para bajo peso
        imc < 25 -> Color(0xFF4CAF50) // Verde para peso normal
        imc < 30 -> Color(0xFFFFC107) // Amarillo para sobrepeso
        else -> Color(0xFFF44336) // Rojo para obesidad
    }
}

fun recomendacionIMC(imc: Float): String {
    return when {
        imc < 18.5 -> "Considera aumentar tu ingesta calórica y consultar con un nutricionista para ganar peso de forma saludable."
        imc < 25 -> "¡Excelente! Mantén tus hábitos saludables de alimentación y ejercicio regular."
        imc < 30 -> "Considera reducir moderadamente tu ingesta calórica e incrementar la actividad física."
        else -> "Es recomendable consultar con un profesional de la salud para establecer un plan de alimentación y ejercicio adecuado."
    }
}

@Preview(showBackground = true)
@Composable
fun IMCCalculatorPreview() {
    IMCAppTheme {
        IMCCalculator()
    }
}