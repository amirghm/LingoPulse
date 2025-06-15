package ai.lingopulse.data.agent.remote.cerebras.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CerebrasMessage(
    @SerialName("content") val content: String,
    @SerialName("role") val role: String
)