package ai.lingopulse.domain.conversation.usecase

import ai.lingopulse.domain.conversation.model.ConversationMessage
import ai.lingopulse.domain.conversation.model.RunResponse
import kotlinx.coroutines.flow.Flow

interface ConversationUseCase {
    suspend fun createThread(messages: List<ConversationMessage>? = null): Flow<String>
    
    suspend fun startConversation(
        threadId: String,
        customInstructions: String? = null,
        messages: List<ConversationMessage>
    ): Flow<RunResponse>
    
    suspend fun transcribeAudio(filePath: String): Flow<String>
    
    suspend fun generateSpeech(text: String): Flow<ByteArray>
}