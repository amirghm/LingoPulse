# Conversation API Implementation

## Overview

Successfully implemented the conversation API integration using MVVM Clean Architecture pattern.

## API Details

- **Endpoint**: `POST https://lingua.app.n8n.cloud/webhook/79b02647-40c9-4d09-970c-bde18e8ff9ab`
- **Auth Header**: `Authtoken: OO@%w^Cu*3!hAj`
- **Content-Type**: `application/json`

## Request Body Structure

```json
{
  "article": "Article content text",
  "targetLanguage": "🇫🇷 French", 
  "languageLevel": "B1 - Pre-Intermediate"
}
```

## Implementation Structure

### 1. Data Layer (`shared/src/commonMain/kotlin/ai/lingopulse/data/news/`)

- **Models**: `ConversationRequest.kt`, `ConversationResponse.kt`
- **API Interface**: `ConversationNetworkAPI.kt`
- **Service**: `ConversationNetworkService.kt` (Ktor HTTP client)
- **Repository**: `ConversationRepositoryImpl.kt`

### 2. Domain Layer (`shared/src/commonMain/kotlin/ai/lingopulse/domain/news/`)

- **Models**: `ConversationRequest.kt`, `ConversationResult.kt`
- **Repository Interface**: `ConversationRepository.kt`
- **Use Case**: `StartConversationUseCase.kt`, `StartConversationUseCaseImpl.kt`
- **Mapper**: `LanguageMapper.kt` (converts IDs to display names)

### 3. Dependency Injection

- Added all components to DI modules in `shared/src/commonMain/kotlin/ai/lingopulse/di/modules.kt`
- Updated `FeedDetailsViewModel` with `StartConversationUseCase` dependency

## Integration Points

### FeedDetailsViewModel

- **Method**: `onStartConversationClicked(feedDetails: UiFeedDetails)`
- **Functionality**:
    1. Gets user language preferences from `StorageRepository`
    2. Validates user has set preferences
    3. Converts language IDs to display names using mapper
    4. Creates API request with article content
    5. Calls API via use case
    6. Handles success/error responses with logging

### User Flow

1. User taps "Listen and Learn" button on article details
2. App retrieves user's language preference and level from settings
3. App sends article content + preferences to API
4. On success, navigates to conversation screen
5. On error, logs error message (can be enhanced with UI feedback)

## Error Handling

- Network errors caught and wrapped in `ConversationResponse`
- User preference validation
- Comprehensive logging for debugging
- Graceful fallback on API failures

## Testing

1. Set user language and level in Settings screen
2. Navigate to any article details
3. Tap "Listen and Learn" button
4. Check logs for API request/response details

## Future Enhancements

- Add loading states to UI during API call
- Implement error dialogs/snackbars for user feedback
- Add retry mechanisms for failed requests
- Cache conversation results
- Add conversation history persistence

## Status

✅ **IMPLEMENTED** - Ready for testing and integration with conversation UI components.