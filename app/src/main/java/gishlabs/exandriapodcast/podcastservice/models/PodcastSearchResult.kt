package gishlabs.exandriapodcast.podcastservice.models

import com.google.gson.annotations.SerializedName

data class PodcastSearchResult(
    val count: Int,
    @SerializedName("next_offset") val nextOffset: Int,
    val total: Int,
    val took: Double,
    val results: List<Podcasts>
)

data class Podcasts(
    val rss: String,
    @SerializedName("description_highlighted") val descriptionHighlighted: String,
    @SerializedName("description_original") val descriptionOriginal: String,
    @SerializedName("title_highlighted") val titleHighlighted: String,
    @SerializedName("title_original") val titleOriginal: String,
    @SerializedName("publisher_highlighted") val publisherHighlighted: String,
    @SerializedName("publisher_original") val publisherOriginal: String,
    val image: String,
    val thumbnail: String,
    @SerializedName("itunes_id") val itunesID: Int,
    @SerializedName("latest_pub_date_ms") val latestPubDateMs: Long,
    @SerializedName("earliest_pub_date_ms") val earliestPubDateMs: Long,
    val id: String,
    @SerializedName("genre_ids") val genreIDs: List<String>,
    @SerializedName("listennotes_url") val listenNotesUrl: String,
    @SerializedName("total_episodes") val totalEpisodes: Int,
    val email: String,
    @SerializedName("explicit_content") val explicitContent: Boolean,
    val website: String,
    @SerializedName("listen_score") val listenScore: String,
    @SerializedName("listen_score_global_rank") val listenScoreGlobalRank: String
)
