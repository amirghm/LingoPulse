@file:OptIn(ExperimentalFoundationApi::class, ExperimentalTime::class)

package ai.lingopulse.ui.feature.feed

import androidx.compose.animation.animateColor
import ai.lingopulse.ui.core.common.model.ScreenEventHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import ai.lingopulse.presentation.feed.FeedScreenUiState
import ai.lingopulse.presentation.common.model.UiCategory
import ai.lingopulse.presentation.feed.model.UiFeed
import ai.lingopulse.ui.core.common.component.LingoPulseMarkdown
import ai.lingopulse.ui.core.common.component.LingoPulseTextField
import ai.lingopulse.ui.core.theme.AccentColor
import ai.lingopulse.ui.core.theme.AiBubbleBrush
import ai.lingopulse.ui.core.theme.BackgroundColor
import ai.lingopulse.ui.core.theme.CornerSmall
import ai.lingopulse.ui.core.theme.ElevationLow
import ai.lingopulse.ui.core.theme.FontSizeMedium
import ai.lingopulse.ui.core.theme.FontSizeSmall
import ai.lingopulse.ui.core.theme.FontSizeXXLarge
import ai.lingopulse.ui.core.theme.PaddingLarge
import ai.lingopulse.ui.core.theme.PaddingMedium
import ai.lingopulse.ui.core.theme.PaddingSmall
import ai.lingopulse.ui.core.theme.PaddingXLarge
import ai.lingopulse.ui.core.theme.PaddingXXLarge
import ai.lingopulse.ui.core.theme.PaddingXXTiny
import ai.lingopulse.ui.core.theme.PlaceholderTextColor
import ai.lingopulse.ui.core.theme.RainbowColor
import ai.lingopulse.ui.core.theme.TextPrimaryColor
import ai.lingopulse.ui.core.theme.TextSecondaryColor
import ai.lingopulse.ui.core.theme.ToolbarColor
import ai.lingopulse.ui.feature.setting.SettingScreenEvent
import ai.lingopulse.util.extension.toHumanReadable
import ai.lingopulse.util.isDesktopMode
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import coil3.compose.AsyncImage
import com.mikepenz.markdown.m2.markdownColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.kodein.emoji.compose.m2.TextWithPlatformEmoji
import lingopulse.composeapp.generated.resources.Res
import lingopulse.composeapp.generated.resources.feed_news_title
import lingopulse.composeapp.generated.resources.generating_your_app
import lingopulse.composeapp.generated.resources.made_by_lingopulse_team
import lingopulse.composeapp.generated.resources.menu_about
import lingopulse.composeapp.generated.resources.menu_adjust_settings
import lingopulse.composeapp.generated.resources.type_your_idea
import kotlin.time.ExperimentalTime

@OptIn(InternalResourceApi::class)
@Composable
fun FeedScreen(
    state: FeedScreenUiState,
    eventHandler: ScreenEventHandler,
) {
    val coroutineScope = rememberCoroutineScope()

    val feedListState = rememberLazyListState()
    val loading = state is FeedScreenUiState.Loading

    val feeds = when (state) {
        is FeedScreenUiState.FeedLoaded -> state.feeds
        is FeedScreenUiState.Loading -> state.feeds
        else -> null
    }

    val categories = when (state) {
        is FeedScreenUiState.Loading -> state.categories
        is FeedScreenUiState.FeedLoaded -> state.categories
        else -> null
    }

    Scaffold(
        topBar = {
            TopAppBarContent(
                scope = coroutineScope,
                eventHandler = eventHandler
            )
        },
        content = { innerPadding ->
            ChatContent(
                modifier = Modifier.padding(innerPadding).requiredWidthIn(min = 320.dp),
                loading = loading,
                feeds = feeds,
                categories = categories,
                eventHandler = eventHandler,
                feedListState = feedListState
            )
        }
    )
}

