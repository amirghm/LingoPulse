@file:OptIn(ExperimentalTime::class)

package ai.lingopulse.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ai.lingopulse.domain.agent.usecase.GetFeedsUseCase
import ai.lingopulse.domain.common.model.Category
import ai.lingopulse.domain.common.model.Feed
import ai.lingopulse.domain.system.StorageRepository
import ai.lingopulse.presentation.common.mapper.toUiCategories
import ai.lingopulse.presentation.feed.FeedScreenNavigationInteraction.OpenNewsDetails
import ai.lingopulse.presentation.feed.mapper.toUiFeeds
import ai.lingopulse.presentation.common.model.UiCategory
import ai.lingopulse.presentation.feed.model.UiFeed
import ai.lingopulse.shared.CATEGORIES
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

class FeedViewModel(
    private val getFeedsUseCase: GetFeedsUseCase,
    storageRepository: StorageRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    var selectedCategories: List<String> = emptyList()
    var availableCategories: List<String> = storageRepository.getUserCategories()
    private var feeds: List<Feed>? = null
    val categories: List<Category>
        get() = CATEGORIES.filter { it.key in availableCategories }.map {
            Category(
                id = it.key,
                category = it.value
            )
        }

    var filteredFeeds: List<Feed> = emptyList()

    var resolvedStrings: Map<String, String> = emptyMap()

    private val _uiState: MutableStateFlow<FeedScreenUiState> =
        MutableStateFlow(FeedScreenUiState.Loading(categories = categories.toUiCategories()))
    val uiState: StateFlow<FeedScreenUiState> =
        _uiState.asStateFlow()

    private val _navigationInteraction = MutableSharedFlow<FeedScreenNavigationInteraction>()
    val navigationInteraction: SharedFlow<FeedScreenNavigationInteraction> = _navigationInteraction

    var initialized = false
    fun setup(stringsData: Map<String, String> = emptyMap()) {
        resolvedStrings = stringsData
        if (initialized) return
        initialized = true
        viewModelScope.launch(dispatcher) {

            getFeedsUseCase(categories = categories.map { it.id })
                .catch {
                    _uiState.value = FeedScreenUiState.Loading(
                        categories = categories.toUiCategories(),
                    )
                }.onStart {
                    _uiState.value = FeedScreenUiState.Loading(
                        categories = categories.toUiCategories(),
                    )
                }.collect {
                    feeds = it
                    filteredFeeds = if(selectedCategories.isNotEmpty()) {
                        feeds?.filter { it.category in selectedCategories } ?: emptyList()
                    } else {
                        feeds ?: emptyList()
                    }

                    _uiState.value = FeedScreenUiState.FeedLoaded(
                        categories = categories.toUiCategories(),
                        feeds = filteredFeeds.toUiFeeds(categories),
                    )
                }
        }
    }

    fun onCategoryClicked(category: UiCategory) {
        viewModelScope.launch(dispatcher) {

            selectedCategories = selectedCategories.toMutableList().apply {
                if (contains(category.id)) {
                    remove(category.id)
                } else {
                    add(category.id)
                }
            }

            filteredFeeds = if (selectedCategories.isNotEmpty()) {
                feeds?.filter { it.category == category.id } ?: emptyList()
            } else {
                feeds ?: emptyList()
            }

            _uiState.value = FeedScreenUiState.FeedLoaded(
                categories = categories.toUiCategories(selectedCategories),
                feeds = filteredFeeds.toUiFeeds(categories),
            )
        }
    }

    fun onSettingsClicked() {
        viewModelScope.launch {
            _navigationInteraction.emit(FeedScreenNavigationInteraction.OpenSettings)
        }
    }

    fun onNewsClicked(feed: UiFeed) {
        viewModelScope.launch {
            _navigationInteraction.emit(OpenNewsDetails(feed))
        }
    }

    fun resolvedString(stringResource: String): String {
        return resolvedStrings[stringResource] ?: ""
    }

}