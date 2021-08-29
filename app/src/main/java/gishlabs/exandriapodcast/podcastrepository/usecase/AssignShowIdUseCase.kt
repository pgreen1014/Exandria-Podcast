package gishlabs.exandriapodcast.podcastrepository.usecase

import gishlabs.exandriapodcast.podcastrepository.models.*

class AssignShowIdUseCase {

    /**
     * Between the Sheets episodes are in there own podcast. So we don't need to evaluate for it
     */
    fun assignShowId(episodeTitle: String): Int {
        return when {
            episodeTitle.contains("Vox Machina Ep.") -> {
                VOX_MACHINA_CAMPAIGN
            }
            episodeTitle.contains("Talks Machina") -> {
                TALKS_MACHINA
            }
            episodeTitle.split(" ")[0].contains("C2") -> {
                MIGHTY_NEIN_CAMPAIGN
            }
            episodeTitle.contains("Exandria Unlimited") -> {
                EXANDRIA_UNLIMITED
            }
            else -> ONE_SHOTS_AND_MISC
        }
    }

}