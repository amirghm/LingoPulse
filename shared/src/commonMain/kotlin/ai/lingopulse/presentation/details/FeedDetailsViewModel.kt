@file:OptIn(ExperimentalTime::class)

package ai.lingopulse.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ai.lingopulse.domain.agent.usecase.GetFeedsUseCase
import ai.lingopulse.domain.common.model.Feed
import ai.lingopulse.domain.common.model.FeedDetails
import ai.lingopulse.domain.news.mapper.toLanguageDisplayName
import ai.lingopulse.domain.news.mapper.toLanguageLevelDisplayName
import ai.lingopulse.domain.news.model.EnhancedNewsRequest
import ai.lingopulse.domain.news.usecase.GetEnhancedNewsUseCase
import ai.lingopulse.domain.system.StorageRepository
import ai.lingopulse.presentation.details.mapper.toUiFeedDetails
import ai.lingopulse.presentation.details.model.UiFeedDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

class FeedDetailsViewModel(
    private val getFeedsUseCase: GetFeedsUseCase,
    private val getEnhancedNewsUseCase: GetEnhancedNewsUseCase,
    private val storageRepository: StorageRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    // get feed details
    // send to get adjusted version
    // show the details

    private var feedDetails: FeedDetails? = null

    private val _uiState: MutableStateFlow<FeedDetailsScreenUiState> =
        MutableStateFlow(FeedDetailsScreenUiState.Loading())
    val uiState: StateFlow<FeedDetailsScreenUiState> =
        _uiState.asStateFlow()

    private val _navigationInteraction = MutableSharedFlow<FeedDetailsScreenNavigationInteraction>()
    val navigationInteraction: SharedFlow<FeedDetailsScreenNavigationInteraction> =
        _navigationInteraction

    fun setup(feedDetails: FeedDetails) {

        this.feedDetails = feedDetails
        _uiState.value = FeedDetailsScreenUiState.Loading()

        viewModelScope.launch(dispatcher) {

            try {
                // Get user preferences
                val userLanguageId = storageRepository.getUserLearningLanguage()
                val userLanguageLevelId = storageRepository.getUserLanguageLevel()

                // Validate that user has set preferences
                if (userLanguageId.isEmpty() || userLanguageLevelId.isEmpty()) {
                    println("User preferences not set. Language: '$userLanguageId', Level: '$userLanguageLevelId'")
                    // You might want to navigate to settings or show a dialog
                    return@launch
                }

                // Convert IDs to display names for API
                val targetLanguage = userLanguageId.toLanguageDisplayName()
                val languageLevel = userLanguageLevelId.toLanguageLevelDisplayName()

                println("Starting enhanced news processing with language: '$targetLanguage', level: '$languageLevel'")

                // Create enhanced news request
                val enhancedNewsRequest = EnhancedNewsRequest(
                    article = feedDetails.content,
                    targetLanguage = targetLanguage,
                    languageLevel = languageLevel
                )

                // Get enhanced news
                val result = getEnhancedNewsUseCase.invoke(enhancedNewsRequest)

                println("Enhanced news result: success=${result.success}, error=${result.error}")

                if (result.success) {
                    // Use enhanced content if available, otherwise use original content
                    val contentToUse = result.enhancedContent ?: feedDetails.content
                    
                    // Navigate to conversation with the result
                    val transferringFeedDetails = FeedDetails(
                        id = feedDetails.id,
                        category = feedDetails.category,
                        title = feedDetails.title,
                        subtitle = feedDetails.subtitle,
                        content = contentToUse,
                        author = feedDetails.author,
                        url = feedDetails.url,
                        imageUrl = feedDetails.imageUrl,
                        publishedAt = feedDetails.publishedAt
                    )
                    _uiState.value = FeedDetailsScreenUiState.FeedDetailsLoaded(
                        feedDetails = transferringFeedDetails.toUiFeedDetails()
                    )
                } else {
                    // Handle error - you might want to show a snackbar or error dialog
                    println("Failed to get enhanced news: ${result.error}")
                }
            } catch (e: Exception) {
                // Handle exception
                println("Error getting enhanced news: ${e.message}")
                e.printStackTrace()
            }

//            getFeedsUseCase(categories = categories.map { it.id })
//                .catch {
//                    _uiState.value = FeedDetailsScreenUiState.Loading(
//                        categories = categories.toUiCategories(),
//                    )
//                }.onStart {
//                    _uiState.value = FeedDetailsScreenUiState.Loading(
//                        categories = categories.toUiCategories(),
//                    )
//                }.collect {
//                    feedDetails = it
//                    filteredFeeds = if (selectedCategories.isNotEmpty()) {
//                        feedDetails?.filter { it.category in selectedCategories } ?: emptyList()
//                    } else {
//                        feedDetails ?: emptyList()
//                    }
//
//                    _uiState.value = FeedDetailsScreenUiState.FeedLoaded(
//                        categories = categories.toUiCategories(),
//                        feeds = filteredFeeds.toUiFeeds(categories),
//                    )
//                }
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationInteraction.emit(FeedDetailsScreenNavigationInteraction.NavigateUp)
        }
    }

    fun onStartConversationClicked(feedDetails: UiFeedDetails) {
        viewModelScope.launch(dispatcher) {
            // Convert UiFeedDetails back to FeedDetails for navigation
            val feedDetailsForNavigation = FeedDetails(
                id = feedDetails.id,
                category = feedDetails.category,
                title = feedDetails.title,
                subtitle = feedDetails.subtitle,
                content = feedDetails.textContent, // This contains the enhanced content
                author = feedDetails.author,
                url = feedDetails.url,
                imageUrl = feedDetails.imageUrl,
                publishedAt = feedDetails.publishedAt
            )
            
            _navigationInteraction.emit(
                FeedDetailsScreenNavigationInteraction.OpenConversation(feedDetailsForNavigation)
            )
        }
    }
}
