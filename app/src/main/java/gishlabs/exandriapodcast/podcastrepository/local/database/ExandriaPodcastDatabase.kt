package gishlabs.exandriapodcast.podcastrepository.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ Podcast::class, DownloadedPodcast::class ], version = 1)
abstract class ExandriaPodcastDatabase : RoomDatabase() {

    abstract fun podcastDao(): PodcastDao

}