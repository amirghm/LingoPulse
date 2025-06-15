package ai.lingopulse.data.agent.remote.cerebras.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CerebrasChatCompletionRequest(
    @SerialName("messages") val messages: List<CerebrasMessage>,
    @SerialName("model") val model: String = "llama3.1-8b",
    @SerialName("provider") val provider: String = "cerebras",
    @SerialName("max_tokens") val maxTokens: Int = 8192,
    @SerialName("temperature") val temperature: Double = 0.7,
    @SerialName("stream") val stream: Boolean = false
)