package ai.lingopulse.presentation.setting

import ai.lingopulse.presentation.common.model.UiCategory
import ai.lingopulse.presentation.setting.model.UiLanguage
import ai.lingopulse.presentation.setting.model.UiLanguageLevel

sealed class SettingScreenUiState {
    data class ScreenLoaded(
        val categories: List<UiCategory>,
        val languages: List<UiLanguage>,
        val languageLevels: List<UiLanguageLevel>
    ) : SettingScreenUiState()
}