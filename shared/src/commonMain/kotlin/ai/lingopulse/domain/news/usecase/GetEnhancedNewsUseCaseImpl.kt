package ai.lingopulse.domain.news.usecase

import ai.lingopulse.domain.news.model.EnhancedNewsRequest
import ai.lingopulse.domain.news.model.EnhancedNewsResult
import ai.lingopulse.domain.news.repository.EnhancedNewsRepository

class GetEnhancedNewsUseCaseImpl(
    private val enhancedNewsRepository: EnhancedNewsRepository
) : GetEnhancedNewsUseCase {

    override suspend fun invoke(request: EnhancedNewsRequest): EnhancedNewsResult {
        return enhancedNewsRepository.getEnhancedNews(request)
    }
}
