package ai.lingopulse.domain.conversation.usecase

import ai.lingopulse.domain.conversation.model.ConversationMessage
import ai.lingopulse.domain.conversation.model.RunResponse
import ai.lingopulse.domain.conversation.repository.ConversationRepository
import kotlinx.coroutines.flow.Flow

class ConversationUseCaseImpl(
    private val conversationRepository: ConversationRepository
) : ConversationUseCase {
    
    override suspend fun createThread(messages: List<ConversationMessage>?): Flow<String> {
        println("🧵 [ConversationUseCase] createThread() called with ${messages?.size ?: 0} messages")
        return conversationRepository.createThread(messages)
    }
    
    override suspend fun startConversation(
        threadId: String,
        customInstructions: String?,
        messages: List<ConversationMessage>
    ): Flow<RunResponse> {
        println("💬 [ConversationUseCase] startConversation() called - threadId: $threadId, messages: ${messages.size}")
        println("📋 [ConversationUseCase] Custom instructions length: ${customInstructions?.length ?: 0}")
        return conversationRepository.createRunStream(threadId, customInstructions, messages)
    }
    
    override suspend fun transcribeAudio(filePath: String): Flow<String> {
        println("🗣️ [ConversationUseCase] transcribeAudio() called - filePath: $filePath")
        return conversationRepository.transcribeAudio(filePath)
    }
    
    override suspend fun generateSpeech(text: String): Flow<ByteArray> {
        println("🔊 [ConversationUseCase] generateSpeech() called - text length: ${text.length}")
        println("📝 [ConversationUseCase] Text preview: '${text.take(50)}...'")
        return conversationRepository.generateSpeech(text)
    }
}
