package com.b21dccn216.smarthome.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

data class ChartData(
    val x: String,
    val y: Int
)


@Composable
fun LineChart(
    yAxisData: List<Int>,
    height: Dp = 340.dp,
    step: Int = 2,
    colorChart: Color = Color.Blue,
    tempMin: Int,
    absMaxYPoint: Int,
){
    val xAxisPadding = 16.dp
    val yAxisPadding = 16.dp

//    val yAxisData = network.map{it.y}
    
    val offsetList = remember { mutableListOf<Offset>() }

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(height)
        .padding(16.dp)
    ){
        val gridHeight = height.toPx() - yAxisPadding.toPx()*3
        val gridWidth = size.width

        val minYAxis = tempMin - tempMin%step
        val yAxisLabelList = mutableListOf<String>()

        var tmp = minYAxis
        while(tmp <= absMaxYPoint){
            yAxisLabelList.add(tmp.toString())
            tmp += step
        }

        yAxisLabelList.add(tmp.toString())

        val xAxisSpacing = (gridWidth - xAxisPadding.toPx()) / (yAxisData.size - 1)
        val yAxisSpacing = gridHeight / (yAxisLabelList.size - 1)

        offsetList.clear()
        for(i in 0 until yAxisData.size){
            val x = i * xAxisSpacing + xAxisPadding.toPx()
            val y = gridHeight - (yAxisSpacing * ((yAxisData[i].toFloat() - minYAxis) / step))
            offsetList.add(Offset(x,y))
        }
//        draw vertical grid
        drawLine(
            color = Color.Black,
            start = Offset(xAxisPadding.toPx() + xAxisSpacing*(yAxisData.size-1), 0f),
            end = Offset(xAxisPadding.toPx() + xAxisSpacing*(yAxisData.size-1), gridHeight),
            strokeWidth = 2f
        )
        drawLine(
            color = Color.Black,
            start = Offset(xAxisPadding.toPx(), 0f),
            end = Offset(xAxisPadding.toPx(), gridHeight),
            strokeWidth = 2f
        )
//        for(i in 0 until network.size){
//            val xOffset = (xAxisSpacing * i) + xAxisPadding.toPx()
//            drawLine(
//                color = if(i == 0 || i == network.size-1) Color.Black else Color.Gray.copy(alpha = 0.3f),
//                start = Offset(xOffset, 0f),
//                end = Offset(xOffset, gridHeight),
//                strokeWidth = 2f
//            )
//        }

//        draw horizon grid
        for(i in 0 until yAxisLabelList.size){
            val yOffset = gridHeight - (yAxisSpacing*i)
            drawLine(
                color = if(i == 0 || i == yAxisLabelList.size-1) Color.Black else Color.Gray.copy(alpha = 0.3f),
                start = Offset(xAxisPadding.toPx(), yOffset),
                end = Offset(gridWidth , yOffset),
                strokeWidth = 2f
            )
        }

//        draw x label
//        for(i in 0 until network.size){
//            val xOffset = (xAxisSpacing * i) + xAxisPadding.toPx()
//            drawContext.canvas.nativeCanvas.drawText(
//                xAxisData[i],
//                xOffset,
//                size.height,
//                Paint().apply {
//                    color = Color.Gray.toArgb()
//                    textAlign = Paint.Align.CENTER
//                    textSize = 10.sp.toPx()
//                }
//            )
//        }

//        draw y label
        for(i in 0 until yAxisLabelList.size){
            drawContext.canvas.nativeCanvas.drawText(
                yAxisLabelList[i],
                0f,
                gridHeight - yAxisSpacing * i,
                Paint().apply {
                    color = Color.Gray.toArgb()
                    textAlign = Paint.Align.CENTER
                    textSize = 10.sp.toPx()
                }
            )
        }
        val lastOffset = offsetList.get(offsetList.lastIndex)
        drawCircle(
            color = colorChart,
            radius = 3.dp.toPx(),
            center = lastOffset
        )

//        draw point for each offset
//        offsetList.forEachIndexed{ index, offset ->
//            drawCircle(
//                color = colorChart,
//                radius = 1.5.dp.toPx(),
//                center = offset
//            )
//        }

//        draw line connect point
        drawPoints(
            points = offsetList,
            color = colorChart.copy(alpha = 0.8f),
            pointMode = PointMode.Polygon,
            strokeWidth = 2f
        )

//        gradient color
        val gradientPath = Path().apply {
            moveTo(xAxisPadding.toPx() , size.height)
            for(i in 0 until yAxisData.size){
                lineTo(offsetList[i].x, offsetList[i].y)
            }
            lineTo(gridWidth, gridHeight)
            close()
        }

        drawPath(
            path = gradientPath,
            brush = Brush.verticalGradient(
                colors = listOf( colorChart.copy(alpha = 0.7f), Color.Transparent)
            )
        )
    }
}

@Composable
fun LineChartComponent(
    name: String,
    chartData: List<Int>,
    step: Int = 5,
    colorChart: Color = Color(0xFFFF8343),
    tempMin: Int,
    absMaxYPoint: Int
){
    Box(
        modifier = Modifier
//            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(15))
                .clip(RoundedCornerShape(15))
                .background(color = Color.White)
                .padding(top = 8.dp)
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = name,)
            LineChart(
                yAxisData = chartData,
                height = 190.dp,
                step = step,
                colorChart = colorChart,
                absMaxYPoint = absMaxYPoint,
                tempMin = tempMin)
        }

    }
}

//@Preview(showBackground = true)
//@Composable
//fun PrviewLineChart(){
//    val chartData = listOf(
//        90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,89,89,89,89,89,89,89,89,89,89,89,90,90,90,89,90,90,90,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,90,90,90,90,89,89,89,89,89,90,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,90,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,88,89,89,89,89,89,89,89,89,89,89,89,88,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,88,89,89,89,89,89,89,89,89,89,89,89,89,88,89,89,89,89,89,89,89,89,89,89,89,89,88,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,88,88,88,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,88,89,89,89,89,89,89,89,89,89,89,89,89,88,89,88,89,89,89,89,89,89,89,89,89,88,89,89,89,89,89,89,89,89,89,89,89,89,88,89,88,88,88,88,88,88,88,89,88,88,88,88,89,88,88,88,88,88,88,88,88,89,88,89,88,89,89,89,89,89,89,89,89,89,89,89,89,88,89,88,88,89,89,89,89,89,89,89,89,88,88,88,88,89,89,89,89,89,89,89,89,89,89,88,88,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,88,88,89,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,88,89,88,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,89,90,89,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,91,90,91,91,91,91,91,91,91,91,91,91,91,91,91,91,91,91,91,91,91,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,93,92,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,94,93,93,93,93,93,93,93,93,93,93,93,93,93,94,93,93,93,93,93,93,93,93,93,93,93,93,93,93,93,94,94,94,94,94,94,94,94,94
//    )
//    Box(modifier = Modifier.padding(20.dp)){
//        LineChartComponent(name = "Humidity", chartData = chartData, step = 10, selectedLimit = "332")
//    }
//
//}