@Composable
fun ChatContent(
    modifier: Modifier,
    loading: Boolean,
    feeds: List<UiFeed>?,
    categories: List<UiCategory>?,
    eventHandler: ScreenEventHandler,
    feedListState: LazyListState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            categories?.let { categories ->
                val scrollState = rememberLazyListState()
                val coroutineScope = rememberCoroutineScope()
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    LazyRow(
                        state = scrollState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .draggable(
                                orientation = Orientation.Horizontal,
                                state = rememberDraggableState { delta ->
                                    coroutineScope.launch {
                                        scrollState.scrollBy(-delta)
                                    }
                                },
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        item {
                            Spacer(modifier = Modifier.width(PaddingLarge))
                        }

                        categories.forEach { category ->
                            item {
                                OutlinedButton(
                                    onClick = {
                                        eventHandler.onEvent(
                                            FeedScreenEvent.OnCategoryClicked(category)
                                        )
                                    },
                                    shape = RoundedCornerShape(CornerSmall),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = if (category.isSelected) TextSecondaryColor else ToolbarColor,
                                        contentColor = TextSecondaryColor
                                    ),
                                    border = BorderStroke(0.5.dp, TextSecondaryColor),
                                    contentPadding = PaddingValues(PaddingSmall),
                                    modifier = Modifier.padding(horizontal = PaddingXXTiny)
                                ) {
                                    TextWithPlatformEmoji(
                                        modifier = Modifier,
                                        text = category.category,
                                        color = if (category.isSelected) ToolbarColor else TextSecondaryColor,
                                    )
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.width(PaddingLarge))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(PaddingLarge))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .then(if (isDesktopMode()) Modifier.fillMaxWidth(0.7f) else Modifier.fillMaxWidth())
                        .padding(
                            bottom = WindowInsets.captionBar.asPaddingValues()
                                .calculateBottomPadding()
                        ),
                    state = feedListState,
                    verticalArrangement = Arrangement.Top,
                ) {
                    feeds?.let { feeds ->
                        items(feeds.size) {
                            val feed = feeds[it]
                            Feed(eventHandler, uiFeed = feed)
                            if (it == feeds.lastIndex) {
                                Spacer(modifier = Modifier.height(PaddingSmall))
                            }
                        }
                    }

                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(vertical = PaddingMedium),
                            contentAlignment = Alignment.Center
                        ) {
                            TextWithPlatformEmoji(
                                text = stringResource(Res.string.made_by_lingopulse_team),
                                color = TextSecondaryColor,
                                fontSize = FontSizeSmall,
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(PaddingLarge))
                    }
                }
            }


        }
    }
}

