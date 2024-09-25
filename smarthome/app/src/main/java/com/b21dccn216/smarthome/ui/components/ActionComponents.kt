package com.b21dccn216.smarthome.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.b21dccn216.smarthome.R
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun ActionBox(
    @DrawableRes icon: Int,
    deviceName: String,
    onClick: ()->Unit,
    isOn: Boolean,
){

    ConstraintLayout(
        modifier = Modifier
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(15))
            .background(color = Color.White)
            .clip(RoundedCornerShape(percent = 15))
            .size(width = 125.dp, 100.dp)
            .clickable{
                onClick()
            }
    ){
        val(deviceIcon, deviceText, slideButton) = createRefs()
        LabelIcon(icon = icon,
            isOn = isOn,
            modifier = Modifier
                .constrainAs(deviceIcon){
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start, margin = 4.dp)
                }
        )
        Column(verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.End,
            modifier = Modifier
            .constrainAs(slideButton){
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end, margin = 8.dp)
            }){
            Text(text = deviceName,
                modifier = Modifier)

            SlideButton(isOn = isOn, modifier = Modifier)
        }


    }
}

@Composable
fun SlideButton(
    modifier: Modifier = Modifier,
    width: Int = 50,
    height: Int = 25,
    isOn: Boolean,
) {
//    Box(
    ConstraintLayout(
        modifier = modifier
            .width(width.dp)
            .height(height.dp)
            .background(
                color = if (isOn) Color(0xFF76EE59) else Color.LightGray,
                shape = RoundedCornerShape(50)
            )
    ) {
        val button = createRef()
        Box(
            modifier = Modifier
                .width((width / 2).dp)
                .height(height.dp)
                .padding(4.dp)
                .background(color = Color.White, shape = RoundedCornerShape(30.dp))
                .constrainAs(button) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    if (!isOn) {
                        start.linkTo(parent.start)
                    } else {
                        end.linkTo(parent.end)
                    }
                }
        )
    }
}

@Preview( showBackground = true)
@Composable
fun PreviewActionBox(){

    Row(modifier = Modifier
        .padding(20.dp)
        .fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween){
        var isOn by remember { mutableStateOf(false) }
//        ActionBox()
        SlideButton(isOn = isOn)
        ActionBox(deviceName = "Light Bulb", icon = R.drawable.lightbulb, onClick = {}, isOn = false)

    }

}

