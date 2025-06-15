package ai.lingopulse.domain.conversation.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConversationMessage(
    val role: String,
    val content: List<MessageContent>
)

@Serializable
data class MessageContent(
    val type: String,
    val text: String? = null,
    @SerialName("image_file")
    val imageFile: ImageFileData? = null,
    @SerialName("image_url")
    val imageUrl: ImageUrlData? = null
) {
    companion object {
        fun text(text: String) = MessageContent(
            type = "text",
            text = text
        )
        
        fun imageFile(fileId: String) = MessageContent(
            type = "image_file",
            imageFile = ImageFileData(fileId)
        )
        
        fun imageUrl(url: String) = MessageContent(
            type = "image_url",
            imageUrl = ImageUrlData(url)
        )
    }
}

@Serializable
data class ImageFileData(
    @SerialName("file_id")
    val fileId: String
)

@Serializable
data class ImageUrlData(
    val url: String
)

@Serializable
data class CreateThreadRequest(
    val messages: List<ConversationMessage>? = null
)

@Serializable
data class CreateRunRequest(
    val assistant_id: String,
    val additional_instructions: String? = null,
    val additional_messages: List<ConversationMessage>,
    val max_prompt_tokens: Int = 8192,
    val max_completion_tokens: Int = 4096,
    val stream: Boolean = false
)

@Serializable
data class TextToSpeechRequest(
    val model: String = "gpt-4o-mini-tts",
    val input: String,
    val voice: String = "nova",
    val response_format: String? = "mp3",
    val instructions: String? = "Speak in a cheerful and positive tone.",
    val speed: Double? = 1.0
)
