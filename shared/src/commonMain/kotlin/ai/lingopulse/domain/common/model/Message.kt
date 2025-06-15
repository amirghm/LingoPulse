package ai.lingopulse.domain.common.model

import kotlinx.serialization.Serializable
import kotlin.time.Clock.System
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Serializable
data class Message(
    val id: Long? = null,
    val conversationId: Long,
    val role: Role,
    val contents: List<Content>,
    val timestamp: Long = System.now().toEpochMilliseconds()
) {
    val textContent: String
        get() = contents.filterIsInstance<Content.Text>().joinToString("\n\n") {
            it.text
        }

    val nonTextContent: Content.ImageUrl?
        get() = contents.filterIsInstance<Content.ImageUrl>().firstOrNull()

}

fun Message.withUpdatedContent(): Message {
    return this.copy(
        contents = this.contents.map { content ->
            when (content) {
                is Content.Text -> Content.Text(content.text.replace("\\\"", "\""))
                else -> content // Keep other types unchanged
            }
        }
    )
}