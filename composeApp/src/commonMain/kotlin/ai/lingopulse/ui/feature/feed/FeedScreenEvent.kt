package ai.lingopulse.ui.feature.feed

import ai.lingopulse.presentation.common.model.UiCategory
import ai.lingopulse.presentation.feed.model.UiFeed
import ai.lingopulse.ui.core.common.model.ScreenEvent

sealed class FeedScreenEvent: ScreenEvent() {
    data class OnCategoryClicked(val category: UiCategory) : FeedScreenEvent()
    data class OnNewsClicked(val feed: UiFeed) : FeedScreenEvent()
    object OnSettingsClicked : FeedScreenEvent()
}