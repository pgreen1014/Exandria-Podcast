package gishlabs.exandriapodcast.podcastrepository

import com.nhaarman.mockitokotlin2.*
import gishlabs.exandriapodcast.podcastrepository.local.database.PodcastDao
import gishlabs.exandriapodcast.podcastrepository.models.*
import gishlabs.exandriapodcast.podcastrepository.remote.PodcastService
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PodcastRepositoryImplTest {

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var mockPodcastDao: PodcastDao
    private lateinit var mockRemoteRepo: PodcastService
    private lateinit var systemUnderTest: PodcastRepositoryImpl

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockPodcastDao = mock()
        mockRemoteRepo = mock()
        systemUnderTest = PodcastRepositoryImpl(mockPodcastDao, mockRemoteRepo)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `getAllExandriaPodcasts returns all podcast data`() = runBlocking {
        val expectedResult = createPodcastResult(5)
        whenever(mockPodcastDao.getAllEpisodes()).thenReturn(expectedResult)

        var actualResult: List<Podcast>? = null
        systemUnderTest.getAllExandriaPodcasts {
            actualResult = it
        }

        verify(mockPodcastDao).getAllEpisodes()
        assertTrue(actualResult!!.containsAll(expectedResult))
    }

    @Test
    fun `getAllFoxMachinaPodcasts returns all Vox Machina podcast data`() = runBlocking {
        val expectedResult = createPodcastResult(EXPECTED_VOX_MACHINA_EPISODES)
        whenever(mockPodcastDao.getAllEpisodes(VOX_MACHINA_CAMPAIGN)).thenReturn(expectedResult)

        var actualResult: List<Podcast>? = null
        systemUnderTest.getAllVoxMachinaPodcasts {
            actualResult = it
        }

        verify(mockPodcastDao).getAllEpisodes(VOX_MACHINA_CAMPAIGN)
        assertTrue(actualResult!!.containsAll(expectedResult))
    }

    @Test
    fun `getAllMightyNeinPodcasts returns all mighty nein podcast data`() = runBlocking {
        val expectedResult = createPodcastResult(EXPECTED_MIGHTY_NEIN_EPISODES)
        whenever(mockPodcastDao.getAllEpisodes(MIGHTY_NEIN_CAMPAIGN)).thenReturn(expectedResult)

        var actualResult: List<Podcast>? = null
        systemUnderTest.getAllMightyNeinPodcasts {
            actualResult = it
        }

        verify(mockPodcastDao).getAllEpisodes(MIGHTY_NEIN_CAMPAIGN)
        assertTrue(actualResult!!.containsAll(expectedResult))
    }

    @Test
    fun `getAllTalksMachinaPodcasts returns all talks machina podcast data`() = runBlocking {
        val expectedResult = createPodcastResult(5)
        whenever(mockPodcastDao.getAllEpisodes(TALKS_MACHINA)).thenReturn(expectedResult)

        var actualResult: List<Podcast>? = null
        systemUnderTest.getAllTalksMachinaPodcasts {
            actualResult = it
        }

        verify(mockPodcastDao).getAllEpisodes(TALKS_MACHINA)
        assertTrue(actualResult!!.containsAll(expectedResult))
    }

    @Test
    fun `getAllExandriaUnlimitedPodcasts returns all exandria unlimited podcast data`() = runBlocking {
        val expectedResult = createPodcastResult(EXPECTED_EXANDRIA_UNLIMITED_EPISODES)
        whenever(mockPodcastDao.getAllEpisodes(EXANDRIA_UNLIMITED)).thenReturn(expectedResult)

        var actualResult: List<Podcast>? = null
        systemUnderTest.getAllExandriaUnlimitedPocasts {
            actualResult = it
        }

        verify(mockPodcastDao).getAllEpisodes(EXANDRIA_UNLIMITED)
        assertTrue(actualResult!!.containsAll(expectedResult))
    }

    @Test
    fun `getAllVoxMachinaPodcasts syncs data with server and returns correct number of episodes if database does not have the right amount of data`(): Unit = runBlocking {
        whenever(mockPodcastDao.getAllEpisodes(VOX_MACHINA_CAMPAIGN)).thenReturn(createPodcastResult(5))
        whenever(mockRemoteRepo.getVoxMachinaEpisodes(any())).thenReturn(createPodcastResult(EXPECTED_VOX_MACHINA_EPISODES))

        var result: List<Podcast>? = null
        systemUnderTest.getAllVoxMachinaPodcasts {
            assertEquals(EXPECTED_VOX_MACHINA_EPISODES, it.size)
            result = it
        }

        verify(mockPodcastDao).insertEpisodes(result!!)
        verify(mockRemoteRepo).getVoxMachinaEpisodes(any())
    }

    @Test
    fun `getAllMightyNeinPodcasts syncs data with server and returns correct number of episodes if database does not have the right amount of data`(): Unit = runBlocking {
        whenever(mockPodcastDao.getAllEpisodes(MIGHTY_NEIN_CAMPAIGN)).thenReturn(createPodcastResult(5))
        whenever(mockRemoteRepo.getMightyNeinEpisodes(any())).thenReturn(createPodcastResult(EXPECTED_MIGHTY_NEIN_EPISODES))

        var result: List<Podcast>? = null
        systemUnderTest.getAllMightyNeinPodcasts {
            assertEquals(EXPECTED_MIGHTY_NEIN_EPISODES, it.size)
            result = it
        }

        verify(mockPodcastDao).insertEpisodes(result!!)
        verify(mockRemoteRepo).getMightyNeinEpisodes(any())
    }

    @Test
    fun `getAllExandriaUnlimitedPodcasts syncs data with server and returns correct number of episodes if database does not have the right amount of data`(): Unit = runBlocking {
        whenever(mockPodcastDao.getAllEpisodes(EXANDRIA_UNLIMITED)).thenReturn(createPodcastResult(5))
        whenever(mockRemoteRepo.getExandriaUnlimitedEpisodes(any())).thenReturn(createPodcastResult(EXPECTED_EXANDRIA_UNLIMITED_EPISODES))

        var result: List<Podcast>? = null
        systemUnderTest.getAllExandriaUnlimitedPocasts {
            assertEquals(EXPECTED_EXANDRIA_UNLIMITED_EPISODES, it.size)
            result = it
        }

        verify(mockPodcastDao).insertEpisodes(result!!)
        verify(mockRemoteRepo).getExandriaUnlimitedEpisodes(any())
    }

    @Test
    fun `getAllTalksMachinaPodcasts checks for new episodes`(): Unit = runBlocking {
        whenever(mockPodcastDao.getAllEpisodes(TALKS_MACHINA)).thenReturn(createPodcastResult(5))

        systemUnderTest.getAllTalksMachinaPodcasts {  }
        verify(mockRemoteRepo).checkForUpdates(eq(TALKS_MACHINA), any(), any())
    }

    @Test
    fun `getAllBetweenTheSheetsEpisodes returns all between the sheets episode data`(): Unit = runBlocking {
        val expectedResult = createPodcastResult(5)
        whenever(mockPodcastDao.getAllEpisodes(BETWEEN_THE_SHEETS)).thenReturn(expectedResult)

        var actualResult: List<Podcast>? = null
        systemUnderTest.getAllBetweenTheSheetsPodcasts {
            actualResult = it
        }

        verify(mockPodcastDao).getAllEpisodes(BETWEEN_THE_SHEETS)
        assertTrue(actualResult!!.containsAll(expectedResult))
    }

    @Test
    fun `getAllBetweenTheSheetsEpisodes checks for new episodes`(): Unit = runBlocking {
        whenever((mockPodcastDao.getAllEpisodes(BETWEEN_THE_SHEETS))).thenReturn(createPodcastResult(5))

        systemUnderTest.getAllBetweenTheSheetsPodcasts {  }
        verify(mockRemoteRepo).checkForUpdates(eq(BETWEEN_THE_SHEETS), any(), any())
    }

    private fun createPodcastResult(numberOfPodcasts: Int): List<Podcast> {
        val podcasts = mutableListOf<Podcast>()
        (1..numberOfPodcasts).forEach {
            val podcast = Podcast(
                    it.toString(),
                    "Title# $it",
                    "description $it",
                    it,
                    it.toLong()
            )

            podcasts.add(podcast)
        }

        return podcasts
    }

}