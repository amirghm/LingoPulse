package ai.lingopulse.ui.feature.conversation

import ai.lingopulse.presentation.conversation.AudioRecordingEvent
import ai.lingopulse.presentation.conversation.AudioPlaybackEvent
import ai.lingopulse.presentation.conversation.ConversationViewModel
import ai.lingopulse.util.audio.AudioRecorder
import ai.lingopulse.util.audio.AudioPlayer
import ai.lingopulse.util.permission.PermissionManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember

@Composable
expect fun AudioRecordingHandler(viewModel: ConversationViewModel)

@Composable
expect fun AudioPlaybackHandler(viewModel: ConversationViewModel)
