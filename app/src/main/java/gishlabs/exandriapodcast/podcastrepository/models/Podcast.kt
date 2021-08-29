package gishlabs.exandriapodcast.podcastrepository.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "podcast")
data class Podcast(
    @PrimaryKey val id: String,
    val episodeTitle: String,
    val episodeDescription: String,
    val showID: Int,
    val publishedDateMillis: Long
)
