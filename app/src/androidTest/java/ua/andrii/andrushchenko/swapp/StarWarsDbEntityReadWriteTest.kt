package ua.andrii.andrushchenko.swapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ua.andrii.andrushchenko.swapp.model.Person
import ua.andrii.andrushchenko.swapp.source.local.PeopleDao
import ua.andrii.andrushchenko.swapp.source.local.StarWarsDb

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class StarWarsDbEntityReadWriteTest {

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
        val person = Person(
            "Luke Skywalker",
            "172",
            "77",
            "blond",
            "fair",
            "blue",
            "19BBBY",
            "male",
            "https://swapi.dev/api/planets/1/",
            "https://swapi.dev/api/people/1/"
        )
        peopleDao.insertAll(listOf(person))
        val people = peopleDao.searchPeople("Luke")
        assertEquals(1, people.size)
        assertThat(people[0], equalTo(person))
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("ua.andrii.andrushchenko.swapp", appContext.packageName)
    }
}