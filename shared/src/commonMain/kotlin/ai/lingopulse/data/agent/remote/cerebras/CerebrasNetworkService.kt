package ai.lingopulse.data.agent.remote.cerebras

import ai.lingopulse.data.agent.remote.cerebras.model.CerebrasChatCompletionRequest
import ai.lingopulse.data.agent.remote.cerebras.model.CebrasChatCompletionResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class CerebrasNetworkService(
    private val client: HttpClient,
    private val baseUrl: String = "http://localhost:8085" // Default to your Lingo Pulse server
) : CerebrasNetworkAPI {

    override suspend fun getChatCompletion(request: CerebrasChatCompletionRequest): CebrasChatCompletionResponse {
        return client.post {
            url("$baseUrl/ask")
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}