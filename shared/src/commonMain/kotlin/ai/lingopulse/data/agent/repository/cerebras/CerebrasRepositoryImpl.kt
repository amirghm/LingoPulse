package ai.lingopulse.data.agent.repository.cerebras

import ai.lingopulse.data.agent.remote.cerebras.CerebrasNetworkAPI
import ai.lingopulse.data.agent.remote.cerebras.model.CerebrasChatCompletionRequest
import ai.lingopulse.data.agent.remote.cerebras.model.CebrasChatCompletionResponse
import ai.lingopulse.data.agent.remote.cerebras.model.CerebrasMessage
import ai.lingopulse.domain.agent.repository.CerebrasRepository

class CerebrasRepositoryImpl(
    private val cerebrasNetworkApi: CerebrasNetworkAPI
) : CerebrasRepository {

    override suspend fun getChatCompletion(
        messages: List<CerebrasMessage>,
        model: String,
        maxTokens: Int,
        temperature: Double
    ): CebrasChatCompletionResponse {
        val request = CerebrasChatCompletionRequest(
            messages = messages,
            model = model,
            provider = "cerebras",
            maxTokens = maxTokens,
            temperature = temperature,
            stream = false
        )
        
        return cerebrasNetworkApi.getChatCompletion(request)
    }
}