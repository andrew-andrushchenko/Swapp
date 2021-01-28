package ua.andrii.andrushchenko.swapp

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ua.andrii.andrushchenko.swapp.source.local.PeopleDao
import ua.andrii.andrushchenko.swapp.source.local.StarWarsDb
import ua.andrii.andrushchenko.swapp.util.Utils

@RunWith(AndroidJUnit4::class)
class StarWarsPersonEntityDeleteTest {

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
    fun deleteTest() = runBlocking {
        val personList = Utils.generatePeople(1)
        peopleDao.insertAll(personList)
        peopleDao.deleteAll()

        val pagingSource = peopleDao.peoplePagingSource()
        val actual = pagingSource.load(PagingSource.LoadParams.Refresh(1, 1, false))
        val listInDb = (actual as? PagingSource.LoadResult.Page)?.data

        Assert.assertEquals(0, listInDb?.size)
    }
}