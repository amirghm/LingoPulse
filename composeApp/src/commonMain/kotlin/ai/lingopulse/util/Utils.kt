package ai.lingopulse.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp

@Composable
fun screenWidth() = LocalWindowInfo.current.containerSize.width

@Composable
fun screenHeight() = LocalWindowInfo.current.containerSize.height

@Composable
fun screenWidthDp(): Dp = with(LocalDensity.current) {
    LocalWindowInfo.current.containerSize.width.toDp()
}

@Composable
fun screenHeightDp(): Dp = with(LocalDensity.current) {
    LocalWindowInfo.current.containerSize.height.toDp()
}


@Composable
fun isDesktopMode(): Boolean {
    val width = screenWidth()
    val height = screenHeight()
    val aspectRatio = height.toFloat() / width
    return width >= 1024 && aspectRatio < 1.5f
}

@Composable
fun previewScaleValue() = if(isDesktopMode()) 2.0 else 1.0
