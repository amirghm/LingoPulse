package ai.lingopulse.presentation.feed

import ai.lingopulse.presentation.feed.model.UiFeed

sealed class FeedScreenNavigationInteraction {
    data class OpenNewsDetails(val feed: UiFeed) : FeedScreenNavigationInteraction()
    object OpenSettings : FeedScreenNavigationInteraction()
}