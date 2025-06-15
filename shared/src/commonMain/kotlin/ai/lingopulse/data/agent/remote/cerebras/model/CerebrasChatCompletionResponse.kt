package ai.lingopulse.data.agent.remote.cerebras.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CebrasChatCompletionResponse(
    @SerialName("id") val id: String,
    @SerialName("object") val objectType: String,
    @SerialName("created") val created: Long,
    @SerialName("model") val model: String,
    @SerialName("choices") val choices: List<CebrasChoice>,
    @SerialName("usage") val usage: CebrasUsage? = null
)

@Serializable
data class CebrasChoice(
    @SerialName("index") val index: Int,
    @SerialName("message") val message: CerebrasMessage,
    @SerialName("finish_reason") val finishReason: String? = null
)

@Serializable
data class CebrasUsage(
    @SerialName("prompt_tokens") val promptTokens: Int,
    @SerialName("completion_tokens") val completionTokens: Int,
    @SerialName("total_tokens") val totalTokens: Int
)