package ai.lingopulse.ui.feature.details

import ai.lingopulse.presentation.details.FeedDetailsScreenUiState
import ai.lingopulse.presentation.details.model.UiFeedDetails
import ai.lingopulse.ui.core.common.model.ScreenEventHandler
import ai.lingopulse.ui.core.common.component.LingoPulseMarkdown
import ai.lingopulse.ui.core.theme.*
import ai.lingopulse.util.extension.shimmerEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mikepenz.markdown.m2.markdownColor
import kotlinx.coroutines.delay
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun FeedDetailsScreen(
    state: FeedDetailsScreenUiState,
    eventHandler: ScreenEventHandler
) {
    when (state) {
        is FeedDetailsScreenUiState.Loading -> {
            FeedDetailsContent(
                feedDetails = state.feedDetails,
                eventHandler = eventHandler,
                isLoading = true
            )
        }
        is FeedDetailsScreenUiState.FeedDetailsLoaded -> {
            FeedDetailsContent(
                feedDetails = state.feedDetails,
                eventHandler = eventHandler,
                isLoading = false
            )
        }
        is FeedDetailsScreenUiState.Error -> {
            ErrorContent(
                message = state.message,
                eventHandler = eventHandler
            )
        }
    }
}

@Composable
private fun FeedDetailsContent(
    feedDetails: UiFeedDetails,
    eventHandler: ScreenEventHandler,
    isLoading: Boolean
) {
    var timeAgo by remember { mutableStateOf("") }

    LaunchedEffect(feedDetails.publishedAt) {
        while (true) {
            timeAgo = try {
                val instant = Instant.parse(feedDetails.publishedAt)
                val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
                "${localDateTime.date} at ${localDateTime.hour.toString().padStart(2, '0')}:${localDateTime.minute.toString().padStart(2, '0')}"
            } catch (e: Exception) {
                feedDetails.publishedAt
            }
            delay(10000)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
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
                        elevation = 0.dp,
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    eventHandler.onEvent(FeedDetailsScreenEvent.OnBackClicked)
                                }
                            ) {
                                Icon(
                                    Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = TextSecondaryColor
                                )
                            }
                        },
                        title = {
                            Text(
                                text = "Article Details",
                                color = TextPrimaryColor,
                                fontSize = FontSizeLarge,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    )
                }
            },
            content = { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackgroundColor)
                        .padding(innerPadding)
                        .padding(bottom = 80.dp), // Space for sticky button
                    verticalArrangement = Arrangement.spacedBy(PaddingMedium)
                ) {
                    // Hero Image
                    item {
                        AsyncImage(
                            model = feedDetails.imageUrl,
                            contentScale = ContentScale.Crop,
                            placeholder = ColorPainter(Color.LightGray),
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                                .shimmerEffect(show = isLoading),
                            contentDescription = null,
                        )
                    }

                    // Title
                    item {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = PaddingLarge)
                                .shimmerEffect(show = isLoading),
                            text = feedDetails.title,
                            fontSize = FontSizeXXLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryColor
                        )
                    }

                    // Date and Author
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = PaddingLarge),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.shimmerEffect(show = isLoading),
                                text = timeAgo,
                                fontSize = FontSizeSmall,
                                color = TextSecondaryColor,
                            )
                            if (feedDetails.author.isNotEmpty()) {
                                Text(
                                    modifier = Modifier.shimmerEffect(show = isLoading),
                                    text = "By ${feedDetails.author}",
                                    fontSize = FontSizeSmall,
                                    color = TextSecondaryColor,
                                )
                            }
                        }
                    }

                    // Content
                    item {
                        val formattedContent = feedDetails.textContent.replace("\\n", "\n")
                        LingoPulseMarkdown(
                            modifier = Modifier
                                .padding(horizontal = PaddingLarge)
                                .shimmerEffect(show = isLoading),
                            content = formattedContent,
                            colors = markdownColor(
                                text = TextPrimaryColor,
                            )
                        )
                    }

                    // Bottom spacing
                    item {
                        Spacer(modifier = Modifier.height(PaddingLarge))
                    }
                }
            }
        )

        // Sticky Listen and Learn Button
        Button(
            onClick = {
                eventHandler.onEvent(
                    FeedDetailsScreenEvent.OnStartConversationClicked(feedDetails)
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(PaddingLarge),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = PrimaryColor,
                contentColor = ToolbarColor
            ),
            shape = RoundedCornerShape(CornerSmall),
            contentPadding = PaddingValues(PaddingMedium),
            enabled = !isLoading
        ) {
            Text(
                text = if (isLoading) "Loading..." else "Listen and Learn",
                fontSize = FontSizeMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: String?,
    eventHandler: ScreenEventHandler
) {
    Scaffold(
        topBar = {
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
                    elevation = 0.dp,
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                eventHandler.onEvent(FeedDetailsScreenEvent.OnBackClicked)
                            }
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = TextSecondaryColor
                            )
                        }
                    },
                    title = {
                        Text(
                            text = "Error",
                            color = TextPrimaryColor,
                            fontSize = FontSizeLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                )
            }
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundColor)
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(PaddingMedium)
                ) {
                    Text(
                        text = "Something went wrong",
                        fontSize = FontSizeLarge,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimaryColor
                    )
                    message?.let {
                        Text(
                            text = it,
                            fontSize = FontSizeMedium,
                            color = TextSecondaryColor
                        )
                    }
                }
            }
        }
    )
}
