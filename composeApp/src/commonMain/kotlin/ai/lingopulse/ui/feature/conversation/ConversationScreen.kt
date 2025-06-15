package ai.lingopulse.ui.feature.conversation

import ai.lingopulse.presentation.details.model.UiFeedDetails
import ai.lingopulse.presentation.common.model.ConversationState
import ai.lingopulse.ui.core.theme.*
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.dp as dpUnit
import coil3.compose.AsyncImage

@Composable
fun ConversationScreen(
    feedDetails: UiFeedDetails,
    conversationState: ConversationState,
    onBackClick: () -> Unit,
    onStartListening: () -> Unit,
    onStopListening: () -> Unit,
    onStopTalking: () -> Unit
) {
    
    val lazyListState = rememberLazyListState()

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
                        IconButton(onClick = onBackClick) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = TextSecondaryColor
                            )
                        }
                    },
                    title = {
                        Text(
                            text = "Discussion",
                            color = TextPrimaryColor,
                            fontSize = FontSizeLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                )
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundColor)
                    .padding(innerPadding),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top gradient shadow
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.1f)
                                )
                            )
                        )
                )

                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        // News Article Card
                        Card(
                            modifier = Modifier
                                .padding(PaddingLarge)
                                .fillMaxWidth(),
                            backgroundColor = Color.White,
                            shape = RoundedCornerShape(CornerSmall),
                            border = BorderStroke(
                                1.dp,
                                Color.Gray.copy(alpha = 0.2f)
                            ),
                            elevation = 4.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                            ) {
                                AsyncImage(
                                    model = feedDetails.imageUrl,
                                    contentDescription = "News Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(RoundedCornerShape(topStart = CornerSmall, bottomStart = CornerSmall))
                                )
                                
                                Column(
                                    modifier = Modifier
                                        .padding(PaddingMedium)
                                        .fillMaxHeight(),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = feedDetails.title,
                                        fontSize = FontSizeMedium,
                                        color = TextPrimaryColor,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        fontWeight = FontWeight.Medium
                                    )
                                    
                                    if (feedDetails.author.isNotEmpty()) {
                                        Text(
                                            text = "By ${feedDetails.author}",
                                            fontSize = FontSizeSmall,
                                            color = TextSecondaryColor
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        // Spacer to push orb to center
                        Spacer(modifier = Modifier.height(PaddingLarge))
                    }

                    item {
                        // Animated Orb - Centered
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AnimatedOrb(
                                modifier = Modifier.size(200.dp),
                                state = conversationState
                            )
                        }
                    }

                    item {
                        // Status Text
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = PaddingLarge)
                        ) {
                            Text(
                                text = when (conversationState) {
                                    ConversationState.Idle -> "Enhanced content ready for discussion"
                                    ConversationState.Listening -> "Listening... Speak now"
                                    ConversationState.Processing -> "Processing your message..."
                                    ConversationState.Talking -> "Speaking..."
                                    ConversationState.Error -> "Something went wrong. Try again."
                                },
                                fontSize = FontSizeMedium,
                                color = TextSecondaryColor
                            )
                            
                            if (conversationState == ConversationState.Idle) {
                                Text(
                                    text = "Tap below to start your voice conversation",
                                    fontSize = FontSizeSmall,
                                    color = TextSecondaryColor.copy(alpha = 0.7f),
                                    modifier = Modifier.padding(top = PaddingSmall)
                                )
                            }
                        }
                    }
                }

                // Bottom gradient shadow
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.1f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // Bottom Action Buttons
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(PaddingLarge)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PushToTalkButton(
                            modifier = Modifier.weight(1f),
                            state = conversationState,
                            onStartListening = onStartListening,
                            onStopListening = onStopListening
                        )
                        
                        Spacer(modifier = Modifier.width(PaddingMedium))
                        
                        // Stop button
                        Button(
                            onClick = onStopTalking,
                            modifier = Modifier.size(56.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.White,
                                contentColor = TextSecondaryColor
                            ),
                            border = BorderStroke(
                                1.dp,
                                Color.Gray.copy(alpha = 0.3f)
                            ),
                            enabled = conversationState == ConversationState.Talking || 
                                     conversationState == ConversationState.Processing
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Stop",
                                tint = if (conversationState == ConversationState.Talking || 
                                         conversationState == ConversationState.Processing) {
                                    Color.Red
                                } else {
                                    TextSecondaryColor.copy(alpha = 0.5f)
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun PushToTalkButton(
    modifier: Modifier,
    state: ConversationState,
    onStartListening: () -> Unit,
    onStopListening: () -> Unit
) {
    val buttonConfig = when (state) {
        ConversationState.Idle -> {
            ButtonConfig("Start Discussion", {
                println("🎯 [ConversationScreen] User clicked 'Start Discussion'")
                onStartListening()
            }, PrimaryColor, true)
        }
        ConversationState.Listening -> {
            ButtonConfig("Stop Listening", {
                println("🛑 [ConversationScreen] User clicked 'Stop Listening'")
                onStopListening()
            }, Color.Red, true)
        }
        ConversationState.Processing -> {
            ButtonConfig("Processing...", {
                println("⏳ [ConversationScreen] User clicked during processing (ignored)")
            }, Color.Gray, false)
        }
        ConversationState.Talking -> {
            ButtonConfig("Speaking...", {
                println("🗣️ [ConversationScreen] User clicked during talking (ignored)")
            }, Color.Gray, false)
        }
        ConversationState.Error -> {
            ButtonConfig("Try Again", {
                println("🔄 [ConversationScreen] User clicked 'Try Again'")
                onStartListening()
            }, AccentColor, true)
        }
    }
    
    Button(
        onClick = buttonConfig.onClick,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = buttonConfig.color,
            contentColor = Color.White,
            disabledBackgroundColor = Color.Gray.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(CornerMedium),
        enabled = buttonConfig.enabled
    ) {
        Text(
            text = buttonConfig.text,
            fontSize = FontSizeMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun AnimatedOrb(
    modifier: Modifier,
    state: ConversationState
) {
    val scale = remember { Animatable(1f) }
    val alpha = remember { Animatable(1f) }

    LaunchedEffect(state) {
        when (state) {
            ConversationState.Idle -> {
                scale.animateTo(1f)
                alpha.animateTo(1f)
            }
            ConversationState.Listening -> {
                scale.animateTo(0.8f, animationSpec = tween(200))
                while (state == ConversationState.Listening) {
                    alpha.animateTo(0.6f, animationSpec = tween(800, easing = LinearEasing))
                    alpha.animateTo(1f, animationSpec = tween(800, easing = LinearEasing))
                }
            }
            ConversationState.Processing -> {
                scale.animateTo(1f)
                while (state == ConversationState.Processing) {
                    alpha.animateTo(0.4f, animationSpec = tween(1000, easing = LinearEasing))
                    alpha.animateTo(1f, animationSpec = tween(1000, easing = LinearEasing))
                }
            }
            ConversationState.Talking -> {
                scale.animateTo(1.1f, animationSpec = tween(150))
                while (state == ConversationState.Talking) {
                    alpha.animateTo(0.7f, animationSpec = tween(600, easing = LinearEasing))
                    alpha.animateTo(1f, animationSpec = tween(600, easing = LinearEasing))
                }
            }
            ConversationState.Error -> {
                scale.animateTo(0.9f)
                alpha.animateTo(0.6f)
            }
        }
    }

    Box(
        modifier = modifier
            .scale(scale.value)
            .alpha(alpha.value)
            .background(
                color = when (state) {
                    ConversationState.Idle -> AccentColor.copy(alpha = 0.8f)
                    ConversationState.Listening -> Color.Blue.copy(alpha = 0.8f)
                    ConversationState.Processing -> Color(0xFFFFA500).copy(alpha = 0.8f) // Orange
                    ConversationState.Talking -> Color.Green.copy(alpha = 0.8f)
                    ConversationState.Error -> Color.Red.copy(alpha = 0.8f)
                },
                shape = CircleShape
            )
    )
}

data class ButtonConfig(
    val text: String,
    val onClick: () -> Unit,
    val color: Color,
    val enabled: Boolean
)
