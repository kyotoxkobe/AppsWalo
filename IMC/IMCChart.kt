package com.example.imcapp.components

import android.graphics.Color
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.imcapp.data.IMCRecord
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun IMCChart(records: List<IMCRecord>, modifier: Modifier = Modifier) {
    if (records.isEmpty()) return
    
    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp),
        factory = { context ->
            LineChart(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                description.isEnabled = false
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(true)
                setPinchZoom(true)
                setDrawGridBackground(false)
                
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.granularity = 1f
                xAxis.setDrawGridLines(false)
                
                axisLeft.setDrawGridLines(false)
                axisRight.isEnabled = false
                
                legend.isEnabled = true
            }
        },
        update = { chart ->
            val entries = records.mapIndexed { index, record ->
                Entry(index.toFloat(), record.imc)
            }
            
            val dataSet = LineDataSet(entries, "IMC").apply {
                color = Color.BLUE
                valueTextColor = Color.BLACK
                lineWidth = 2f
                setDrawCircles(true)
                setDrawValues(true)
                circleRadius = 4f
                setCircleColor(Color.BLUE)
            }
            
            val lineData = LineData(dataSet)
            chart.data = lineData
            
            // Formatear las etiquetas del eje X para mostrar fechas
            val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
            val labels = records.map { dateFormat.format(it.date) }
            chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            
            chart.invalidate() // Refrescar el gráfico
        }
    )
}