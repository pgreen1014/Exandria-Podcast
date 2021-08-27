package gishlabs.exandriapodcast.podcastrepository.remote.exceptions

import java.lang.Exception

class UnsuccessfulNetworkResponseException(errorMessage: String): Exception(errorMessage)
