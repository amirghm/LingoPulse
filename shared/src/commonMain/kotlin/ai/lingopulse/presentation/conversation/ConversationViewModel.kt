package ai.lingopulse.presentation.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ai.lingopulse.domain.common.model.FeedDetails
import ai.lingopulse.domain.conversation.model.ConversationMessage
import ai.lingopulse.domain.conversation.model.MessageContent
import ai.lingopulse.domain.conversation.usecase.ConversationUseCase
import ai.lingopulse.presentation.common.model.ConversationState
import ai.lingopulse.presentation.details.mapper.toUiFeedDetails
import ai.lingopulse.presentation.details.model.UiFeedDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ConversationViewModel(
    private val conversationUseCase: ConversationUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private var feedDetails: FeedDetails? = null
    private var currentRecordingPath: String? = null
    private var threadId: String? = null
    
    // Audio queue for streaming playback
    private val audioQueue = mutableListOf<ByteArray>()
    private var isPlayingAudio = false
    private var currentAudioIndex = 0

    private val _uiState: MutableStateFlow<ConversationScreenUiState> =
        MutableStateFlow(ConversationScreenUiState.Loading(
            UiFeedDetails(
                id = "",
                category = "",
                title = "",
                subtitle = "",
                textContent = "",
                author = "",
                url = "",
                imageUrl = "",
                publishedAt = ""
            )
        ))
    val uiState: StateFlow<ConversationScreenUiState> = _uiState.asStateFlow()

    private val _navigationInteraction = MutableSharedFlow<ConversationScreenNavigationInteraction>()
    val navigationInteraction: SharedFlow<ConversationScreenNavigationInteraction> = _navigationInteraction

    private val _audioRecordingEvent = MutableSharedFlow<AudioRecordingEvent>()
    val audioRecordingEvent: SharedFlow<AudioRecordingEvent> = _audioRecordingEvent

    private val _audioPlaybackEvent = MutableSharedFlow<AudioPlaybackEvent>()
    val audioPlaybackEvent: SharedFlow<AudioPlaybackEvent> = _audioPlaybackEvent

    fun setup(feedDetails: FeedDetails) {
        this.feedDetails = feedDetails
        
        // Create thread for conversation
        viewModelScope.launch(dispatcher) {
            try {
                conversationUseCase.createThread().collect { id ->
                    threadId = id
                    println("Conversation thread created: $id")
                    
                    _uiState.value = ConversationScreenUiState.ConversationReady(
                        feedDetails = feedDetails.toUiFeedDetails(),
                        conversationState = ConversationState.Idle
                    )
                }
            } catch (e: Exception) {
                _uiState.value = ConversationScreenUiState.Error(
                    feedDetails = feedDetails.toUiFeedDetails(),
                    message = "Failed to initialize conversation: ${e.message}"
                )
            }
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            // Stop any ongoing recording before navigating away
            if (getCurrentState() == ConversationState.Listening) {
                _audioRecordingEvent.emit(AudioRecordingEvent.StartRecording)
            }
            _navigationInteraction.emit(ConversationScreenNavigationInteraction.NavigateUp)
        }
    }

    fun onStartListening() {
        viewModelScope.launch(dispatcher) {
            feedDetails?.let { details ->
                _uiState.value = ConversationScreenUiState.ConversationReady(
                    feedDetails = details.toUiFeedDetails(),
                    conversationState = ConversationState.Listening
                )
                _audioRecordingEvent.emit(AudioRecordingEvent.StartRecording)
            }
        }
    }

    fun onStopListening() {
        viewModelScope.launch(dispatcher) {
            feedDetails?.let { details ->
                _uiState.value = ConversationScreenUiState.ConversationReady(
                    feedDetails = details.toUiFeedDetails(),
                    conversationState = ConversationState.Processing
                )
                _audioRecordingEvent.emit(AudioRecordingEvent.StopRecording)
            }
        }
    }

    fun onStopTalking() {
        viewModelScope.launch(dispatcher) {
            // Stop audio playback
            _audioPlaybackEvent.emit(AudioPlaybackEvent.StopPlayback)
            
            feedDetails?.let { details ->
                _uiState.value = ConversationScreenUiState.ConversationReady(
                    feedDetails = details.toUiFeedDetails(),
                    conversationState = ConversationState.Idle
                )
            }
        }
    }

    fun onRetryAfterError() {
        viewModelScope.launch(dispatcher) {
            feedDetails?.let { details ->
                _uiState.value = ConversationScreenUiState.ConversationReady(
                    feedDetails = details.toUiFeedDetails(),
                    conversationState = ConversationState.Idle
                )
            }
        }
    }

    fun onVoiceInputReceived(audioData: ByteArray) {
        viewModelScope.launch(dispatcher) {
            feedDetails?.let { details ->
                _uiState.value = ConversationScreenUiState.ConversationReady(
                    feedDetails = details.toUiFeedDetails(),
                    conversationState = ConversationState.Processing
                )
                // TODO: Process voice input and get AI response
            }
        }
    }

    fun onAudioPlaybackFinished(messageId: String) {
        println("🔊 [ConversationViewModel] Audio playback finished for message: $messageId")
        onPlaybackFinished()
    }

    fun onAudioPlaybackStarted() {
        // Audio playback has started successfully
        println("🔊 [ConversationViewModel] Audio playback started")
    }

    fun onAudioPlaybackFinished() {
        // Audio playback completed, delegate to queue management
        println("🔊 [ConversationViewModel] Audio playback finished - delegating to queue management")
        onPlaybackFinished()
    }

    fun onRecordingStarted(filePath: String) {
        currentRecordingPath = filePath
        println("🎤 [ConversationViewModel] Recording started: $filePath")
    }

    fun onRecordingCompleted(filePath: String?) {
        println("🎤 [ConversationViewModel] Recording completed: $filePath")
        currentRecordingPath = filePath
        if (filePath != null) {
            println("📁 [ConversationViewModel] Recording file saved successfully, starting audio processing...")
            processRecordedAudio(filePath)
        } else {
            // Recording failed, show error state
            println("❌ [ConversationViewModel] Recording failed - no file path provided")
            viewModelScope.launch {
                feedDetails?.let { details ->
                    _uiState.value = ConversationScreenUiState.Error(
                        feedDetails = details.toUiFeedDetails(),
                        message = "Failed to record audio"
                    )
                }
            }
        }
    }

    private fun processRecordedAudio(filePath: String) {
        println("🔄 [ConversationViewModel] Starting audio processing for: $filePath")
        viewModelScope.launch(dispatcher) {
            try {
                println("🗣️ [ConversationViewModel] Starting audio transcription...")
                // Step 1: Transcribe audio to text
                conversationUseCase.transcribeAudio(filePath)
                    .catch { e ->
                        println("❌ [ConversationViewModel] Transcription failed: ${e.message}")
                        handleError("Transcription failed: ${e.message}")
                    }
                    .collect { transcribedText ->
                        println("✅ [ConversationViewModel] Transcription successful: '$transcribedText'")
                        
                        // Step 2: Send message to AI and get streaming response
                        threadId?.let { id ->
                            println("🤖 [ConversationViewModel] Starting AI conversation with thread: $id")
                            startConversation(id, transcribedText)
                        } ?: run {
                            println("❌ [ConversationViewModel] No conversation thread available")
                            handleError("No conversation thread available")
                        }
                    }
            } catch (e: Exception) {
                println("❌ [ConversationViewModel] Audio processing failed: ${e.message}")
                println("📊 [ConversationViewModel] Exception stack trace: ${e.stackTraceToString()}")
                handleError("Audio processing failed: ${e.message}")
            }
        }
    }

    private fun startConversation(threadId: String, userMessage: String) {
        println("💬 [ConversationViewModel] Starting conversation - Thread: $threadId, Message: '$userMessage'")
        viewModelScope.launch(dispatcher) {
            try {
                val messages = listOf(
                    ConversationMessage(
                        role = "user",
                        content = listOf(MessageContent.text(text = userMessage))
                    )
                )
                
                val customInstructions = buildCustomInstructions()
                println("📋 [ConversationViewModel] Custom instructions prepared (${customInstructions.length} chars)")
                
                // Update state to talking when AI starts responding
                feedDetails?.let { details ->
                    println("🎯 [ConversationViewModel] Updating UI state to Talking")
                    _uiState.value = ConversationScreenUiState.ConversationReady(
                        feedDetails = details.toUiFeedDetails(),
                        conversationState = ConversationState.Talking
                    )
                }
                
                println("🌊 [ConversationViewModel] Starting streaming conversation...")
                conversationUseCase.startConversation(threadId, customInstructions, messages)
                    .catch { e ->
                        println("❌ [ConversationViewModel] Streaming conversation failed: ${e.message}")
                        println("📊 [ConversationViewModel] Exception stack trace: ${e.stackTraceToString()}")
                        handleError("Conversation failed: ${e.message}")
                    }
                    .collect { response ->
                        println("📝 [ConversationViewModel] AI Response received: '${response.message}'")
                        
                        // Convert AI response to speech
                        generateSpeechFromText(response.message)
                    }
            } catch (e: Exception) {
                println("❌ [ConversationViewModel] Conversation error: ${e.message}")
                println("📊 [ConversationViewModel] Exception stack trace: ${e.stackTraceToString()}")
                handleError("Conversation error: ${e.message}")
            }
        }
    }

    private fun generateSpeechFromText(text: String) {
        println("🔊 [ConversationViewModel] Starting TTS generation for text: '${text.take(50)}...'")
        viewModelScope.launch(dispatcher) {
            try {
                // Split text into paragraphs
                val paragraphs = text.split("\n\n")
                println("📄 [ConversationViewModel] Split text into ${paragraphs.size} paragraphs")
                
                // Process each paragraph in sequence
                for ((index, paragraph) in paragraphs.withIndex()) {
                    if (paragraph.isNotBlank()) {
                        println("🎵 [ConversationViewModel] Generating TTS for paragraph $index: '${paragraph.take(30)}...'")
                        conversationUseCase.generateSpeech(paragraph)
                            .catch { e ->
                                println("❌ [ConversationViewModel] TTS generation failed for paragraph $index: ${e.message}")
                                handleError("Speech generation failed: ${e.message}")
                            }
                            .collect { audioData ->
                                println("✅ [ConversationViewModel] TTS generated for paragraph $index, size: ${audioData.size} bytes")
                                
                                // Add to audio queue
                                audioQueue.add(audioData)
                                println("🎶 [ConversationViewModel] Audio queue size: ${audioQueue.size}")
                                
                                // If not already playing, start playback
                                if (!isPlayingAudio) {
                                    println("▶️ [ConversationViewModel] Starting audio playback queue")
                                    playNextAudio()
                                }
                            }
                    }
                }
            } catch (e: Exception) {
                println("❌ [ConversationViewModel] TTS generation error: ${e.message}")
                println("📊 [ConversationViewModel] Exception stack trace: ${e.stackTraceToString()}")
                handleError("Speech generation error: ${e.message}")
            }
        }
    }

    private fun playNextAudio() {
        println("🎵 [ConversationViewModel] playNextAudio() called - Queue size: ${audioQueue.size}, isPlaying: $isPlayingAudio")
        
        if (audioQueue.isNotEmpty() && !isPlayingAudio) {
            isPlayingAudio = true
            currentAudioIndex = 0
            println("▶️ [ConversationViewModel] Starting playback of audio chunk ${currentAudioIndex}")
            
            viewModelScope.launch {
                _audioPlaybackEvent.emit(AudioPlaybackEvent.StartPlayback(audioQueue[currentAudioIndex]))
            }
        } else if (audioQueue.isNotEmpty() && isPlayingAudio) {
            println("⏸️ [ConversationViewModel] Audio already playing, queuing audio chunk")
        } else {
            println("🔇 [ConversationViewModel] No audio in queue to play")
        }
    }

    fun onPlaybackFinished() {
        println("🔄 [ConversationViewModel] onPlaybackFinished() called - Current index: $currentAudioIndex, Queue size: ${audioQueue.size}")
        
        // Move to next audio in queue
        currentAudioIndex++
        
        if (currentAudioIndex < audioQueue.size) {
            println("▶️ [ConversationViewModel] Playing next audio chunk: $currentAudioIndex")
            viewModelScope.launch {
                _audioPlaybackEvent.emit(AudioPlaybackEvent.StartPlayback(audioQueue[currentAudioIndex]))
            }
        } else {
            // All audio played, return to idle state
            println("✅ [ConversationViewModel] All audio chunks played, returning to idle state")
            isPlayingAudio = false
            audioQueue.clear()
            currentAudioIndex = 0
            
            viewModelScope.launch {
                feedDetails?.let { details ->
                    _uiState.value = ConversationScreenUiState.ConversationReady(
                        feedDetails = details.toUiFeedDetails(),
                        conversationState = ConversationState.Idle
                    )
                }
            }
        }
    }

    private fun buildCustomInstructions(): String {
        return feedDetails?.let { details ->
            """
            You are a language learning assistant helping users practice speaking about news articles.
            
            Article Title: ${details.title}
            Article Content: ${details.content}
            
            Please respond in a conversational way that helps the user practice the language while discussing this article.
            Keep responses concise and engaging. Ask follow-up questions to encourage more conversation.
            """.trimIndent()
        } ?: "You are a helpful language learning assistant."
    }

    fun onRecordingError(errorMessage: String) {
        handleError(errorMessage)
    }

    private fun handleError(errorMessage: String) {
        println("❌ [ConversationViewModel] Handling error: $errorMessage")
        
        // Stop any ongoing audio playback
        viewModelScope.launch {
            _audioPlaybackEvent.emit(AudioPlaybackEvent.StopPlayback)
        }
        
        // Clear audio queue
        isPlayingAudio = false
        audioQueue.clear()
        
        viewModelScope.launch {
            feedDetails?.let { details ->
                println("🔄 [ConversationViewModel] Transitioning to Error state")
                _uiState.value = ConversationScreenUiState.Error(
                    feedDetails = details.toUiFeedDetails(),
                    message = errorMessage
                )
            }
        }
    }

    private fun getCurrentState(): ConversationState {
        return when (val state = _uiState.value) {
            is ConversationScreenUiState.ConversationReady -> state.conversationState
            is ConversationScreenUiState.Loading -> ConversationState.Processing
            is ConversationScreenUiState.Error -> ConversationState.Error
        }
    }
}

sealed class AudioRecordingEvent {
    object StartRecording : AudioRecordingEvent()
    object StopRecording : AudioRecordingEvent()
}

sealed class AudioPlaybackEvent {
    data class StartPlayback(val audioData: ByteArray) : AudioPlaybackEvent() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as StartPlayback

            if (!audioData.contentEquals(other.audioData)) return false

            return true
        }

        override fun hashCode(): Int {
            return audioData.contentHashCode()
        }
    }
    object StopPlayback : AudioPlaybackEvent()
}
