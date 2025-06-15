package ai.lingopulse.data.news.remote

import ai.lingopulse.data.news.remote.model.EnhancedNewsRequest
import ai.lingopulse.data.news.remote.model.EnhancedNewsResponse

interface EnhancedNewsNetworkAPI {
    suspend fun getEnhancedNews(request: EnhancedNewsRequest): EnhancedNewsResponse
}
