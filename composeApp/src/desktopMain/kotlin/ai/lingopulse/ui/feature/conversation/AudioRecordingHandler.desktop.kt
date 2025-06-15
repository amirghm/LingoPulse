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
                    // TODO: Implement desktop audio recording
                    viewModel.onRecordingError("Desktop audio recording not implemented yet")
                }
                is AudioRecordingEvent.StopRecording -> {
                    // TODO: Implement desktop audio recording
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
                    // TODO: Implement desktop audio playback
                    viewModel.onAudioPlaybackFinished()
                }
                is AudioPlaybackEvent.StopPlayback -> {
                    // TODO: Implement desktop audio playback stop
                    viewModel.onAudioPlaybackFinished()
                }
            }
        }
    }
}
