package gishlabs.exandriapodcast.podcastrepository.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "downloaded_podcast")
data class DownloadedPodcast(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "episode_id") val episodeId: Int,
    val downloadData: ByteArray
)
