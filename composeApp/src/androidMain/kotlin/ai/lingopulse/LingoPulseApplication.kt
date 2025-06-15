package ai.lingopulse

import android.app.Application
import ai.lingopulse.di.initKoin
import org.koin.android.ext.koin.androidContext

class LingoPulseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@LingoPulseApplication)
        }
    }
}