@file:OptIn(ExperimentalTime::class)

package ai.lingopulse.presentation.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ai.lingopulse.domain.common.model.Category
import ai.lingopulse.domain.common.model.Language
import ai.lingopulse.domain.common.model.LanguageLevel
import ai.lingopulse.domain.system.StorageRepository
import ai.lingopulse.presentation.common.mapper.toUiCategories
import ai.lingopulse.presentation.common.mapper.toUiLanguageLevels
import ai.lingopulse.presentation.common.mapper.toUiLanguages
import ai.lingopulse.presentation.common.model.UiCategory
import ai.lingopulse.presentation.setting.model.UiLanguage
import ai.lingopulse.presentation.setting.model.UiLanguageLevel
import ai.lingopulse.shared.CATEGORIES
import ai.lingopulse.shared.LANGUAGE_LEVELS
import ai.lingopulse.shared.SUPPORTING_LANGUAGES
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

class SettingViewModel(
    private val storageRepository: StorageRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    var selectedCategories: List<String> = storageRepository.getUserCategories()
    var selectedLanguageLevel: String = storageRepository.getUserLanguageLevel()
    var selectedLanguage: String = storageRepository.getUserLearningLanguage()

    var availableCategories: Map<String, String> = CATEGORIES
    var availableLanguageLevels: Map<String, String> = LANGUAGE_LEVELS
    var availableLanguageToLearn: Map<String, String> = SUPPORTING_LANGUAGES

    val categories: List<Category>
        get() = availableCategories.map {
            Category(
                id = it.key,
                category = it.value
            )
        }

    val languageLevels: List<LanguageLevel>
        get() = availableLanguageLevels.map {
            LanguageLevel(
                id = it.key,
                level = it.value
            )
        }

    val languages: List<Language>
        get() = availableLanguageToLearn.map {
            Language(
                id = it.key,
                name = it.value
            )
        }

    private val _uiState: MutableStateFlow<SettingScreenUiState> =
        MutableStateFlow(
            SettingScreenUiState.ScreenLoaded(
                categories = categories.toUiCategories(selectedCategories),
                languages = languages.toUiLanguages(selectedLanguage),
                languageLevels = languageLevels.toUiLanguageLevels(selectedLanguageLevel)
            )
        )
    val uiState: StateFlow<SettingScreenUiState> =
        _uiState.asStateFlow()

    private val _navigationInteraction = MutableSharedFlow<SettingScreenNavigationInteraction>()
    val navigationInteraction: SharedFlow<SettingScreenNavigationInteraction> =
        _navigationInteraction

    fun onCategoryClicked(category: UiCategory) {
        viewModelScope.launch(dispatcher) {

            selectedCategories = selectedCategories.toMutableList().apply {
                if (contains(category.id)) {
                    remove(category.id)
                } else {
                    add(category.id)
                }
            }

            storageRepository.setUserCategories(selectedCategories)

            _uiState.value = SettingScreenUiState.ScreenLoaded(
                categories = categories.toUiCategories(selectedCategories),
                languages = languages.toUiLanguages(selectedLanguage ?: ""),
                languageLevels = languageLevels.toUiLanguageLevels(selectedLanguageLevel ?: "")
            )
        }
    }

    fun onLanguageLevelClicked(languageLevel: UiLanguageLevel) {
        viewModelScope.launch(dispatcher) {

            selectedLanguageLevel = if (selectedLanguageLevel == languageLevel.id) "" else languageLevel.id

            storageRepository.setUserLanguageLevel(selectedLanguageLevel ?: "")

            _uiState.value = SettingScreenUiState.ScreenLoaded(
                categories = categories.toUiCategories(selectedCategories),
                languages = languages.toUiLanguages(selectedLanguage ?: ""),
                languageLevels = languageLevels.toUiLanguageLevels(selectedLanguageLevel ?: "")
            )
        }
    }

    fun onLanguageClicked(language: UiLanguage) {
        viewModelScope.launch(dispatcher) {

            selectedLanguage = if (selectedLanguage == language.id) "" else language.id

            storageRepository.setUserLearningLanguage(selectedLanguage)

            _uiState.value = SettingScreenUiState.ScreenLoaded(
                categories = categories.toUiCategories(selectedCategories),
                languages = languages.toUiLanguages(selectedLanguage),
                languageLevels = languageLevels.toUiLanguageLevels(selectedLanguageLevel)
            )
        }
    }

    fun onConfirmClicked() {
        viewModelScope.launch {
            _navigationInteraction.emit(SettingScreenNavigationInteraction.NavigateToFeeds)
        }
    }
}