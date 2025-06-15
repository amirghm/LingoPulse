package ai.lingopulse.domain.conversation.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateThreadResponse(
    val id: String,
    val `object`: String,
    val created_at: Long
)

@Serializable
data class CreateRunResponse(
    val id: String,
    val `object`: String,
    val thread_id: String,
    val status: String
)

@Serializable
data class RunResponse(
    val id: String,
    val message: String
)

@Serializable
data class TranscriptionResponse(
    val text: String
)

@Serializable
data class StreamEvent(
    val `object`: String,
    val delta: Delta? = null,
    val content: List<ContentItem>? = null
)

@Serializable
data class Delta(
    val content: List<ContentItem>? = null
)

@Serializable
data class ContentItem(
    val type: String,
    val text: TextValue? = null
)

@Serializable
data class TextValue(
    val value: String
)