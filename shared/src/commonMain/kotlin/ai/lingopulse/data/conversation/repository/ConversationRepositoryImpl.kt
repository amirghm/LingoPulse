package ai.lingopulse.data.conversation.repository

import ai.lingopulse.data.conversation.remote.ConversationNetworkAPI
import ai.lingopulse.domain.conversation.model.*
import ai.lingopulse.domain.conversation.repository.ConversationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ConversationRepositoryImpl(
    private val conversationNetworkApi: ConversationNetworkAPI,
    private val assistantId: String = "asst_1BG5nYmfNfFg2TZTBoDLMU9s"
) : ConversationRepository {

    override suspend fun createThread(messages: List<ConversationMessage>?): Flow<String> = flow {
        println("🌐 [ConversationRepository] Creating thread with ${messages?.size ?: 0} messages")
        try {
            val request = CreateThreadRequest(messages = messages)
            println("📤 [ConversationRepository] Sending createThread request...")
            val response = conversationNetworkApi.createThread(request)
            println("📥 [ConversationRepository] Thread created successfully: ${response.id}")
            emit(response.id)
        } catch (e: Exception) {
            println("❌ [ConversationRepository] Failed to create thread: ${e.message}")
            println("📊 [ConversationRepository] Exception stack trace: ${e.stackTraceToString()}")
            throw e
        }
    }

    override suspend fun createRunStream(
        threadId: String,
        customInstructions: String?,
        messages: List<ConversationMessage>
    ): Flow<RunResponse> {
        println("🌊 [ConversationRepository] Starting run stream - threadId: $threadId, assistantId: $assistantId")
        println("📋 [ConversationRepository] Custom instructions: ${customInstructions?.take(100)}...")
        println("💬 [ConversationRepository] Messages count: ${messages.size}")
        
        try {
            val request = CreateRunRequest(
                assistant_id = assistantId,
                additional_instructions = customInstructions,
                additional_messages = messages,
                stream = true
            )
            
            println("📤 [ConversationRepository] Sending createRunStream request with assistantId: ${request.assistant_id}")
            return conversationNetworkApi.createRunStream(threadId, request)
        } catch (e: Exception) {
            println("❌ [ConversationRepository] Failed to start run stream: ${e.message}")
            println("📊 [ConversationRepository] Exception stack trace: ${e.stackTraceToString()}")
            throw e
        }
    }

    override suspend fun transcribeAudio(
        filePath: String,
        model: String,
        language: String?,
        responseFormat: String?,
        temperature: Float?
    ): Flow<String> = flow {
        println("🎧 [ConversationRepository] Transcribing audio file: $filePath")
        println("🔧 [ConversationRepository] Model: $model, Language: $language, Format: $responseFormat, Temp: $temperature")
        try {
            println("📤 [ConversationRepository] Sending transcription request...")
            val response = conversationNetworkApi.transcribeAudio(
                filePath = filePath,
                model = model,
                language = language,
                responseFormat = responseFormat,
                temperature = temperature
            )
            println("📥 [ConversationRepository] Transcription successful: '${response.text}'")
            emit(response.text)
        } catch (e: Exception) {
            println("❌ [ConversationRepository] Failed to transcribe audio: ${e.message}")
            println("📊 [ConversationRepository] Exception stack trace: ${e.stackTraceToString()}")
            throw e
        }
    }

    override suspend fun generateSpeech(
        text: String,
        voice: String,
        format: String?,
        speed: Double?
    ): Flow<ByteArray> = flow {
        println("🎤 [ConversationRepository] Generating speech - text length: ${text.length}, voice: $voice")
        println("📝 [ConversationRepository] Format: $format, Speed: $speed")
        println("📝 [ConversationRepository] Text preview: '${text.take(50)}...'")
        try {
            val request = TextToSpeechRequest(
                input = text,
                voice = voice,
                response_format = format,
                speed = speed
            )
            println("📤 [ConversationRepository] Sending TTS request...")
            val audioData = conversationNetworkApi.generateSpeech(request)
            println("📥 [ConversationRepository] TTS successful - audio size: ${audioData.size} bytes")
            emit(audioData)
        } catch (e: Exception) {
            println("❌ [ConversationRepository] Failed to generate speech: ${e.message}")
            println("📊 [ConversationRepository] Exception stack trace: ${e.stackTraceToString()}")
            throw e
        }
    }
}
