@file:Suppress("DEPRECATION")

package com.b21dccn216.smarthome.ui.screen


import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.b21dccn216.smarthome.R


@Composable
fun ProfileScreen(
    modifier: Modifier,
    innerPadding: PaddingValues
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = innerPadding.calculateBottomPadding())

    ) {
        ImageProfile(modifier = Modifier)
    }
}


@Composable
private fun ImageProfile(
    modifier: Modifier
){
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
    ) {
        val (backImg, profileImg, whiteBack) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.beach_coastline_aerial_view_nature_forest_4k_wallpaper_3840x2160_uhdpaper_com_434_0_b),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(backImg) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.matchParent
                }
        )
        val horizontalGuiline = createTopBarrier(profileImg, margin = 65.dp)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp)
                .shadow(elevation = 5.dp, shape = RoundedCornerShape(15))
                .clip(RoundedCornerShape(15))
                .constrainAs(whiteBack) {
                    top.linkTo(horizontalGuiline)
                },
            shadowElevation = 5.dp
        ){
            Column(
                modifier = Modifier.wrapContentSize(),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(88.dp))
                Text(text = "Nguyen Tran Dat - B21DCCN216",
                    style = MaterialTheme.typography.titleMedium
                )
                val context = LocalContext.current
                Column(Modifier.padding(horizontal = 24.dp)){
                    IconAndLink(
                        context = context,
                        icon = R.drawable.github,
                        link = "https://github.com/datd21ptit/BTL_IoT/tree/main",
                        title = "Link Github")
                    IconAndLink(
                        context = context,
                        icon = R.drawable.google_drive,
                        link = "https://drive.google.com/file/d/12KMPp_kpq-B-ucTZyJeG4DIXJjwE4utO/view?usp=sharing",
                        title = "Link Report")
                    IconAndLink(
                        context = context,
                        icon = R.drawable.facebook,
                        link = "https://www.facebook.com/profile.php?id=100023005893756",
                        title = "Nguyen Tran Dat")
                    IconAndLink(
                        context = context,
                        icon = R.drawable.instagram,
                        link = "https://www.instagram.com/datanddatt/",
                        title = "nguyen_tran_dat")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        CreateImageProfile(
            modifier = Modifier.constrainAs(profileImg){
                bottom.linkTo(backImg.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
    }
}

@Composable
fun IconAndLink(
    context: Context,
    icon: Int,
    link: String,
    title: String
){
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon( painter = painterResource(id = icon), contentDescription = null,
            modifier = Modifier.size(24.dp))
        val annotatedString = buildAnnotatedString {
            pushStringAnnotation(title, annotation = link)
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)){
                append(title)
            }
        }
        ClickableText(text = annotatedString,
            onClick = {offset ->
                annotatedString.getStringAnnotations(title, offset, offset)
                    .firstOrNull()?.let { annotation ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                        context.startActivity(intent)
                    }
            })
    }
}

@Composable
private fun CreateImageProfile(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .size(150.dp)
            .padding(5.dp),
        shape = CircleShape,
        border = BorderStroke(5.dp, Color.White),
        shadowElevation = 5.dp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile_img),
            contentDescription = "Profile Image",
            modifier = modifier.size(135.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ProfileScreen(modifier = Modifier, PaddingValues())
}