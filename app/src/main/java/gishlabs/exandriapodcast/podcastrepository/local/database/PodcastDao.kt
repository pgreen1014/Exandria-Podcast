package gishlabs.exandriapodcast.podcastrepository.local.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface PodcastDao {
    @Query("SELECT * FROM podcast")
    fun getAllEpisodes(): List<Podcast>
}