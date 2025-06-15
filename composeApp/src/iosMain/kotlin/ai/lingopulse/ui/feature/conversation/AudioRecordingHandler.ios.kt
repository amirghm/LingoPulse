package ai.lingopulse.ui.feature.conversation

import ai.lingopulse.presentation.conversation.AudioPlaybackEvent
import ai.lingopulse.presentation.conversation.AudioRecordingEvent
import ai.lingopulse.presentation.conversation.ConversationViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
actual fun AudioRecordingHandler(viewModel: ConversationViewModel) {
    LaunchedEffect(viewModel) {
        viewModel.audioRecordingEvent.collect { event ->
            when (event) {
                is AudioRecordingEvent.StartRecording -> {
                    // TODO: Implement iOS audio recording
                    viewModel.onRecordingError("iOS audio recording not implemented yet")
                }
                is AudioRecordingEvent.StopRecording -> {
                    // TODO: Implement iOS audio recording
                    viewModel.onRecordingCompleted(null)
                }
            }
        }
    }
}

@Composable
actual fun AudioPlaybackHandler(viewModel: ConversationViewModel) {
    LaunchedEffect(viewModel) {
        viewModel.audioPlaybackEvent.collect { event ->
            when (event) {
                is AudioPlaybackEvent.StartPlayback -> {
                    // TODO: Implement iOS audio playback
                    viewModel.onAudioPlaybackFinished()
                }
                is AudioPlaybackEvent.StopPlayback -> {
                    // TODO: Implement iOS audio playback stop
                    viewModel.onAudioPlaybackFinished()
                }
            }
        }
    }
}
