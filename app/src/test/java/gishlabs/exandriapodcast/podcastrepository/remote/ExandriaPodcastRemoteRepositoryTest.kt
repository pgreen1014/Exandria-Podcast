package gishlabs.exandriapodcast.podcastrepository.remote

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import gishlabs.exandriapodcast.podcastrepository.remote.exceptions.UnsuccessfulHTTPStatusCodeException
import gishlabs.exandriapodcast.podcastrepository.remote.models.PodcastEpisode
import gishlabs.exandriapodcast.podcastrepository.remote.models.PodcastEpisodesResult
import junit.framework.TestCase.*
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
import java.io.IOException
import java.lang.RuntimeException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ExandriaPodcastRemoteRepositoryTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var mockListenNotesService: ListenNotesService
    private lateinit var repository: ExandriaPodcastRemoteRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockListenNotesService = mock()
        repository = ExandriaPodcastRemoteRepository(mockListenNotesService, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `getPodcastOneEpisodes calls onSuccess if successful`() = runBlocking {
        val expectedResults = mockSuccessfulPodcastEpisodesCall()
        val episodesResult = mutableListOf<PodcastEpisode>()
        val successCallback: (episodes:List<PodcastEpisode>) -> Unit = { episodes ->
            episodesResult.addAll(episodes)
        }
        val failureCallback: (error: Throwable) -> Unit = { fail("onFail() should not be called") }

        repository.getPodcastOneEpisodes(ListenNotesService.SORT_ORDER_OLDEST_FIRST, successCallback, failureCallback)

        assertEquals(
            "onSuccess should be called with the number of episodes returned from service",
            expectedResults.episodes.size,
            episodesResult.size
        )
    }

    @Test
    fun `getPodcastTwoEpisodes calls onSuccess if successful`() = runBlocking {
        val expectedResults = mockSuccessfulPodcastEpisodesCall()
        val episodesResult = mutableListOf<PodcastEpisode>()
        val successCallback: (episodes:List<PodcastEpisode>) -> Unit = { episodes ->
            episodesResult.addAll(episodes)
        }
        val failureCallback: (error: Throwable) -> Unit = { fail("onFail() should not be called") }

        repository.getPodcastTwoEpisodes(ListenNotesService.SORT_ORDER_OLDEST_FIRST, successCallback, failureCallback)

        assertEquals(
            "onSuccess should be called with the number of episodes returned from service",
            expectedResults.episodes.size,
            episodesResult.size
        )
    }

    @Test
    fun `getBetweenTheSheetsEpisodes calls onSuccess if successful`() = runBlocking {
        val expectedResults = mockSuccessfulPodcastEpisodesCall()
        val episodesResult = mutableListOf<PodcastEpisode>()
        val successCallback: (episodes:List<PodcastEpisode>) -> Unit = { episodes ->
            episodesResult.addAll(episodes)
        }
        val failureCallback: (error: Throwable) -> Unit = { fail("onFail() should not be called") }

        repository.getBetweenTheSheetsEpisodes(ListenNotesService.SORT_ORDER_OLDEST_FIRST, successCallback, failureCallback)

        assertEquals(
            "onSuccess should be called with the number of episodes returned from service",
            expectedResults.episodes.size,
            episodesResult.size
        )
    }

    @Test
    fun `getPodcastOneEpisodes calls onFailure if exception thrown during network`() = runBlocking {
        val expectedMessage = "test message"
        val successCallback: (episodes:List<PodcastEpisode>) -> Unit = { fail() }
        var messageResult = ""
        val failureCallback: (error: Throwable) -> Unit = { error ->
            messageResult = error.message!!
        }

        val exceptionsToTest = listOf<Throwable>(
            IOException(expectedMessage),
            RuntimeException(expectedMessage)
        )

        var numberOfExceptionsTested = 0
        exceptionsToTest.forEach {
            mockFailedNetworkCall(it)
            repository.getPodcastOneEpisodes(ListenNotesService.SORT_ORDER_OLDEST_FIRST, successCallback, failureCallback)
            assertEquals("onFail lambda should be called with throwable", expectedMessage, messageResult)
            numberOfExceptionsTested++
        }

        if (numberOfExceptionsTested == 0) fail("Test is broken: we did not run any assertions")
    }

    @Test
    fun `getPodcastTwoEpisodes calls onFailure if exception thrown during network`() = runBlocking {
        val expectedMessage = "test message"
        val successCallback: (episodes:List<PodcastEpisode>) -> Unit = { fail() }
        var messageResult = ""
        val failureCallback: (error: Throwable) -> Unit = { error ->
            messageResult = error.message!!
        }

        val exceptionsToTest = listOf<Throwable>(
            IOException(expectedMessage),
            RuntimeException(expectedMessage)
        )

        var numberOfExceptionsTested = 0
        exceptionsToTest.forEach {
            mockFailedNetworkCall(it)
            repository.getPodcastTwoEpisodes(ListenNotesService.SORT_ORDER_OLDEST_FIRST, successCallback, failureCallback)
            assertEquals("onFail lambda should be called with throwable", expectedMessage, messageResult)
            numberOfExceptionsTested++
        }

        if (numberOfExceptionsTested == 0) fail("Test is broken: we did not run any assertions")
    }

    @Test
    fun `getBetweenTheSheetsEpisodes calls onFailure if Exception thrown during network`() = runBlocking {
        val expectedMessage = "test message"
        val successCallback: (episodes:List<PodcastEpisode>) -> Unit = { fail() }
        var messageResult = ""
        val failureCallback: (error: Throwable) -> Unit = { error ->
            messageResult = error.message!!
        }

        val exceptionsToTest = listOf<Throwable>(
            IOException(expectedMessage),
            RuntimeException(expectedMessage)
        )

        var numberOfExceptionsTested = 0
        exceptionsToTest.forEach {
            mockFailedNetworkCall(it)
            repository.getBetweenTheSheetsEpisodes(ListenNotesService.SORT_ORDER_OLDEST_FIRST, successCallback, failureCallback)
            assertEquals("onFail lambda should be called with throwable", expectedMessage, messageResult)
            numberOfExceptionsTested++
        }

        if (numberOfExceptionsTested == 0) fail("Test is broken: we did not run any assertions")
    }

    @Test
    fun `getPodcastOneEpisodes calls onFailure if response is not a successfull HTTP status code`() = runBlocking {
        mockFailedPodcastEpisodesCall()
        val successCallback: (episodes:List<PodcastEpisode>) -> Unit = { fail() }
        var errorResult: Throwable? = null
        val failureCallback: (error: Throwable) -> Unit = { error ->
            errorResult = error
        }

        repository.getPodcastOneEpisodes(ListenNotesService.SORT_ORDER_OLDEST_FIRST, successCallback, failureCallback)

        assertTrue("Unsuccessful Http response code should throw an UnsuccessfulNetoworkResponseException",
            errorResult is UnsuccessfulHTTPStatusCodeException)
    }

    @Test
    fun `getPodcastTwoEpisodes calls onFailure if response is not a successfull HTTP status code`() = runBlocking {
        mockFailedPodcastEpisodesCall()
        val successCallback: (episodes:List<PodcastEpisode>) -> Unit = { fail() }
        var errorResult: Throwable? = null
        val failureCallback: (error: Throwable) -> Unit = { error ->
            errorResult = error
        }

        repository.getPodcastTwoEpisodes(ListenNotesService.SORT_ORDER_OLDEST_FIRST, successCallback, failureCallback)

        assertTrue("Unsuccessful Http response code should throw an UnsuccessfulNetoworkResponseException",
            errorResult is UnsuccessfulHTTPStatusCodeException)
    }

    @Test
    fun `getBetweenTheSheetsEpisodes calls onFailure if response is not a successfull HTTP status code`() = runBlocking {
        mockFailedPodcastEpisodesCall()
        val successCallback: (episodes:List<PodcastEpisode>) -> Unit = { fail() }
        var errorResult: Throwable? = null
        val failureCallback: (error: Throwable) -> Unit = { error ->
            errorResult = error
        }

        repository.getBetweenTheSheetsEpisodes(ListenNotesService.SORT_ORDER_OLDEST_FIRST, successCallback, failureCallback)

        assertTrue("Unsuccessful Http response code should throw an UnsuccessfulNetoworkResponseException",
            errorResult is UnsuccessfulHTTPStatusCodeException)
    }

    private fun mockFailedNetworkCall(exceptionToThrow: Throwable) {
        val mockEpisodesCall: Call<PodcastEpisodesResult> = mock()
        whenever(mockListenNotesService.getPodcastEpisodes(any(), any())).thenReturn(mockEpisodesCall)
        whenever(mockEpisodesCall.execute()).thenThrow(exceptionToThrow)
    }

    private fun mockSuccessfulPodcastEpisodesCall(): PodcastEpisodesResult {
        val mockEpisodesCall: Call<PodcastEpisodesResult> = mock()
        val mockResponse: Response<PodcastEpisodesResult> = mock()
        whenever(mockListenNotesService.getPodcastEpisodes(any(), any())).thenReturn(mockEpisodesCall)
        whenever(mockEpisodesCall.execute()).thenReturn(mockResponse)

        whenever(mockResponse.isSuccessful).thenReturn(true)
        val expectedResults = createPodcastEpisodesResultData()
        whenever(mockResponse.body()).thenReturn(expectedResults)

        return expectedResults
    }

    private fun mockFailedPodcastEpisodesCall() {
        val mockEpisodesCall: Call<PodcastEpisodesResult> = mock()
        val request = Request.Builder().url("https://www.url.com").build()
        whenever(mockEpisodesCall.request()).thenReturn(request)

        val mockResponse: Response<PodcastEpisodesResult> = mock()
        whenever(mockListenNotesService.getPodcastEpisodes(any(), any())).thenReturn(mockEpisodesCall)
        whenever(mockEpisodesCall.execute()).thenReturn(mockResponse)

        whenever(mockResponse.isSuccessful).thenReturn(false)
        whenever(mockResponse.code()).thenReturn(400)
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

}