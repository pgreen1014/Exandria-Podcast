package gishlabs.exandriapodcast.podcastrepository.usecase

import gishlabs.exandriapodcast.podcastrepository.models.*
import junit.framework.TestCase.assertEquals
import org.junit.Test

class AssignShowIdUseCaseTest {


    @Test
    fun `assign showID to campaign one episodes`() {
        val input = "Vox Machina Ep. 96 - Family Matters"
        val systemUnderTest = AssignShowIdUseCase()
        val result = systemUnderTest.assignShowId(input)
        assertEquals("Episodes with Vox Macina Ep. in title should be assigned to $VOX_MACHINA_CAMPAIGN",
            VOX_MACHINA_CAMPAIGN, result)
    }

    @Test
    fun `assign showID to talks machina episodes`() {
        val inputs = listOf(
            "Talks Machina: Discussing C1E110 - The Climb Within",
            "Talks Machina: Discussing Up to C2E119 - Malice and Mystery Below"
        )
        val systemUnderTest = AssignShowIdUseCase()
        inputs.forEach {
            val result = systemUnderTest.assignShowId(it)
            assertEquals("Episodes titles with Talks Machina in them should be assigned to $TALKS_MACHINA",
                TALKS_MACHINA, result)
        }
    }

    @Test
    fun `assign showID to one-shots and miscellaneous`() {
        val inputs = listOf(
            "Campaign Wrap-Up",
            "Talking Liam's Quest",
            "Pants Optional Critmas!",
            "Talking Critical Role - Battle Royale"
        )
        val systemUnderTest = AssignShowIdUseCase()
        inputs.forEach {
            val result = systemUnderTest.assignShowId(it)
            assertEquals("Episodes that don't fit into one of the other channels should be assigned the showID of $ONE_SHOTS_AND_MISC",
                ONE_SHOTS_AND_MISC, result)
        }
    }

    @Test
    fun `assign showID to campaign two episodes`() {
        val inputs = listOf(
            "C2E11 Zemnian Nights"
        )
        val systemUnderTest = AssignShowIdUseCase()
        inputs.forEach {
            val result = systemUnderTest.assignShowId(it)
            assertEquals("Campaign two episodes should be assigned the show id $MIGHTY_NEIN_CAMPAIGN",
                MIGHTY_NEIN_CAMPAIGN, result)
        }
    }

    @Test
    fun `assign showID to Exandria Unlimited episodes` () {
        val inputs = listOf(
            "Exandria Unlimited Episode 1: The Nameless Ones",
            "Exandria Unlimited Episode 2: The Oh No Plateau",
            "Exandria Unlimited Episode 3: A Glorious Return",
            "Exandria Unlimited Episode 4: By The Road",
            "Exandria Unlimited Episode 5: A Test of Worth",
            "Exandria Unlimited Episode 6: The Gift Among the Green",
            "Exandria Unlimited Episode 7: Beyond the Heart City",
            "Exandria Unlimited Episode 8: What Comes Next"
        )
        val systemUnderTest = AssignShowIdUseCase()
        inputs.forEach {
            val result = systemUnderTest.assignShowId(it)
            assertEquals("Exandria Unlimited episodes should be assigned the show id $EXANDRIA_UNLIMITED",
                EXANDRIA_UNLIMITED, result)
        }
    }
}