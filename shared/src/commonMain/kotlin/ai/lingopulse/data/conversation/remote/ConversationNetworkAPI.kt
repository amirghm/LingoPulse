package ai.lingopulse.data.conversation.remote

import ai.lingopulse.domain.conversation.model.*
import kotlinx.coroutines.flow.Flow

interface ConversationNetworkAPI {
    suspend fun createThread(request: CreateThreadRequest): CreateThreadResponse
    
    suspend fun createRunStream(
        threadId: String,
        request: CreateRunRequest
    ): Flow<RunResponse>
    
    suspend fun transcribeAudio(
        filePath: String,
        model: String = "whisper-1",
        language: String? = null,
        responseFormat: String? = "text",
        temperature: Float? = 0f
    ): TranscriptionResponse
    
    suspend fun generateSpeech(request: TextToSpeechRequest): ByteArray
}