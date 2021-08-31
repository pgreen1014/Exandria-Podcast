package gishlabs.exandriapodcast.podcastrepository.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import gishlabs.exandriapodcast.podcastrepository.models.MIGHTY_NEIN_CAMPAIGN
import gishlabs.exandriapodcast.podcastrepository.models.Podcast

@Dao
interface PodcastDao {

    @Query("SELECT * FROM podcast")
    fun getAllEpisodes(): List<Podcast>

    @Query("SELECT * FROM podcast WHERE showID=(:showId) ORDER BY publishedDateMillis ASC")
    fun getAllEpisodes(showId: Int): List<Podcast>

    @Query("SELECT * FROM podcast WHERE showID=$MIGHTY_NEIN_CAMPAIGN ORDER BY publishedDateMillis ASC LIMIT 10")
    fun getRecentEpisodes(): List<Podcast>

    @Insert
    fun insertPodcast(vararg podcast: Podcast)
}