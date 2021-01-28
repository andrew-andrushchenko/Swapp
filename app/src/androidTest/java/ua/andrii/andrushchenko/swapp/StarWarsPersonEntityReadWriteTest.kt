package ua.andrii.andrushchenko.swapp

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ua.andrii.andrushchenko.swapp.source.local.PeopleDao
import ua.andrii.andrushchenko.swapp.source.local.StarWarsDb
import ua.andrii.andrushchenko.swapp.util.Utils

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class StarWarsPersonEntityReadWriteTest {

    private lateinit var peopleDao: PeopleDao
    private lateinit var starWarsDb: StarWarsDb

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        starWarsDb = Room.inMemoryDatabaseBuilder(
            context, StarWarsDb::class.java
        ).build()
        peopleDao = starWarsDb.peopleDao()
    }

    @After
    fun closeDb() {
        starWarsDb.close()
    }

    @Test
    fun readWriteTest() = runBlocking {
        val peopleList = Utils.generatePeople(5)
        peopleDao.insertAll(peopleList)

        val pagingSource = peopleDao.peoplePagingSource()
        val actual = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 1, loadSize = 5, placeholdersEnabled = false
            )
        )
        val peopleListInDb = (actual as? PagingSource.LoadResult.Page)?.data

        assertEquals(peopleList, peopleListInDb)
    }
}