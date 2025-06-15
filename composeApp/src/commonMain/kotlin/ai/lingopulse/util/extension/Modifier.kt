package ai.lingopulse.util.extension

import ai.lingopulse.ui.core.theme.ShimmerEffectColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

private const val ANIMATION_DURATION_MS = 9_000
private val CORNER_RADIUS_DEFAULT_DP = 4.dp

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}

fun Modifier.shimmerEffect(
    radius: Dp = CORNER_RADIUS_DEFAULT_DP,
    show: Boolean = true,
): Modifier =
    composed {
        if (show) {
            val shimmerColor = ShimmerEffectColor
            var size by remember { mutableStateOf(IntSize.Zero) }
            val transition = rememberInfiniteTransition(label = "shimmer")
            val startOffsetX by transition.animateFloat(
                initialValue = -2 * size.width.toFloat(),
                targetValue = size.width.toFloat(),
                animationSpec =
                    infiniteRepeatable(animation = tween(durationMillis = ANIMATION_DURATION_MS, easing = LinearEasing)),
                label = "shimmer move",
            )
            drawWithContent {
                drawRoundRect(
                    brush =
                        Brush.linearGradient(
                            colors =
                                listOf(
                                    shimmerColor,
                                    shimmerColor,
                                    shimmerColor,
                                    shimmerColor,
                                    shimmerColor.copy(alpha = 0.15f),
                                    shimmerColor,
                                    shimmerColor,
                                ),
                            start = Offset(startOffsetX, 0f),
                            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat()),
                            tileMode = TileMode.Repeated,
                        ),
                    cornerRadius =
                        CornerRadius(
                            x = radius.toPx(),
                            y = radius.toPx(),
                        ),
                )
            }.onGloballyPositioned {
                size = it.size
            }
        } else {
            this
        }
    }

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.noRippleCombinedClickable(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    role: Role? = null
): Modifier = composed {
    combinedClickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick,
        onLongClick = onLongClick,
        role = role
    )
}