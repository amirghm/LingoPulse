package ai.lingopulse.ui.core.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ai.lingopulse.util.extension.isWeb
import org.jetbrains.compose.resources.FontResource
import org.jetbrains.compose.resources.Font as ResourceFont
import lingopulse.composeapp.generated.resources.Res
import lingopulse.composeapp.generated.resources.Nunito_Light
import lingopulse.composeapp.generated.resources.Nunito_Medium
import lingopulse.composeapp.generated.resources.Noto_Regular
import lingopulse.composeapp.generated.resources.Nunito_Bold

@Composable
private fun nunitoFontFamily(): FontFamily {
    return FontFamily(
        ResourceFont(
            resource = Res.font.Nunito_Light,
            weight = FontWeight.Light
        ),
        ResourceFont(
            resource = Res.font.Nunito_Medium,
            weight = FontWeight.Medium
        ),
        ResourceFont(
            resource = Res.font.Nunito_Bold,
            weight = FontWeight.Bold
        )
    )
}

@Composable
private fun notoFontFamily(): FontFamily {
    return FontFamily(
        ResourceFont(
            resource = Res.font.Noto_Regular,
            weight = FontWeight.Normal
        )
    )
}

@Composable
fun appTypography(): Typography {
    val fontFamily = if (isWeb()) notoFontFamily() else nunitoFontFamily()
    
    return Typography(
        h1 = TextStyle(
            fontFamily = fontFamily,
            fontWeight = if (isWeb()) FontWeight.Normal else FontWeight.Light,
            fontSize = 36.sp,
            letterSpacing = (-1.5).sp
        ),
        h2 = TextStyle(
            fontFamily = fontFamily,
            fontWeight = if (isWeb()) FontWeight.Normal else FontWeight.Light,
            fontSize = 30.sp,
            letterSpacing = (-0.5).sp
        ),
        h3 = TextStyle(
            fontFamily = fontFamily,
            fontWeight = if (isWeb()) FontWeight.Normal else FontWeight.Medium,
            fontSize = 27.sp,
            letterSpacing = 0.sp
        ),
        h4 = TextStyle(
            fontFamily = fontFamily,
            fontWeight = if (isWeb()) FontWeight.Normal else FontWeight.Bold,
            fontSize = 25.sp,
            letterSpacing = 0.25.sp
        ),
        h5 = TextStyle(
            fontFamily = fontFamily,
            fontWeight = if (isWeb()) FontWeight.Normal else FontWeight.Bold,
            fontSize = 23.sp,
            letterSpacing = 0.sp
        ),
        h6 = TextStyle(
            fontFamily = fontFamily,
            fontWeight = if (isWeb()) FontWeight.Normal else FontWeight.Bold,
            fontSize = 20.sp,
            letterSpacing = 0.15.sp
        ),
        subtitle1 = TextStyle(
            fontFamily = fontFamily,
            fontWeight = if (isWeb()) FontWeight.Normal else FontWeight.Medium,
            fontSize = 16.sp,
            letterSpacing = 0.15.sp
        ),
        subtitle2 = TextStyle(
            fontFamily = fontFamily,
            fontWeight = if (isWeb()) FontWeight.Normal else FontWeight.Medium,
            fontSize = 14.sp,
            letterSpacing = 0.1.sp
        ),
        body1 = TextStyle(
            fontFamily = fontFamily,
            fontWeight = if (isWeb()) FontWeight.Normal else FontWeight.Light,
            fontSize = 16.sp,
            letterSpacing = 0.5.sp
        ),
        body2 = TextStyle(
            fontFamily = fontFamily,
            fontWeight = if (isWeb()) FontWeight.Normal else FontWeight.Light,
            fontSize = 14.sp,
            letterSpacing = 0.25.sp
        ),
        button = TextStyle(
            fontFamily = fontFamily,
            fontWeight = if (isWeb()) FontWeight.Normal else FontWeight.Medium,
            fontSize = 14.sp,
            letterSpacing = 1.25.sp
        ),
        caption = TextStyle(
            fontFamily = fontFamily,
            fontWeight = if (isWeb()) FontWeight.Normal else FontWeight.Light,
            fontSize = 12.sp,
            letterSpacing = 0.4.sp
        ),
        overline = TextStyle(
            fontFamily = fontFamily,
            fontWeight = if (isWeb()) FontWeight.Normal else FontWeight.Light,
            fontSize = 10.sp,
            letterSpacing = 1.5.sp
        )
    )
}
