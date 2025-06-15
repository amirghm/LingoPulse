package ai.lingopulse.ui.feature.setting

import ai.lingopulse.presentation.setting.SettingScreenNavigationInteraction
import ai.lingopulse.presentation.setting.SettingViewModel
import ai.lingopulse.ui.Screen
import ai.lingopulse.ui.core.common.model.ScreenEvent
import ai.lingopulse.ui.core.common.model.ScreenEventHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController

@Composable
internal fun SettingRoute(
    viewModel: SettingViewModel,
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsState()
    val eventHandler: ScreenEventHandler = object : ScreenEventHandler {
        override fun onEvent(event: ScreenEvent) {
            when (event) {
                is SettingScreenEvent.OnCategoryClicked -> {
                    viewModel.onCategoryClicked(
                        category = event.category
                    )
                }
                is SettingScreenEvent.OnLanguageLevelClicked -> {
                    viewModel.onLanguageLevelClicked(
                        languageLevel = event.languageLevel
                    )
                }
                is SettingScreenEvent.OnLanguageClicked -> {
                    viewModel.onLanguageClicked(
                        language = event.language
                    )
                }
                is SettingScreenEvent.OnConfirmClicked -> {
                    viewModel.onConfirmClicked()
                }
            }
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.navigationInteraction.collect {
            when (it) {
                is SettingScreenNavigationInteraction.NavigateToFeeds -> {
                    navController.navigate(Screen.Feed)
                }
            }
        }
    }

    SettingScreen(uiState, eventHandler)
}
