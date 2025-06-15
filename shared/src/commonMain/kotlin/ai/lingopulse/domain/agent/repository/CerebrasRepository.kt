package ai.lingopulse.domain.agent.repository

import ai.lingopulse.data.agent.remote.cerebras.model.CerebrasMessage
import ai.lingopulse.data.agent.remote.cerebras.model.CebrasChatCompletionResponse

interface CerebrasRepository {
    suspend fun getChatCompletion(
        messages: List<CerebrasMessage>,
        model: String = "llama3.1-8b",
        maxTokens: Int = 8192,
        temperature: Double = 0.7
    ): CebrasChatCompletionResponse
}