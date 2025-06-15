package ai.lingopulse.domain.news.usecase

import ai.lingopulse.domain.news.model.EnhancedNewsRequest
import ai.lingopulse.domain.news.model.EnhancedNewsResult

interface GetEnhancedNewsUseCase {
    suspend fun invoke(request: EnhancedNewsRequest): EnhancedNewsResult
}
