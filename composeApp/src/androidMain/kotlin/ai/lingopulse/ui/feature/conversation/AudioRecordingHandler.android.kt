package ai.lingopulse.ui.feature.conversation

import ai.lingopulse.presentation.conversation.AudioPlaybackEvent
import ai.lingopulse.presentation.conversation.AudioRecordingEvent
import ai.lingopulse.presentation.conversation.ConversationViewModel
import ai.lingopulse.util.audio.AudioPlayer
import ai.lingopulse.util.audio.AudioRecorder
import ai.lingopulse.util.permission.PermissionManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import android.Manifest

@Composable
actual fun AudioRecordingHandler(viewModel: ConversationViewModel) {
    val context = LocalContext.current
    val audioRecorder = remember { AudioRecorder(context) }
    val permissionManager = remember { PermissionManager(context) }
    
    var pendingRecordingRequest by remember { mutableStateOf(false) }
    
    println("🔧 [AudioRecordingHandler] Component initialized")
    
    // Permission launcher - this must be created during composition
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        println("🔐 [AudioRecordingHandler] Permission result: isGranted=$isGranted, pendingRequest=$pendingRecordingRequest")
        if (isGranted && pendingRecordingRequest) {
            pendingRecordingRequest = false
            println("🎤 [AudioRecordingHandler] Permission granted, starting recording...")
            val filePath = audioRecorder.startRecording()
            if (filePath != null) {
                println("✅ [AudioRecordingHandler] Recording started successfully: $filePath")
                viewModel.onRecordingStarted(filePath)
            } else {
                println("❌ [AudioRecordingHandler] Failed to start recording")
                viewModel.onRecordingError("Failed to start recording")
            }
        } else if (pendingRecordingRequest) {
            pendingRecordingRequest = false
            println("❌ [AudioRecordingHandler] Permission denied")
            viewModel.onRecordingError("Microphone permission denied")
        }
    }
    
    LaunchedEffect(viewModel) {
        println("👂 [AudioRecordingHandler] Starting to listen for recording events...")
        viewModel.audioRecordingEvent.collect { event ->
            println("📢 [AudioRecordingHandler] Received event: $event")
            when (event) {
                is AudioRecordingEvent.StartRecording -> {
                    println("🎯 [AudioRecordingHandler] Processing StartRecording event...")
                    if (permissionManager.checkAudioPermission()) {
                        println("✅ [AudioRecordingHandler] Permission already granted, starting recording...")
                        val filePath = audioRecorder.startRecording()
                        if (filePath != null) {
                            println("✅ [AudioRecordingHandler] Recording started successfully: $filePath")
                            viewModel.onRecordingStarted(filePath)
                        } else {
                            println("❌ [AudioRecordingHandler] Failed to start recording")
                            viewModel.onRecordingError("Failed to start recording")
                        }
                    } else {
                        println("🔐 [AudioRecordingHandler] Permission not granted, requesting...")
                        // Request permission
                        pendingRecordingRequest = true
                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                }
                is AudioRecordingEvent.StopRecording -> {
                    println("🛑 [AudioRecordingHandler] Processing StopRecording event...")
                    val filePath = audioRecorder.stopRecording()
                    println("📁 [AudioRecordingHandler] Recording stopped, file path: $filePath")
                    viewModel.onRecordingCompleted(filePath)
                }
            }
        }
    }
}

@Composable
actual fun AudioPlaybackHandler(viewModel: ConversationViewModel) {
    val context = LocalContext.current
    val audioPlayer = remember { AudioPlayer(context) }
    
    LaunchedEffect(viewModel) {
        viewModel.audioPlaybackEvent.collect { event ->
            when (event) {
                is AudioPlaybackEvent.StartPlayback -> {
                    viewModel.onAudioPlaybackStarted()
                    
                    // Play the actual audio data from TTS
                    audioPlayer.playAudio(event.audioData) {
                        // Called when audio playback completes - triggers next in queue
                        viewModel.onPlaybackFinished()
                    }
                }
                is AudioPlaybackEvent.StopPlayback -> {
                    audioPlayer.stopPlayback()
                    viewModel.onAudioPlaybackFinished()
                }
            }
        }
    }
}
