package gishlabs.exandriapodcast.podcastrepository.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import gishlabs.exandriapodcast.podcastrepository.models.Podcast

@Dao
interface PodcastDao {

    @Query("SELECT * FROM podcast")
    fun getAllEpisodes(): List<Podcast>

    @Insert
    fun insertPodcast(vararg podcast: Podcast)
}