package com.b21dccn216.smarthome.ui.components

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.b21dccn216.smarthome.model.SensorType

@Composable
fun SensorBox(
    sensorType: SensorType,
    value: String
){
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(15))
            .clip(RoundedCornerShape(percent = 15))
            .background(color = Color.White)
            .padding(vertical = 8.dp)
        ,
    ){
        val (label, row, div) = createRefs()
        LabelIcon(
            icon = sensorType.icon,
            color = sensorType.color,
            value = sensorType.calculateGradient(value.toInt()),
            modifier = Modifier.constrainAs(label){
                top.linkTo(parent.top)
                start.linkTo(parent.start, margin = 4.dp)
                end.linkTo(parent.end, margin = 4.dp)
            }
        )
        HorizontalDivider(modifier = Modifier
            .padding(horizontal = 8.dp)
            .constrainAs(div){
            top.linkTo(label.bottom)
            bottom.linkTo(label.bottom)
        })
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(row) {
                    top.linkTo(div.bottom, margin = 4.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ){
            Text(
                text = value,
                style = TextStyle(
                    fontWeight = FontWeight.ExtraLight,
                    fontSize = 36.sp,
                ),
                modifier = Modifier
            )
            Icon(
                painter = painterResource(id = sensorType.unit),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }

    }

}

@Composable
fun LabelIcon(
    modifier: Modifier,
    color: Color = Color.Transparent,
    isOn: Boolean = true,
    value: Int? = null,
    @DrawableRes icon: Int)
{
    var mod = modifier
        .size(64.dp)
        .padding(8.dp)
    if(value != null){
        Log.d("value", value.toString())
        mod = mod.background(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFD9D9D9), color),
                startY = value.toFloat()),
            shape = CircleShape
        )
    }else{
        mod = mod.background(color = Color(0xFFD9D9D9),
            shape = CircleShape
        )
    }
    Box(
        modifier = mod
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "",
            tint = if(isOn) Color.Black else Color.Black.copy(alpha = 0.3f),
            modifier = Modifier
                .padding(8.dp)
        )
    }
}
@Preview( showBackground = true)
@Composable
fun PreviewSensorInformationBox(){

    Row(
        modifier = Modifier
            .padding(25.dp)
            .fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween){
        SensorBox(
            sensorType = SensorType.Temperature,
            value = "50")

        SensorBox(
            sensorType = SensorType.Light,
            value = "1500")
    }

}

