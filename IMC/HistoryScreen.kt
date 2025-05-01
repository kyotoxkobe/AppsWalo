package com.example.imcapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.imcapp.components.IMCChart
import com.example.imcapp.data.IMCRecord
import com.example.imcapp.viewmodel.IMCViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(viewModel: IMCViewModel, onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de IMC") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Mostrar el gráfico solo si hay registros
            if (viewModel.imcRecords.isNotEmpty()) {
                Text(
                    text = "Progreso de IMC",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                IMCChart(
                    records = viewModel.imcRecords,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                
                Text(
                    text = "Registros",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay registros de IMC",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            LazyColumn {
                items(viewModel.imcRecords) { record ->
                    RecordItem(
                        record = record,
                        onDeleteClick = { viewModel.deleteRecord(record) }
                    )
                }
            }
        }
    }
}

@Composable
fun RecordItem(record: IMCRecord, onDeleteClick: () -> Unit) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = dateFormat.format(record.date),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "IMC: ${String.format("%.2f", record.imc)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = record.category,
                    fontSize = 14.sp
                )
                
                Text(
                    text = "Peso: ${record.weight} kg, Altura: ${record.height} m",
                    fontSize = 14.sp
                )
            }
            
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar registro",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}