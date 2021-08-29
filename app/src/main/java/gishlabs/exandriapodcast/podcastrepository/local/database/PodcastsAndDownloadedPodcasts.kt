package gishlabs.exandriapodcast.podcastrepository.local.database

import androidx.room.Embedded
import androidx.room.Relation
import gishlabs.exandriapodcast.podcastrepository.models.Podcast

data class PodcastsAndDownloadedPodcasts(
    @Embedded val podcast: Podcast,
    @Relation(
        parentColumn = "id",
        entityColumn = "episode_id"
    )
    val download: DownloadedPodcast
)
