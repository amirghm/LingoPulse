package ai.lingopulse.domain.common.model

import kotlinx.serialization.Serializable

@Serializable
enum class Role {
    SYSTEM,
    USER,
    AI,
    FUNCTION,
    TOOL;

    fun mappedRole(): String {
        return when (this) {
            SYSTEM -> "assistant"
            USER -> "user"
            AI -> "assistant"
            FUNCTION -> "function"
            TOOL -> "tool"
        }
    }
}
