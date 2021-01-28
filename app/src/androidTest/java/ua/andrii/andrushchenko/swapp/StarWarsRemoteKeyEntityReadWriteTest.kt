package ua.andrii.andrushchenko.swapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ua.andrii.andrushchenko.swapp.model.RemoteKey
import ua.andrii.andrushchenko.swapp.source.local.RemoteKeyDao
import ua.andrii.andrushchenko.swapp.source.local.StarWarsDb

@RunWith(AndroidJUnit4::class)
class StarWarsRemoteKeyEntityReadWriteTest {

    private lateinit var remoteKeyDao: RemoteKeyDao
    private lateinit var starWarsDb: StarWarsDb

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        starWarsDb = Room.inMemoryDatabaseBuilder(
            context, StarWarsDb::class.java
        ).build()
        remoteKeyDao = starWarsDb.remoteKeyDao()
    }

    @After
    fun closeDb() {
        starWarsDb.close()
    }

    @Test
    fun readWriteTest() = runBlocking {
        val remoteKey = RemoteKey("label1", nextKey = 2, prevKey = null)
        val remoteKeyList = listOf(remoteKey)
        remoteKeyDao.insertAll(remoteKeyList)
        val remoteKeyInDb = remoteKeyDao.remoteKeyByLabel("label1")

        assertEquals(remoteKey, remoteKeyInDb)

    }
}