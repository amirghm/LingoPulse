package ai.lingopulse.ui.feature.feed

import ai.lingopulse.ui.core.common.model.ScreenEvent
import ai.lingopulse.ui.core.common.model.ScreenEventHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import ai.lingopulse.presentation.feed.FeedScreenNavigationInteraction
import ai.lingopulse.presentation.feed.FeedViewModel
import ai.lingopulse.shared.Platform
import ai.lingopulse.ui.Screen
import ai.lingopulse.util.resolveStrings
import lingopulse.composeapp.generated.resources.Res
import lingopulse.composeapp.generated.resources.app_name
import lingopulse.composeapp.generated.resources.new_conversation
import lingopulse.composeapp.generated.resources.opps_an_error_occurred_while_checking_your_message
import lingopulse.composeapp.generated.resources.unknown

var resolvedStrings: Map<String, String> = emptyMap()
@Composable
internal fun FeedRoute(
    viewModel: FeedViewModel,
    navController: NavController,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit) {
        resolvedStrings = resolveStrings(
            Res.string.app_name,
            Res.string.unknown,
            Res.string.new_conversation,
            Res.string.opps_an_error_occurred_while_checking_your_message,
        )
        viewModel.setup(resolvedStrings)
    }

    val uiState by viewModel.uiState.collectAsState()
    val eventHandler: ScreenEventHandler = object : ScreenEventHandler {
        override fun onEvent(event: ScreenEvent) {
            when (event) {
                is FeedScreenEvent.OnNewsClicked -> {
                    viewModel.onNewsClicked(
                        feed = event.feed
                    )
                }

                is FeedScreenEvent.OnCategoryClicked -> {
                    viewModel.onCategoryClicked(event.category)
                }

                is FeedScreenEvent.OnSettingsClicked -> {
                    viewModel.onSettingsClicked()
                }
            }
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.navigationInteraction.collect {
            when (it) {
                is FeedScreenNavigationInteraction.OpenNewsDetails -> {
                    navController.navigate(
                        Screen.FeedDetails(
                            id = it.feed.id,
                            category = it.feed.category.id,
                            title = it.feed.title,
                            subtitle = it.feed.subtitle,
                            content = it.feed.content,
                            author = it.feed.author,
                            url = it.feed.url,
                            imageUrl = it.feed.imageUrl,
                            publishedAt = it.feed.publishedAt
                        )
                    )
                }
                FeedScreenNavigationInteraction.OpenSettings -> {
                    navController.navigate(Screen.Setting)
                }
            }
        }
    }

    FeedScreen(uiState, eventHandler)
}
