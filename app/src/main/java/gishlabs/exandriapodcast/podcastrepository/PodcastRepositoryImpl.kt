package gishlabs.exandriapodcast.podcastrepository

import gishlabs.exandriapodcast.podcastrepository.remote.PodcastService

class PodcastRepositoryImpl(
    private val remoteRepo: PodcastService,
    ): PodcastRepository {


}