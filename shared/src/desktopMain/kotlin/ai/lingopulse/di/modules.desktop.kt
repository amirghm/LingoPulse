package ai.lingopulse.di

import ai.lingopulse.shared.Platform
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformModule = module {
    singleOf(::Platform)
}