@Composable
fun TopAppBarContent(
    scope: CoroutineScope,
    eventHandler: ScreenEventHandler
) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(ToolbarColor)
            .padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
            )
    ) {
        TopAppBar(
            backgroundColor = ToolbarColor,
            contentColor = TextSecondaryColor,
            modifier = Modifier
                .height(80.dp),
            elevation = 0.dp,
            title = {
                Text(
                    modifier = Modifier.padding(
                        horizontal = PaddingXXLarge
                    ),
                    text = stringResource(Res.string.feed_news_title),
                    color = TextPrimaryColor,
                    fontSize = FontSizeXXLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            actions = {
                var expanded by remember { mutableStateOf(false) }
                IconButton(onClick = { expanded = true })
                {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "App Options",
                        tint = TextSecondaryColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    DropdownMenuItem(
                        content = {
                            Text(stringResource(Res.string.menu_adjust_settings))
                        },
                        onClick = {
                            expanded = false
                            eventHandler.onEvent(FeedScreenEvent.OnSettingsClicked)
                        }
                    )
                    DropdownMenuItem(
                        content = {
                            Text(stringResource(Res.string.menu_about))
                        },
                        onClick = {
                            expanded = false
                        }
                    )
                }
            }
        )
    }
}

@Composable
fun PromptInputField(
    eventHandler: ScreenEventHandler,
    animated: Boolean,
    processing: Boolean,
    prompt: String,
    onPromptChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    var size by remember { mutableStateOf(IntSize.Zero) }

    // Shimmer animation: only run if animated == true or processing == true
    val sweepProgress = if (animated || processing) {
        val shimmerTransition = rememberInfiniteTransition()
        shimmerTransition.animateFloat(
            initialValue = 0f,
            targetValue = 0.98f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 5000,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "rotationProgress"
        ).value
    } else {
        .1f
    }

    val shimmerBrush = if (processing) {
        Brush.sweepGradient(
            colorStops = RainbowColor.map {
                it.first * sweepProgress to it.second
            }.toTypedArray(),
            center = Offset(
                (1 - sweepProgress) * size.width,
                (1 - sweepProgress) * size.height
            )
        )
    } else {
        Brush.sweepGradient(
            colorStops = arrayOf(
                0.0f to AccentColor.copy(alpha = 0.1f),
                sweepProgress to AccentColor,
                1.0f to AccentColor.copy(alpha = 0.1f)
            ),
            center = Offset(
                (1 - sweepProgress) * size.width,
                (1 - sweepProgress) * size.height
            )
        )
    }

    // Animated hint color: only run if animated == true or processing == true
    val animatedHintColor = if (animated || processing) {
        val textColorTransition = rememberInfiniteTransition()
        textColorTransition.animateColor(
            initialValue = PlaceholderTextColor,
            targetValue = TextPrimaryColor,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        ).value
    } else {
        PlaceholderTextColor
    }

    val isDesktop = isDesktopMode()

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = modifier
                .then(
                    if (isDesktop) Modifier.fillMaxWidth(0.7f)
                    else Modifier.fillMaxWidth()
                )
                .padding(start = PaddingSmall, end = PaddingSmall, bottom = PaddingSmall)
                .shadow(ElevationLow, RoundedCornerShape(CornerSmall))
                .background(ToolbarColor, RoundedCornerShape(CornerSmall))
                .border(
                    width = 2.dp,
                    brush = shimmerBrush,
                    shape = RoundedCornerShape(CornerSmall)
                )
                .onSizeChanged { size = it },
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            LingoPulseTextField(
                value = prompt,
                onValueChange = { onPromptChanged(it) },
                placeholder = {
                    Text(
                        text = if (processing) {
                            stringResource(Res.string.generating_your_app)
                        } else {
                            stringResource(Res.string.type_your_idea)
                        },
                        color = if (processing) animatedHintColor else PlaceholderTextColor
                    )
                },
                maxLines = 10,
                modifier = Modifier
                    .weight(1f)
                    .padding(PaddingXXTiny)
                    .background(ToolbarColor),
                textStyle = TextStyle(
                    fontSize = FontSizeMedium,
                    color = TextPrimaryColor
                ),
                singleLine = false,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = ToolbarColor,
                    cursorColor = TextPrimaryColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun Feed(eventHandler: ScreenEventHandler, uiFeed: UiFeed) {

    var timeAgo = uiFeed.timestamp.toHumanReadable()

    LaunchedEffect(Unit) {
        while (true) {
            delay(10000) // Wait for 5 seconds
            timeAgo = uiFeed.timestamp.toHumanReadable()
        }
    }

    Card(
        shape = RoundedCornerShape(CornerSmall),
        modifier = Modifier
            .padding(
                start = PaddingXLarge,
                end = PaddingXLarge,
                bottom = PaddingSmall
            )
            .shadow(ElevationLow)
            .background(AiBubbleBrush, RoundedCornerShape(CornerSmall))

    ) {
        Box(
            modifier = Modifier.fillMaxWidth().clickable {
                eventHandler.onEvent(FeedScreenEvent.OnNewsClicked(uiFeed))
            },
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Box() {
                    AsyncImage(
                        model = uiFeed.imageUrl,
                        contentScale = ContentScale.Crop,
                        placeholder = ColorPainter(Color.LightGray),
                        modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f),
                        contentDescription = null,
                    )
//                    OutlinedButton(
//                        onClick = {},
//                        shape = RoundedCornerShape(CornerSmall),
//                        colors = ButtonDefaults.buttonColors(
//                            backgroundColor = TextSecondaryColor,
//                            contentColor = TextSecondaryColor
//                        ),
//                        contentPadding = PaddingValues(PaddingSmall),
//                        modifier = Modifier
//                            .padding(horizontal = PaddingXXTiny)
//                            .align(Alignment.BottomEnd)
//                    ) {
//                        TextWithPlatformEmoji(
//                            modifier = Modifier,
//                            text = uiFeed.category.category,
//                            color = ToolbarColor,
//                            fontSize = FontSizeSmall,
//                        )
//                    }
                }


                Row {
                    LingoPulseMarkdown(
                        modifier = Modifier.weight(1f).padding(
                            start = PaddingMedium,
                            end = PaddingMedium,
                            top = PaddingSmall,
                            bottom = PaddingSmall
                        ),
                        content = uiFeed.title,
                        colors = markdownColor(
                            text = TextPrimaryColor,
                        )
                    )
                    Spacer(modifier = Modifier.width(PaddingXXTiny))
                    Text(
                        modifier = Modifier.padding(
                            start = PaddingMedium,
                            end = PaddingMedium,
                            top = PaddingSmall,
                            bottom = PaddingSmall
                        ),
                        text = timeAgo,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = FontSizeSmall,
                        color = TextSecondaryColor,
                    )
                }
            }
        }
    }
}
