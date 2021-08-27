package gishlabs.exandriapodcast

import android.app.Application
import timber.log.Timber

class ExandriaPodcastApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}