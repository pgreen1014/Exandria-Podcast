package gishlabs.exandriapodcast.podcastrepository

import com.nhaarman.mockitokotlin2.*
import gishlabs.exandriapodcast.podcastrepository.local.database.PodcastDao
import gishlabs.exandriapodcast.podcastrepository.remote.exceptions.UnsuccessfulHTTPStatusCodeException
import gishlabs.exandriapodcast.podcastrepository.remote.listennotes.ListenNotesService
import gishlabs.exandriapodcast.podcastrepository.remote.models.PodcastEpisode
import gishlabs.exandriapodcast.podcastrepository.remote.models.PodcastEpisodesResult
import gishlabs.testutils.CoroutinesTest
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.Request
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PodcastDataInitializerTest : CoroutinesTest() {

    private lateinit var mockService: ListenNotesService
    private lateinit var mockLocalRepository: PodcastDao
    private lateinit var systemUnderTest: PodcastDataInitializer
    private lateinit var mockEpisodesCall: Call<PodcastEpisodesResult>

    @Before
    override fun setUp() {
        super.setUp()
        mockService = mock()
        mockLocalRepository = mock()
        mockEpisodesCall = mock()
        systemUnderTest = PodcastDataInitializer(mockService, mockLocalRepository, testDispatcher)
    }

    @ExperimentalCoroutinesApi
    @After
    override fun tearDown() {
        super.tearDown()
    }

    @Test
    fun `initialize should call getEpisodes from all remote podcasts`(): Unit = runBlocking {
        mockSuccessfulPodcastEpisodesCall()
        systemUnderTest.initialize {  }
        verify(mockService).getPodcastEpisodes(eq(ListenNotesService.PODCAST_ID_ORIGINAL), any())
        verify(mockService).getPodcastEpisodes(eq(ListenNotesService.PODCAST_ID_CURRENT), any())
        verify(mockService).getPodcastEpisodes(eq(ListenNotesService.PODCAST_ID_BETWEEN_THE_SHEETS), any())
    }

    @Test
    fun `initialize inserts retrieved episodes in database`(): Unit = runBlocking {
        val expectedResult = mockSuccessfulPodcastEpisodesCall()
        systemUnderTest.initialize {  }

        val expectedNumberOfInvocations = expectedResult.episodes.size * 3
        verify(mockLocalRepository, times(expectedNumberOfInvocations)).insertEpisode(any())
    }

    @Test
    fun `initialize throws UnsuccessfulHTTPSStatusCodeException if bad http code is found`(): Unit = runBlocking {
        mockFailedPodcastEpisodesCall()
        try {
            systemUnderTest.initialize {  }
            fail()
        } catch (exception: Exception) {
            assertTrue("PodcastDataInitializer::initialize should throw UnsuccessfulHTTPSStatusCodeException if bad http code",
                exception is UnsuccessfulHTTPStatusCodeException)
        }
    }

    @Test
    fun `initialize finishes inserting all downloaded episodes to DB before calling onComplete`() = runBlocking {
        val expectedResult = mockSuccessfulPodcastEpisodesCall()
        val expectedNumberOfInvocations = expectedResult.episodes.size * 3
        systemUnderTest.initialize {
            verify(mockLocalRepository, times(expectedNumberOfInvocations)).insertEpisode(any())
        }
    }

    private fun mockSuccessfulPodcastEpisodesCall(): PodcastEpisodesResult {
        val mockResponse: Response<PodcastEpisodesResult> = mock()
        whenever(mockService.getPodcastEpisodes(any(), any())).thenReturn(mockEpisodesCall)
        whenever(mockEpisodesCall.execute()).thenReturn(mockResponse)

        whenever(mockResponse.isSuccessful).thenReturn(true)
        val expectedResults = createPodcastEpisodesResultData()
        whenever(mockResponse.body()).thenReturn(expectedResults)

        return expectedResults
    }

    private fun createPodcastEpisodesResultData(): PodcastEpisodesResult {
        val episodes = createPodcastListData()
        return PodcastEpisodesResult(
            id = "87313241346",
            totalEpisodes = episodes.size,
            earliestPublishedDateMillis = 92399834589L,
            episodes = episodes,
            nextEpisodePubDate = 92389797898234L
        )
    }

    private fun createPodcastListData(): List<PodcastEpisode> {
        val result = mutableListOf<PodcastEpisode>()
        for (i in 1..3) {
            result.add(
                PodcastEpisode(
                    id = i.toString(),
                    title = "title $i",
                    description = "description $i",
                    publishedDateMillis = i.toLong(),
                    audioUrl = "audio url $i",
                    audioLengthSeconds = i,
                    thumbnailUrl = "thumbnail url $i",
                    maybeAudioInvalid = false
                )
            )
        }
        return result
    }

    private fun mockFailedPodcastEpisodesCall() {
        val request = Request.Builder().url("https://www.url.com").build()
        whenever(mockEpisodesCall.request()).thenReturn(request)

        val mockResponse: Response<PodcastEpisodesResult> = mock()
        whenever(mockService.getPodcastEpisodes(any(), any())).thenReturn(mockEpisodesCall)
        whenever(mockEpisodesCall.execute()).thenReturn(mockResponse)

        whenever(mockResponse.isSuccessful).thenReturn(false)
        whenever(mockResponse.code()).thenReturn(400)
    }

}