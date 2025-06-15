package ai.lingopulse.data.conversation.remote

import ai.lingopulse.BuildKonfig
import ai.lingopulse.domain.conversation.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.encodeToString

/**
 * ConversationNetworkService handles OpenAI Assistants API v2 interactions.
 *
 * IMPORTANT: The assistant_id must be passed in the CreateRunRequest body when creating a run.
 * According to OpenAI API docs, the assistant_id is required in the request body, not as a URL parameter.
 *
 * Fixed issue: Previously the assistant_id was defined but not used. Now it's properly passed
 * through the CreateRunRequest to ensure the correct assistant handles the conversation.
 */
class ConversationNetworkService(
    private val client: HttpClient,
    private val baseUrl: String = "https://api.openai.com/v1",
    private val apiKey: String = BuildKonfig.OPENAI_KEY, // TODO: Replace with actual API key
    private val assistantId: String = BuildKonfig.ASSISTANT_ID // TODO: Replace with actual assistant ID
) : ConversationNetworkAPI {

    override suspend fun createThread(request: CreateThreadRequest): CreateThreadResponse {
        return client.post("$baseUrl/threads") {
            contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $apiKey")
                append("OpenAI-Beta", "assistants=v2")
            }
            setBody(request)
        }.body()
    }

    override suspend fun createRunStream(
        threadId: String,
        request: CreateRunRequest
    ): Flow<RunResponse> = flow {
        println("🤖 [ConversationNetworkService] Creating run stream with assistantId: ${request.assistant_id}")
        println("🧵 [ConversationNetworkService] Thread ID: $threadId")
        println("📋 [ConversationNetworkService] Request details: messages=${request.additional_messages.size}, stream=${request.stream}")

        // Validation
        if (threadId.isBlank()) {
            throw IllegalArgumentException("Thread ID cannot be blank")
        }

        if (!threadId.startsWith("thread_")) {
            println("⚠️ [ConversationNetworkService] Warning: Thread ID doesn't start with 'thread_': $threadId")
        }

        // Ensure we're using the correct assistant ID
        val correctedRequest = request.copy(
            assistant_id = if (request.assistant_id.isBlank()) assistantId else request.assistant_id,
            stream = true
        )

        if (!correctedRequest.assistant_id.startsWith("asst_")) {
            println("⚠️ [ConversationNetworkService] Warning: Assistant ID doesn't start with 'asst_': ${correctedRequest.assistant_id}")
        }

        println("✅ [ConversationNetworkService] Using assistant ID: ${correctedRequest.assistant_id}")
        println("📤 [ConversationNetworkService] Full request body: $correctedRequest")

        val response: HttpResponse = client.post("$baseUrl/threads/$threadId/runs") {
            contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $apiKey")
                append("OpenAI-Beta", "assistants=v2")
            }
            setBody(correctedRequest)
        }

        println("📡 [ConversationNetworkService] HTTP Response Status: ${response.status}")

        if (!response.status.isSuccess()) {
            val errorBody = response.bodyAsText()
            println("❌ [ConversationNetworkService] Error response body: $errorBody")
            println("📋 [ConversationNetworkService] Response headers: ${response.headers.entries()}")
            throw Exception("OpenAI API Error (${response.status}): $errorBody")
        }

        val channel = response.bodyAsChannel()
        val buffer = StringBuilder()

        while (!channel.isClosedForRead) {
            val line = channel.readUTF8Line() ?: break

            if (line.isBlank()) continue

            when {
                line.startsWith("data: ") -> {
                    val jsonData = line.substringAfter("data: ").trim()
                    if (jsonData == "[DONE]") {
                        println("✅ [ConversationNetworkService] Stream completed")
                        break
                    }

                    try {
                        val jsonElement = Json.parseToJsonElement(jsonData)
                        val eventType = jsonElement.jsonObject["object"]?.jsonPrimitive?.content

                        when (eventType) {
                            "thread.message.delta" -> {
                                println("🔄 [ConversationNetworkService] Processing message delta")
                                val delta = jsonElement.jsonObject["delta"]?.jsonObject
                                val content = delta?.get("content")?.jsonArray

                                content?.forEach { contentItem ->
                                    if (contentItem is JsonObject) {
                                        val itemType = contentItem["type"]?.jsonPrimitive?.content
                                        if (itemType == "text") {
                                            val textObject = contentItem["text"]?.jsonObject
                                            val textValue = textObject?.get("value")?.jsonPrimitive?.content
                                            textValue?.let { text ->
                                                buffer.append(text)
                                                if (isParagraphComplete(buffer)) {
                                                    println("📝 [ConversationNetworkService] Emitting paragraph: '${buffer.toString().take(50)}...'")
                                                    emit(RunResponse(id = "", message = buffer.toString()))
                                                    buffer.clear()
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            "thread.message.completed" -> {
                                println("✅ [ConversationNetworkService] Message completed")
                                if (buffer.isNotEmpty()) {
                                    println("📝 [ConversationNetworkService] Emitting final content: '${buffer.toString().take(50)}...'")
                                    emit(RunResponse(id = "", message = buffer.toString()))
                                    buffer.clear()
                                }
                            }
                            "thread.run" -> {
                                val status = jsonElement.jsonObject["status"]?.jsonPrimitive?.content
                                println("🏃 [ConversationNetworkService] Run status: $status")
                            }
                            "thread.run.step" -> {
                                val stepType = jsonElement.jsonObject["type"]?.jsonPrimitive?.content
                                val status = jsonElement.jsonObject["status"]?.jsonPrimitive?.content
                                println("👣 [ConversationNetworkService] Run step: $stepType, status: $status")
                            }
                            "thread.message" -> {
                                val role = jsonElement.jsonObject["role"]?.jsonPrimitive?.content
                                val status = jsonElement.jsonObject["status"]?.jsonPrimitive?.content
                                println("💬 [ConversationNetworkService] Message from $role, status: $status")
                            }
                            else -> {
                                println("🔍 [ConversationNetworkService] Unhandled event type: $eventType")
                            }
                        }
                    } catch (e: Exception) {
                        println("❌ [ConversationNetworkService] Error parsing streaming response: ${e.message}")
                        // Don't print full JSON on error to avoid log spam, just log the event type if possible
                        try {
                            val eventType = Json.parseToJsonElement(jsonData).jsonObject["object"]?.jsonPrimitive?.content
                            println("🔍 [ConversationNetworkService] Failed to parse event type: $eventType")
                        } catch (ignored: Exception) {
                            // Ignore secondary parsing errors
                        }
                    }
                }
            }
        }

        // Emit any remaining content
        if (buffer.isNotEmpty()) {
            println("📝 [ConversationNetworkService] Emitting remaining content: '${buffer.toString().take(50)}...'")
            emit(RunResponse(id = "", message = buffer.toString()))
        }
    }

    override suspend fun transcribeAudio(
        filePath: String,
        model: String,
        language: String?,
        responseFormat: String?,
        temperature: Float?
    ): TranscriptionResponse {
        println("🎧 [ConversationNetworkService] Starting transcription - filePath: $filePath")
        println("🔧 [ConversationNetworkService] Model: $model, Language: $language, Format: $responseFormat, Temp: $temperature")
        
        // Validate file exists and get info
        val fileBytes = try {
            readFileBytes(filePath)
        } catch (e: Exception) {
            println("❌ [ConversationNetworkService] Failed to read audio file: ${e.message}")
            throw Exception("Failed to read audio file: ${e.message}")
        }
        
        val fileName = getFileName(filePath)
        println("📁 [ConversationNetworkService] File info - Name: $fileName, Size: ${fileBytes.size} bytes")
        
        if (fileBytes.isEmpty()) {
            println("❌ [ConversationNetworkService] Audio file is empty")
            throw Exception("Audio file is empty")
        }

        val response = try {
            client.submitFormWithBinaryData(
                url = "$baseUrl/audio/transcriptions",
                formData = formData {
                    append("file", fileBytes, Headers.build {
                        append(HttpHeaders.ContentType, "audio/mp4") // Try mp4 for better compatibility
                        append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                    })
                    append("model", model)
                    language?.let { append("language", it) }
                    responseFormat?.let { append("response_format", it) }
                    temperature?.let { append("temperature", it.toString()) }
                    
                    // Log form data being sent
                    println("📤 [ConversationNetworkService] Form data - model: $model, file: $fileName, language: $language")
                }
            ) {
                headers {
                    append("Authorization", "Bearer $apiKey")
                }
            }
        } catch (e: Exception) {
            println("❌ [ConversationNetworkService] Network error during transcription: ${e.message}")
            throw Exception("Network error during transcription: ${e.message}")
        }

        println("📡 [ConversationNetworkService] Transcription response status: ${response.status}")
        println("📋 [ConversationNetworkService] Transcription response content-type: ${response.contentType()}")
        
        if (!response.status.isSuccess()) {
            val errorBody = response.bodyAsText()
            println("❌ [ConversationNetworkService] Transcription API error response: $errorBody")
            println("📋 [ConversationNetworkService] Response headers: ${response.headers.entries()}")
            throw Exception("OpenAI Transcription API Error (${response.status}): $errorBody")
        }

        // Handle different response formats
        return try {
            when (responseFormat) {
                "json" -> {
                    // JSON response format
                    val jsonResponse = response.bodyAsText()
                    println("📝 [ConversationNetworkService] JSON response: $jsonResponse")
                    response.body<TranscriptionResponse>()
                }
                "text", null -> {
                    // Plain text response format (default)
                    val textResponse = response.bodyAsText()
                    println("✅ [ConversationNetworkService] Transcription result: '$textResponse'")
                    TranscriptionResponse(text = textResponse)
                }
                else -> {
                    // Fallback to text
                    val textResponse = response.bodyAsText()
                    println("✅ [ConversationNetworkService] Transcription result (fallback): '$textResponse'")
                    TranscriptionResponse(text = textResponse)
                }
            }
        } catch (e: Exception) {
            println("❌ [ConversationNetworkService] Failed to parse transcription response: ${e.message}")
            // Try to get raw response for debugging
            val rawResponse = response.bodyAsText()
            println("🔍 [ConversationNetworkService] Raw response: $rawResponse")
            throw Exception("Failed to parse transcription response: ${e.message}")
        }
    }

    override suspend fun generateSpeech(request: TextToSpeechRequest): ByteArray {
        println("🎤 [ConversationNetworkService] Starting TTS generation")
        println("📝 [ConversationNetworkService] TTS request: model=${request.model}, voice=${request.voice}, format=${request.response_format}, speed=${request.speed}")
        println("🎯 [ConversationNetworkService] Instructions: ${request.instructions}")
        println("📄 [ConversationNetworkService] Text to synthesize (${request.input.length} chars): '${request.input.take(100)}...'")
        
        // Manually construct JSON to avoid serialization issues
        val jsonBody = buildString {
            append("""{"model":"${request.model}","input":"${request.input.replace("\"", "\\\"")}","voice":"${request.voice}"""")
            request.response_format?.let { append(""","response_format":"$it"""") }
            request.instructions?.let { append(""","instructions":"${it.replace("\"", "\\\"")}"""") }
            request.speed?.let { append(""","speed":$it""") }
            append("}")
        }
        println("🔍 [ConversationNetworkService] Raw JSON body: $jsonBody")

        val response = client.post("$baseUrl/audio/speech") {
            contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $apiKey")
            }
            setBody(jsonBody)
        }

        println("📡 [ConversationNetworkService] TTS response status: ${response.status}")
        println("📋 [ConversationNetworkService] TTS response content-type: ${response.contentType()}")

        if (!response.status.isSuccess()) {
            val errorBody = response.bodyAsText()
            println("❌ [ConversationNetworkService] TTS API error response: $errorBody")
            throw Exception("OpenAI TTS API Error (${response.status}): $errorBody")
        }

        val audioData = response.body<ByteArray>()
        println("🔊 [ConversationNetworkService] TTS audio data received: ${audioData.size} bytes")

        if (audioData.size < 1000) {
            println("⚠️ [ConversationNetworkService] TTS audio data seems very small (${audioData.size} bytes)")
            println("🔍 [ConversationNetworkService] First 20 bytes: ${audioData.take(20).joinToString(" ") { it.toUByte().toString(16).padStart(2, '0') }}")
        }

        return audioData
    }

    private fun isParagraphComplete(buffer: StringBuilder): Boolean {
        val text = buffer.toString()
        return text.contains("\n\n") || text.contains(". ") || text.length > 200
    }
}

// Platform-specific file operations
expect fun readFileBytes(filePath: String): ByteArray
expect fun getFileName(filePath: String): String
