package gishlabs.exandriapodcast.podcastrepository.remote

import com.nhaarman.mockitokotlin2.*
import gishlabs.exandriapodcast.podcastrepository.models.BETWEEN_THE_SHEETS
import gishlabs.exandriapodcast.podcastrepository.models.MIGHTY_NEIN_CAMPAIGN
import gishlabs.exandriapodcast.podcastrepository.models.Podcast
import gishlabs.exandriapodcast.podcastrepository.remote.exceptions.UnsuccessfulHTTPStatusCodeException
import gishlabs.exandriapodcast.podcastrepository.remote.listennotes.ListenNotesService
import gishlabs.exandriapodcast.podcastrepository.remote.models.PodcastEpisode
import gishlabs.exandriapodcast.podcastrepository.remote.models.PodcastEpisodesResult
import gishlabs.testutils.CoroutinesTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking
import okhttp3.Request
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PodcastServiceImplTest : CoroutinesTest() {

    private lateinit var mockService: ListenNotesService
    private lateinit var mockCall: Call<PodcastEpisodesResult>
    private lateinit var mockResponse: Response<PodcastEpisodesResult>
    private lateinit var systemUnderTest: PodcastServiceImpl

    @Before
    override fun setUp() {
        super.setUp()
        mockService = mock()
        mockCall = mock()
        mockResponse = mock()
        systemUnderTest = PodcastServiceImpl(mockService, testDispatcher, GlobalScope.coroutineContext)
    }

    @After
    override fun tearDown() {
        super.tearDown()
    }


    @Test
    fun `getNewEpisodes call onNewEpisodesFound lambda with episodes released since latest episode in db`(): Unit = runBlocking {
        val responseResult = createPodcastEpisodeResult("C2",1, 10, 10)
        whenever(mockService.getPodcastEpisodes(any(), any())).thenReturn(mockCall)
        whenever(mockCall.execute()).thenReturn(mockResponse)
        whenever(mockResponse.isSuccessful).thenReturn(true)
        whenever(mockResponse.body()).thenReturn(responseResult)

        val lastEpisodeInDB = Podcast("5", "title", "description", MIGHTY_NEIN_CAMPAIGN, 5L)
        systemUnderTest.getNewEpisodes(MIGHTY_NEIN_CAMPAIGN, lastEpisodeInDB) {
            assertTrue(it.size == 4)
            it.forEach { result ->
                val resultId = result.id.toInt()
                val lastEpId = lastEpisodeInDB.id.toInt()
                assertTrue(resultId < lastEpId)
            }
        }
    }

    @Test
    fun `getNewEpisodes properly handles updates for shows from multiple podcasts`(): Unit = runBlocking {
        val mockCallToCurrentPodcast: Call<PodcastEpisodesResult> = mock()
        whenever(mockService.getPodcastEpisodes(eq(ListenNotesService.PODCAST_ID_CURRENT), any())).thenReturn(mockCallToCurrentPodcast)
        val currentPodcastResponse = createPodcastEpisodeResult("C2", 20, 11, 10)
        val mockCurrentResponse: Response<PodcastEpisodesResult> = mock {
            on { isSuccessful }.doReturn(true)
            on { body() }.doReturn(currentPodcastResponse)
        }
        whenever(mockCallToCurrentPodcast.execute()).thenReturn(mockCurrentResponse)

        val mockCallToOriginalPodcastResult: Call<PodcastEpisodesResult> = mock()
        whenever(mockService.getPodcastEpisodes(eq(ListenNotesService.PODCAST_ID_ORIGINAL), any())).thenReturn(mockCallToOriginalPodcastResult)
        val originalPodcastResponse = createPodcastEpisodeResult("C2", 10, 1, 10)
        val mockOriginalPodcastResponse: Response<PodcastEpisodesResult> = mock {
            on { isSuccessful }.doReturn(true)
            on { body() }.doReturn(originalPodcastResponse)
        }
        whenever(mockCallToOriginalPodcastResult.execute()).thenReturn(mockOriginalPodcastResponse)

        val lastEpisodeInDB = Podcast("5", "title", "description", MIGHTY_NEIN_CAMPAIGN, 5L)
        systemUnderTest.getNewEpisodes(MIGHTY_NEIN_CAMPAIGN, lastEpisodeInDB) {
            assertTrue(it.size == 15)
            it.forEach { result ->
                val resultId = result.id.toInt()
                val lastEpId = lastEpisodeInDB.id.toInt()
                assertTrue(resultId > lastEpId)
            }
        }

    }

    @Test
    fun `getNewEpisodes exits loop, returning all episodes in show, if last episode is not found`(): Unit = runBlocking{
        val mockCallToCurrentPodcast: Call<PodcastEpisodesResult> = mock()
        whenever(mockService.getPodcastEpisodes(eq(ListenNotesService.PODCAST_ID_CURRENT), any())).thenReturn(mockCallToCurrentPodcast)
        val currentPodcastResponse = createPodcastEpisodeResult("C2", 20, 11, 10)
        val mockCurrentResponse: Response<PodcastEpisodesResult> = mock {
            on { isSuccessful }.doReturn(true)
            on { body() }.doReturn(currentPodcastResponse)
        }
        whenever(mockCallToCurrentPodcast.execute()).thenReturn(mockCurrentResponse)

        val mockCallToOriginalPodcastResult: Call<PodcastEpisodesResult> = mock()
        whenever(mockService.getPodcastEpisodes(eq(ListenNotesService.PODCAST_ID_ORIGINAL), any())).thenReturn(mockCallToOriginalPodcastResult)
        val originalPodcastResponse = createPodcastEpisodeResult("C2", 10, 1, 10)
        val mockOriginalPodcastResponse: Response<PodcastEpisodesResult> = mock {
            on { isSuccessful }.doReturn(true)
            on { body() }.doReturn(originalPodcastResponse)
        }
        whenever(mockCallToOriginalPodcastResult.execute()).thenReturn(mockOriginalPodcastResponse)

        val lastEpisodeInDB = Podcast("923451", "title", "description", MIGHTY_NEIN_CAMPAIGN, 5L)
        systemUnderTest.getNewEpisodes(MIGHTY_NEIN_CAMPAIGN, lastEpisodeInDB) {
            assertTrue(it.size == 20)
        }
    }

    @Test
    fun `getNewEpisodes can handle service pagination`(): Unit = runBlocking {
        val mockCallToCurrentPodcast: Call<PodcastEpisodesResult> = mock()
        whenever(mockService.getPodcastEpisodes(eq(ListenNotesService.PODCAST_ID_CURRENT), any())).thenReturn(mockCallToCurrentPodcast)
        val currentPodcastResponseOne = createPodcastEpisodeResult("C2", 30, 21, 20)
        val currentPodcastResponseTwo = createPodcastEpisodeResult("C2", 20, 11, 20)
        val mockCurrentResponse: Response<PodcastEpisodesResult> = mock {
            on { isSuccessful }.doReturn(true)
            on { body() }.doReturn(currentPodcastResponseOne, currentPodcastResponseTwo)
        }
        whenever(mockCallToCurrentPodcast.execute()).thenReturn(mockCurrentResponse)

        val mockCallToOriginalPodcastResult: Call<PodcastEpisodesResult> = mock()
        whenever(mockService.getPodcastEpisodes(eq(ListenNotesService.PODCAST_ID_ORIGINAL), any())).thenReturn(mockCallToOriginalPodcastResult)
        val originalPodcastResponse = createPodcastEpisodeResult("C2", 10, 1, 10)
        val mockOriginalPodcastResponse: Response<PodcastEpisodesResult> = mock {
            on { isSuccessful }.doReturn(true)
            on { body() }.doReturn(originalPodcastResponse)
        }
        whenever(mockCallToOriginalPodcastResult.execute()).thenReturn(mockOriginalPodcastResponse)

        val lastEpisodeInDB = Podcast("5", "title", "description", MIGHTY_NEIN_CAMPAIGN, 5L)
        systemUnderTest.getNewEpisodes(MIGHTY_NEIN_CAMPAIGN, lastEpisodeInDB) {
            assertTrue(it.size == 25)
        }
    }

    @Test
    fun `getNewEpisodes does not include episodes for shows not passed as the showID`(): Unit = runBlocking {
        val resultPodcastEpisodes = listOf(
            PodcastEpisode("6", "Random One Shot", "description", 100L, "url", 1, "url", false),
            PodcastEpisode("5", "C2E3", "description", 100L, "url", 1, "url", false),
            PodcastEpisode("4", "Talks Machina C2E2", "description", 100L, "url", 1, "url", false),
            PodcastEpisode("3", "C2E2", "description", 100L, "url", 1, "url", false),
            PodcastEpisode("2", "Talks Machina C2E1", "description", 100L, "url", 1, "url", false),
            PodcastEpisode("1", "C2E1", "description", 100L, "url", 1, "url", false)
        )
        val podcastEpisodesResult = PodcastEpisodesResult("test_id", totalEpisodes = resultPodcastEpisodes.size, 1L, resultPodcastEpisodes, 1L)
        whenever(mockService.getPodcastEpisodes(any(), any())).thenReturn(mockCall)
        whenever(mockCall.execute()).thenReturn(mockResponse)
        whenever(mockResponse.isSuccessful).thenReturn(true)
        whenever(mockResponse.body()).thenReturn(podcastEpisodesResult)

        val lastEpisodeInDB = Podcast("1", "title", "description", BETWEEN_THE_SHEETS, 5L)
        systemUnderTest.getNewEpisodes(MIGHTY_NEIN_CAMPAIGN, lastEpisodeInDB) {
            assertTrue(it.size == 2)
            it.forEach { result ->
                assertEquals(MIGHTY_NEIN_CAMPAIGN, result.showID)
                assertTrue(result.episodeTitle == "C2E3" || result.episodeTitle == "C2E2")
            }
        }
    }

    @Test
    fun `getNewEpisodes is compatable with between the sheets episodes`(): Unit = runBlocking {
        val resultPodcastEpisodes = createPodcastEpisodeResult("A Between the Sheets Episode ", 10, 1, 10)
        whenever(mockService.getPodcastEpisodes(any(), any())).thenReturn(mockCall)
        whenever(mockCall.execute()).thenReturn(mockResponse)
        whenever(mockResponse.isSuccessful).thenReturn(true)
        whenever(mockResponse.body()).thenReturn(resultPodcastEpisodes)

        val lastEpisodeInDb = Podcast("3", "A Between the Sheets Episode 3", "", BETWEEN_THE_SHEETS, 10L)
        systemUnderTest.getNewEpisodes(BETWEEN_THE_SHEETS, lastEpisodeInDb) {
            assertTrue(it.size == 7)
            it.forEach { episode ->
                assertTrue(episode.id.toInt() > lastEpisodeInDb.id.toInt())
            }
        }
    }

    @Test
    fun `getNewEpisodes throws UnsuccessfuHTTPSStatusCodeException if response is unsuccessful`(): Unit = runBlocking {
        whenever(mockService.getPodcastEpisodes(any(), any())).thenReturn(mockCall)
        whenever(mockCall.execute()).thenReturn(mockResponse)
        whenever(mockResponse.code()).thenReturn(500)
        val mockRequest: Request = mock()
        whenever(mockCall.request()).thenReturn(mockRequest)
        whenever(mockRequest.url()).thenReturn(mock())
        whenever(mockResponse.isSuccessful).thenReturn(false)

        try {
            val lastEpisodeInDB = Podcast("5", "title", "description", MIGHTY_NEIN_CAMPAIGN, 5L)
            systemUnderTest.getNewEpisodes(MIGHTY_NEIN_CAMPAIGN, lastEpisodeInDB) {
                fail()
            }
        } catch (ex: Exception) {
            assertTrue(ex is UnsuccessfulHTTPStatusCodeException)
        }

    }

    @Test
    fun `getVoxMachinaEpisodes returns all episodes for vox machina`(): Unit = runBlocking {
        val episodesFromService = listOf(
            PodcastEpisode("1", "C2E1 Curious Beginnings", "", 1L, "", 1, "", false),
            PodcastEpisode("2", "Random One-Shot", "", 1L, "", 1, "", false),
            PodcastEpisode("3", "Vox Machina C1E115 - The Chapter Closes", "", 1L, "", 1, "", false),
            PodcastEpisode("4", "Vox Machina C1E114 - Vecna, The Ascended", "", 1L, "", 1, "", false),
            PodcastEpisode("5", "Talks Machina: Discussing C1E113 the Final Ascent", "", 1L, "", 1, "", false),
            PodcastEpisode("6", "Vox Machina C1E113 - The Final Ascent", "", 1L, "", 1, "", false)
        )
        val serviceResult = PodcastEpisodesResult(
            id = "test_id",
            totalEpisodes = episodesFromService.size,
            earliestPublishedDateMillis = 1L,
            episodes = episodesFromService,
            nextEpisodePubDate = 2L
        )

        whenever(mockService.getPodcastEpisodes(any(), any())).thenReturn(mockCall)
        whenever(mockCall.execute()).thenReturn(mockResponse)
        whenever(mockResponse.isSuccessful).thenReturn(true)
        whenever(mockResponse.body()).thenReturn(serviceResult)

        val result = systemUnderTest.getVoxMachinaEpisodes(ListenNotesService.SORT_ORDER_RECENT_FIRST)
        result.forEach {
            assert(it.episodeTitle.contains("Vox Machina"))
        }
    }

    @Test
    fun `getMightyNeinEpisodes returns all episodes for campaign 2`(): Unit = runBlocking {
        val episodeResult = listOf(
            PodcastEpisode("1", "C2E4 Curious Beginnings", "", 1L, "", 1, "", false),
            PodcastEpisode("2", "C2E5 Curious Beginnings", "", 1L, "", 1, "", false),
            PodcastEpisode("3", "Vox Machina C1E115 - The Chapter Closes", "", 1L, "", 1, "", false),
            PodcastEpisode("4", "Vox Machina C1E114 - Vecna, The Ascended", "", 1L, "", 1, "", false),
            PodcastEpisode("5", "Talks Machina: Discussing C1E113 the Final Ascent", "", 1L, "", 1, "", false),
            PodcastEpisode("6", "Vox Machina C1E113 - The Final Ascent", "", 1L, "", 1, "", false)
        )
        val responseResult = PodcastEpisodesResult(
            id = "test_id",
            totalEpisodes = episodeResult.size,
            earliestPublishedDateMillis = 1L,
            episodes = episodeResult,
            nextEpisodePubDate = 2L
        )

        whenever(mockService.getPodcastEpisodes(any(), any())).thenReturn(mockCall)
        whenever(mockCall.execute()).thenReturn(mockResponse)
        whenever(mockResponse.isSuccessful).thenReturn(true)
        whenever(mockResponse.body()).thenReturn(responseResult)

        val result = systemUnderTest.getMightyNeinEpisodes(ListenNotesService.SORT_ORDER_RECENT_FIRST)
        assertTrue("getMightyNeinEpisodes returned an incorrect list", result.size == 4)
        result.forEach {
            assert(it.episodeTitle.contains("C2"))
        }
    }

    @Test
    fun `getExandriaUnlimitedEpisodes returns all episodes for EXU`(): Unit = runBlocking {
        val episodesResult = listOf(
            PodcastEpisode("1", "The Nautilus Ark: A Johnson Corp Odyssey", "", 1L, "", 1, "", false),
            PodcastEpisode("2", "Exandria Unlimited Wrap-Up", "", 1L, "", 1, "", false),
            PodcastEpisode("3", "The Elder Scrolls Online: Blackwood - Part Two: A Faulty Foundation One-Shot", "", 1L, "", 1, "", false),
            PodcastEpisode("4", "Exandria Unlimited Episode 8: What Comes Next", "", 1L, "", 1, "", false),
            PodcastEpisode("5", "Exandria Unlimited Episode 7: Beyond the Heart City", "", 1L, "", 1, "", false),
            PodcastEpisode("6", "Exandria Unlimited Episode 6: The Gift Among the Green", "", 1L, "", 1, "", false),
            PodcastEpisode("7", "Exandria Unlimited Episode 5: A Test of Worth", "", 1L, "", 1, "", false),
            PodcastEpisode("8", "Exandria Unlimited Episode 4: By the Road", "", 1L, "", 1, "", false),
            PodcastEpisode("9", "Exandria Unlimited Episode 3: A Glorious Return", "", 1L, "", 1, "", false),
            PodcastEpisode("10", "Exandria Unlimited Episode 2: The Oh No Plateau", "", 1L, "", 1, "", false),
            PodcastEpisode("11", "Exandria Unlimited Episode 1: The Nameless Ones", "", 1L, "", 1, "", false),
            PodcastEpisode("12", "Vox Machina vs. Mighty Nein", "", 1L, "", 1, "", false),
        )
        val responseResult = PodcastEpisodesResult(
            id = "test_id",
            totalEpisodes = episodesResult.size,
            earliestPublishedDateMillis = 1L,
            episodes = episodesResult,
            nextEpisodePubDate = 2L
        )

        whenever(mockService.getPodcastEpisodes(any(), any())).thenReturn(mockCall)
        whenever(mockCall.execute()).thenReturn(mockResponse)
        whenever(mockResponse.isSuccessful).thenReturn(true)
        whenever(mockResponse.body()).thenReturn(responseResult)

        val result = systemUnderTest.getExandriaUnlimitedEpisodes(ListenNotesService.SORT_ORDER_RECENT_FIRST)
        assertEquals(9, result.size)
        result.forEach {
            assert(it.episodeTitle.contains("Exandria Unlimited"))
        }
    }

    // region UTILITIES

    private fun createPodcastEpisodeResult(episodeTitle: String, first: Int, last: Int, totalEpisodes: Int) : PodcastEpisodesResult {
        val podcastEpisodes: MutableList<PodcastEpisode> = mutableListOf()
        if (first < last) {
            (first..last).forEach {
                val episode = PodcastEpisode(
                    id = it.toString(),
                    title = "$episodeTitle$it",
                    description = "description $it",
                    publishedDateMillis = it.toLong(),
                    audioUrl = "audio url $it",
                    audioLengthSeconds = 100,
                    thumbnailUrl = "thumbnail url $it",
                    maybeAudioInvalid = false
                )
                podcastEpisodes.add(episode)
            }
        } else {
            (first downTo last).forEach {
                val episode = PodcastEpisode(
                    id = it.toString(),
                    title = "$episodeTitle$it",
                    description = "description $it",
                    publishedDateMillis = it.toLong(),
                    audioUrl = "audio url $it",
                    audioLengthSeconds = 100,
                    thumbnailUrl = "thumbnail url $it",
                    maybeAudioInvalid = false
                )
                podcastEpisodes.add(episode)
            }
        }

        return PodcastEpisodesResult(
            id = "test_id",
            totalEpisodes = totalEpisodes,
            earliestPublishedDateMillis = 1L,
            episodes = podcastEpisodes,
            nextEpisodePubDate = 2L
        )
    }

    // endregion UTILITIES
}