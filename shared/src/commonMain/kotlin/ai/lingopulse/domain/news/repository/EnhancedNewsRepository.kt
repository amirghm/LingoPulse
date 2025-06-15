package ai.lingopulse.domain.news.repository

import ai.lingopulse.domain.news.model.EnhancedNewsRequest
import ai.lingopulse.domain.news.model.EnhancedNewsResult

interface EnhancedNewsRepository {
    suspend fun getEnhancedNews(request: EnhancedNewsRequest): EnhancedNewsResult
}
