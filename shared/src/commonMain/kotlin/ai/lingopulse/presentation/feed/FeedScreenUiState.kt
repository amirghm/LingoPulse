package ai.lingopulse.presentation.feed

import ai.lingopulse.presentation.common.feedDecorator
import ai.lingopulse.presentation.feed.mapper.toUiFeeds
import ai.lingopulse.presentation.common.model.UiCategory
import ai.lingopulse.presentation.feed.model.UiFeed

sealed class FeedScreenUiState {
    data class Error(val message: String? = null) : FeedScreenUiState()

    data class Loading(
        val categories: List<UiCategory>,
        val feeds: List<UiFeed>? = feedDecorator.toUiFeeds(emptyList()),
    ) : FeedScreenUiState()

    data class FeedLoaded(
        val categories: List<UiCategory>,
        val feeds: List<UiFeed> = emptyList(),
    ) : FeedScreenUiState()
}