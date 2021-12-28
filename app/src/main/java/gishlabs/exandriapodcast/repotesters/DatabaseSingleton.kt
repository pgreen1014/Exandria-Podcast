package gishlabs.exandriapodcast.repotesters

import android.content.Context
import androidx.room.Room
import gishlabs.exandriapodcast.podcastrepository.local.database.ExandriaPodcastDatabase
import java.lang.IllegalStateException

class DatabaseSingleton private constructor(context: Context) {

    private val database : ExandriaPodcastDatabase = Room.databaseBuilder(
            context,
            ExandriaPodcastDatabase::class.java,
            "exandria-podcast-database"
    ).build()

    val podcastDao = database.podcastDao()

    companion object {
        private var INSTANCE: DatabaseSingleton? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = DatabaseSingleton(context)
            }
        }

        fun get(): DatabaseSingleton {
            return INSTANCE ?: throw IllegalStateException("DatabaseSingleton must be initialized")
        }
    }

}