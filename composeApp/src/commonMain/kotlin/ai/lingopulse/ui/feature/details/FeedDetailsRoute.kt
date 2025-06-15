package ai.lingopulse.ui.feature.details

import ai.lingopulse.presentation.details.FeedDetailsScreenNavigationInteraction
import ai.lingopulse.presentation.details.FeedDetailsViewModel
import ai.lingopulse.ui.core.common.model.ScreenEvent
import ai.lingopulse.ui.core.common.model.ScreenEventHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import ai.lingopulse.presentation.feed.FeedScreenNavigationInteraction
import ai.lingopulse.shared.Platform
import ai.lingopulse.ui.Screen
import ai.lingopulse.ui.feature.feed.FeedScreen


@Composable
internal fun FeedDetailsRoute(
    viewModel: FeedDetailsViewModel,
    navController: NavController,
) {

    val uiState by viewModel.uiState.collectAsState()
    val eventHandler: ScreenEventHandler = object : ScreenEventHandler {
        override fun onEvent(event: ScreenEvent) {
            when (event) {
                is FeedDetailsScreenEvent.OnBackClicked -> {
                    viewModel.onBackClicked()
                }

                is FeedDetailsScreenEvent.OnStartConversationClicked -> {
                    viewModel.onStartConversationClicked(event.feedDetails)
                }
            }
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.navigationInteraction.collect {
            when (it) {
                is FeedDetailsScreenNavigationInteraction.NavigateUp -> {
                    navController.navigateUp()
                }

                is FeedDetailsScreenNavigationInteraction.OpenConversation -> {
                    navController.navigate(
                        Screen.Conversation(
                            id = it.feedDetails.id,
                            category = it.feedDetails.category,
                            title = it.feedDetails.title,
                            subtitle = it.feedDetails.subtitle,
                            content = it.feedDetails.content,
                            author = it.feedDetails.author,
                            url = it.feedDetails.url,
                            imageUrl = it.feedDetails.imageUrl,
                            publishedAt = it.feedDetails.publishedAt
                        )
                    )
                }
            }
        }
    }

    FeedDetailsScreen(uiState, eventHandler)
}
