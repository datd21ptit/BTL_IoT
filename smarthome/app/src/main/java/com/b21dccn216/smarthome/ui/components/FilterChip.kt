package com.b21dccn216.smarthome.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FilterChip(
    text: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFE5EAF2),
    contentColor: Color = Color(0xFF344767),
    closeIcon: ImageVector = Icons.Default.Close
) {
    Surface(
        modifier = modifier
            .padding(4.dp)
            .height(32.dp),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = contentColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = closeIcon,
                contentDescription = "Close",
                modifier = Modifier
                    .size(18.dp)
                    .clickable { onClose() },
                tint = contentColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FilterChipPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        FilterChip(text = "Academic Year", onClose = {})
        FilterChip(text = "Semester: 2020 Fall", onClose = {})
        FilterChip(text = "Division: Stateside", onClose = {})
    }
}
