package ai.lingopulse.data.agent.remote.cerebras

import ai.lingopulse.data.agent.remote.cerebras.model.CerebrasChatCompletionRequest
import ai.lingopulse.data.agent.remote.cerebras.model.CebrasChatCompletionResponse

interface CerebrasNetworkAPI {
    suspend fun getChatCompletion(request: CerebrasChatCompletionRequest): CebrasChatCompletionResponse
}