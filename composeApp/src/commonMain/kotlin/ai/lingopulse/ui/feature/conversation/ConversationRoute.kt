package ai.lingopulse.ui.feature.conversation

import ai.lingopulse.presentation.conversation.AudioRecordingEvent
import ai.lingopulse.presentation.conversation.ConversationScreenNavigationInteraction
import ai.lingopulse.presentation.conversation.ConversationScreenUiState
import ai.lingopulse.presentation.common.model.ConversationState
import ai.lingopulse.presentation.conversation.ConversationViewModel
import ai.lingopulse.ui.core.common.model.ScreenEvent
import ai.lingopulse.ui.core.common.model.ScreenEventHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController

@Composable
internal fun ConversationRoute(
    viewModel: ConversationViewModel,
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsState()
    val eventHandler: ScreenEventHandler = object : ScreenEventHandler {
        override fun onEvent(event: ScreenEvent) {
            when (event) {
                is ConversationScreenEvent.OnBackClicked -> {
                    viewModel.onBackClicked()
                }
                is ConversationScreenEvent.OnStartListening -> {
                    viewModel.onStartListening()
                }
                is ConversationScreenEvent.OnStopListening -> {
                    viewModel.onStopListening()
                }
                is ConversationScreenEvent.OnStopTalking -> {
                    viewModel.onStopTalking()
                }
                is ConversationScreenEvent.OnRetryAfterError -> {
                    viewModel.onRetryAfterError()
                }
                is ConversationScreenEvent.OnVoiceInputReceived -> {
                    viewModel.onVoiceInputReceived(event.audioData)
                }
                is ConversationScreenEvent.OnAudioPlaybackFinished -> {
                    viewModel.onAudioPlaybackFinished(event.messageId)
                }
            }
        }
    }

    // Handle navigation events
    LaunchedEffect(viewModel) {
        viewModel.navigationInteraction.collect {
            when (it) {
                is ConversationScreenNavigationInteraction.NavigateUp -> {
                    navController.navigateUp()
                }
            }
        }
    }

    when (val state = uiState) {
        is ConversationScreenUiState.Loading -> {
            ConversationScreenWithAudio(
                feedDetails = state.feedDetails,
                conversationState = ConversationState.Processing,
                eventHandler = eventHandler,
                viewModel = viewModel
            )
        }
        is ConversationScreenUiState.ConversationReady -> {
            ConversationScreenWithAudio(
                feedDetails = state.feedDetails,
                conversationState = state.conversationState,
                eventHandler = eventHandler,
                viewModel = viewModel
            )
        }
        is ConversationScreenUiState.Error -> {
            ConversationScreenWithAudio(
                feedDetails = state.feedDetails,
                conversationState = ConversationState.Error,
                eventHandler = eventHandler,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun ConversationScreenWithAudio(
    feedDetails: ai.lingopulse.presentation.details.model.UiFeedDetails,
    conversationState: ConversationState,
    eventHandler: ScreenEventHandler,
    viewModel: ConversationViewModel
) {
    // Audio recording handler
    AudioRecordingHandler(viewModel = viewModel)
    
    // Audio playback handler
    AudioPlaybackHandler(viewModel = viewModel)
    
    ConversationScreen(
        feedDetails = feedDetails,
        conversationState = conversationState,
        onBackClick = { eventHandler.onEvent(ConversationScreenEvent.OnBackClicked) },
        onStartListening = { eventHandler.onEvent(ConversationScreenEvent.OnStartListening) },
        onStopListening = { eventHandler.onEvent(ConversationScreenEvent.OnStopListening) },
        onStopTalking = { eventHandler.onEvent(ConversationScreenEvent.OnStopTalking) }
    )
}
