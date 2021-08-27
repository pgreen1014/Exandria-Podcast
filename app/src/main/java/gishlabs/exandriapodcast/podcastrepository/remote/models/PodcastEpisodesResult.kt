package gishlabs.exandriapodcast.podcastrepository.remote.models

import com.google.gson.annotations.SerializedName

data class PodcastEpisodesResult(
    val id: String,
    @SerializedName("total_episodes") val totalEpisodes: Int,
    @SerializedName("earliest_pub_date_ms") val earliestPublishedDateMillis: Long,
    val episodes: List<PodcastEpisode>,
    @SerializedName("next_episode_pub_date") val nextEpisodePubDate: Long,
)

data class PodcastEpisode(
    val id: String,
    val title: String,
    val description: String,
    @SerializedName("pub_date_ms") val publishedDateMillis: Long,
    @SerializedName("audio") val audioUrl: String,
    @SerializedName("audio_length_sec") val audioLengthSeconds: Int,
    @SerializedName("thumbnail") val thumbnailUrl: String,
    @SerializedName("maybe_audio_invalid") val maybeAudioInvalid: Boolean,
)
