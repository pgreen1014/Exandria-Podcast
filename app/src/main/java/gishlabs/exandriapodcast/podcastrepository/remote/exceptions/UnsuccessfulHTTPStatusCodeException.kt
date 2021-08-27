package gishlabs.exandriapodcast.podcastrepository.remote.exceptions

import java.lang.Exception

class UnsuccessfulHTTPStatusCodeException(errorMessage: String): Exception(errorMessage)
