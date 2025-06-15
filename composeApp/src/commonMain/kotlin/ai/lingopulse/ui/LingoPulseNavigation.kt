package ai.lingopulse.ui

import ai.lingopulse.domain.common.model.FeedDetails
import ai.lingopulse.domain.system.StorageRepository
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ai.lingopulse.ui.feature.feed.FeedRoute
import ai.lingopulse.presentation.feed.FeedViewModel
import ai.lingopulse.presentation.setting.SettingViewModel
import ai.lingopulse.presentation.details.FeedDetailsViewModel
import ai.lingopulse.ui.feature.setting.SettingRoute
import ai.lingopulse.ui.feature.details.FeedDetailsRoute
import ai.lingopulse.ui.feature.conversation.ConversationRoute
import ai.lingopulse.presentation.conversation.ConversationViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.koin.compose.getKoin

sealed class Screen() {
    @Serializable
    data object Feed : Screen()

    @Serializable
    data object Setting : Screen()

    @Serializable
    data class FeedDetails(
        val id: String,
        val category: String,
        val title: String,
        val subtitle: String,
        val content: String,
        val author: String,
        val url: String,
        val imageUrl: String,
        val publishedAt: String
    ) : Screen()

    @Serializable
    data class Conversation(
        val id: String,
        val category: String,
        val title: String,
        val subtitle: String,
        val content: String,
        val author: String,
        val url: String,
        val imageUrl: String,
        val publishedAt: String
    ) : Screen()
}

@Composable
fun LingoPulseNavigation(navController: NavHostController = rememberNavController()) {
    val storageRepository = getKoin().get<StorageRepository>()
    val settingSet = storageRepository.getUserCategories().isEmpty()
    NavHost(
        navController = navController,
        startDestination = if (settingSet) Screen.Setting else Screen.Feed
    ) {
        composable<Screen.Setting> { navBackStackEntry ->
            val viewModel: SettingViewModel = getKoin().get()
            SettingRoute(
                viewModel,
                navController,
            )
        }
        composable<Screen.Feed> { navBackStackEntry ->
            val viewModel: FeedViewModel = getKoin().get()
            FeedRoute(
                viewModel,
                navController,
            )
        }
        composable<Screen.FeedDetails> { navBackStackEntry ->
            val viewModel: FeedDetailsViewModel = getKoin().get()
            val data = navBackStackEntry.toRoute<Screen.FeedDetails>()
            LaunchedEffect(viewModel) {
                viewModel.setup(
                    FeedDetails(
                        id = data.id,
                        category = data.category,
                        title = data.title,
                        subtitle = data.subtitle,
                        content = data.content,
                        author = data.author,
                        url = data.url,
                        imageUrl = data.imageUrl,
                        publishedAt = data.publishedAt
                    )
                )
            }

            FeedDetailsRoute(
                viewModel,
                navController
            )
        }
        composable<Screen.Conversation> { navBackStackEntry ->
            val viewModel: ConversationViewModel = getKoin().get()
            val data = navBackStackEntry.toRoute<Screen.Conversation>()
            LaunchedEffect(viewModel) {
                viewModel.setup(
                    FeedDetails(
                        id = data.id,
                        category = data.category,
                        title = data.title,
                        subtitle = data.subtitle,
                        content = data.content,
                        author = data.author,
                        url = data.url,
                        imageUrl = data.imageUrl,
                        publishedAt = data.publishedAt
                    )
                )
            }

            ConversationRoute(
                viewModel,
                navController
            )
        }
    }
}
