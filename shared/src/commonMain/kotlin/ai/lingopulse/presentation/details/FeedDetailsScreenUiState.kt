package ai.lingopulse.presentation.details

import ai.lingopulse.presentation.common.feedDecorator
import ai.lingopulse.presentation.feed.mapper.toUiFeeds
import ai.lingopulse.presentation.common.model.UiCategory
import ai.lingopulse.presentation.common.newsDetails
import ai.lingopulse.presentation.details.mapper.toUiFeedDetails
import ai.lingopulse.presentation.details.model.UiFeedDetails
import ai.lingopulse.presentation.feed.model.UiFeed

sealed class FeedDetailsScreenUiState {
    data class Error(val message: String? = null) : FeedDetailsScreenUiState()

    data class Loading(
        val feedDetails: UiFeedDetails = newsDetails.toUiFeedDetails()
    ) : FeedDetailsScreenUiState()

    data class FeedDetailsLoaded(
        val feedDetails: UiFeedDetails
    ) : FeedDetailsScreenUiState()
}