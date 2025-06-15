package ai.lingopulse.data.news.repository

import ai.lingopulse.data.news.remote.EnhancedNewsNetworkAPI
import ai.lingopulse.domain.news.model.EnhancedNewsRequest
import ai.lingopulse.domain.news.model.EnhancedNewsResult
import ai.lingopulse.domain.news.repository.EnhancedNewsRepository

class EnhancedNewsRepositoryImpl(
    private val enhancedNewsNetworkApi: EnhancedNewsNetworkAPI
) : EnhancedNewsRepository {

    override suspend fun getEnhancedNews(request: EnhancedNewsRequest): EnhancedNewsResult {
        val dataRequest = ai.lingopulse.data.news.remote.model.EnhancedNewsRequest(
            article = request.article,
            targetLanguage = request.targetLanguage,
            languageLevel = request.languageLevel
        )
        
        return try {
            val response = enhancedNewsNetworkApi.getEnhancedNews(dataRequest)
            EnhancedNewsResult(
                success = true,
                enhancedContent = response.output
            )
        } catch (e: Exception) {
            EnhancedNewsResult(
                success = false,
                error = e.message ?: "Unknown error occurred"
            )
        }
    }
}
