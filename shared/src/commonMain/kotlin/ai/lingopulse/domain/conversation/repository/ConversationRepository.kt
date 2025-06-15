package ai.lingopulse.domain.conversation.repository

import ai.lingopulse.domain.conversation.model.*
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    suspend fun createThread(messages: List<ConversationMessage>? = null): Flow<String>
    
    suspend fun createRunStream(
        threadId: String,
        customInstructions: String? = null,
        messages: List<ConversationMessage>
    ): Flow<RunResponse>
    
    suspend fun transcribeAudio(
        filePath: String,
        model: String = "whisper-1",
        language: String? = null,
        responseFormat: String? = "text",
        temperature: Float? = 0f
    ): Flow<String>
    
    suspend fun generateSpeech(
        text: String,
        voice: String = "nova",
        format: String? = "mp3",
        speed: Double? = 1.0
    ): Flow<ByteArray>
}