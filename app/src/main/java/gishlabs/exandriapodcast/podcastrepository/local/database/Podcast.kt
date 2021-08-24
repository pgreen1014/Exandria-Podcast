package gishlabs.exandriapodcast.podcastrepository.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "podcast")
data class Podcast(
    @PrimaryKey val id: Int,
    val episodeTitle: String,
    val episodeDescription: String,
    val showID: Int,
    val episodeChannel: Int
)
