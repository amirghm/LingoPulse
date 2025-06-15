package ai.lingopulse.data.news.remote

import ai.lingopulse.data.news.remote.model.EnhancedNewsRequest
import ai.lingopulse.data.news.remote.model.EnhancedNewsResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class EnhancedNewsNetworkService(
    private val client: HttpClient,
    private val baseUrl: String = "https://lingua.app.n8n.cloud",
    private val authToken: String = "OO@%w^Cu*3!hAj"
) : EnhancedNewsNetworkAPI {

    override suspend fun getEnhancedNews(request: EnhancedNewsRequest): EnhancedNewsResponse {
        return client.post {
            url("$baseUrl/webhook/79b02647-40c9-4d09-970c-bde18e8ff9ab")
            contentType(ContentType.Application.Json)
            header("Authtoken", authToken)
            setBody(request)
        }.body()
    }
}
