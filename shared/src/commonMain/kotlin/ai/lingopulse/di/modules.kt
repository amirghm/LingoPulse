package ai.lingopulse.di

import ai.lingopulse.data.agent.remote.cerebras.CerebrasNetworkAPI
import ai.lingopulse.data.agent.remote.cerebras.CerebrasNetworkService
import ai.lingopulse.data.agent.repository.cerebras.CerebrasRepositoryImpl
import ai.lingopulse.data.conversation.remote.ConversationNetworkAPI
import ai.lingopulse.data.conversation.remote.ConversationNetworkService
import ai.lingopulse.data.conversation.repository.ConversationRepositoryImpl
import ai.lingopulse.data.news.remote.EnhancedNewsNetworkAPI
import ai.lingopulse.data.news.remote.EnhancedNewsNetworkService
import ai.lingopulse.data.news.repository.EnhancedNewsRepositoryImpl
import ai.lingopulse.data.system.repository.StorageRepositoryImpl
import ai.lingopulse.domain.agent.repository.CerebrasRepository
import ai.lingopulse.domain.agent.usecase.AskAgentUseCase
import ai.lingopulse.domain.agent.usecase.AskAgentUseCaseImpl
import ai.lingopulse.domain.agent.usecase.GetFeedsUseCase
import ai.lingopulse.domain.agent.usecase.GetFeedsUseCaseImpl
import ai.lingopulse.domain.conversation.repository.ConversationRepository
import ai.lingopulse.domain.conversation.usecase.ConversationUseCase
import ai.lingopulse.domain.conversation.usecase.ConversationUseCaseImpl
import ai.lingopulse.domain.news.repository.EnhancedNewsRepository
import ai.lingopulse.domain.news.usecase.GetEnhancedNewsUseCase
import ai.lingopulse.domain.news.usecase.GetEnhancedNewsUseCaseImpl
import ai.lingopulse.domain.system.StorageRepository
import ai.lingopulse.presentation.details.FeedDetailsViewModel
import ai.lingopulse.presentation.feed.FeedViewModel
import ai.lingopulse.presentation.setting.SettingViewModel
import ai.lingopulse.presentation.conversation.ConversationViewModel
import ai.lingopulse.util.Storage
import com.russhwolf.settings.Settings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.HttpTimeout
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

expect val platformModule: Module

private val settings = module {
    factory { (name: String) -> Storage(name) }
    single<Settings>(named("Preferences")) { Storage("Preferences").get() }
}

private val network = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = false
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 120_000
                connectTimeoutMillis = 120_000
                socketTimeoutMillis = 120_000
            }
            // Remove default request configuration as we'll handle it in the service
            // The base URL will be configured in CerebrasNetworkService
        }
    }
}

private val dataSources = module {
    single<CerebrasNetworkAPI> {
        CerebrasNetworkService(
            client = get(),
            baseUrl = "http://YOUR_VPS_IP:8085" // Replace with your actual VPS IP
        )
    }
    
    single<EnhancedNewsNetworkAPI> {
        EnhancedNewsNetworkService(
            client = get()
        )
    }
    
    single<ConversationNetworkAPI> {
        ConversationNetworkService(
            client = get()
        )
    }
}

private val repositories = module {
    single<CerebrasRepository> {
        CerebrasRepositoryImpl(
            cerebrasNetworkApi = get()
        )
    }
    single<StorageRepository> {
        StorageRepositoryImpl(
            preference = get(named("Preferences"))
        )
    }
    single<EnhancedNewsRepository> {
        EnhancedNewsRepositoryImpl(
            enhancedNewsNetworkApi = get()
        )
    }
    single<ConversationRepository> {
        ConversationRepositoryImpl(
            conversationNetworkApi = get(),
            assistantId = "asst_1BG5nYmfNfFg2TZTBoDLMU9s" // Explicitly pass the assistant ID
        )
    }
}


private val useCases: Module = module {
    singleOf(::AskAgentUseCaseImpl) { bind<AskAgentUseCase>() }
    singleOf(::GetFeedsUseCaseImpl) { bind<GetFeedsUseCase>() }
    singleOf(::GetEnhancedNewsUseCaseImpl) { bind<GetEnhancedNewsUseCase>() }
    singleOf(::ConversationUseCaseImpl) { bind<ConversationUseCase>() }
}

private val mappers = module {
    // single { MilestonesMapper() }
}

private val coroutines = module {
    single<CoroutineDispatcher>(qualifier = named("MainDispatcher")) { Dispatchers.Main }
    single<CoroutineDispatcher>(qualifier = named("DefaultDispatcher")) { Dispatchers.Default }
}

private val viewModels = module {
    single {
        FeedViewModel(
            getFeedsUseCase = get(),
            storageRepository = get(),
            dispatcher = get(named("DefaultDispatcher"))
        )
    }
    single {
        SettingViewModel(
            storageRepository = get(),
            dispatcher = get(named("DefaultDispatcher"))
        )
    }
    single {
        FeedDetailsViewModel(
            getFeedsUseCase = get(),
            getEnhancedNewsUseCase = get(),
            storageRepository = get(),
            dispatcher = get(named("DefaultDispatcher"))
        )
    }
    single {
        ConversationViewModel(
            conversationUseCase = get(),
            dispatcher = get(named("DefaultDispatcher"))
        )
    }
}

val sharedModule = module {
    includes(
        settings,
        network,
        dataSources,
        repositories,
        useCases,
        mappers,
        viewModels,
        coroutines
    )
}
