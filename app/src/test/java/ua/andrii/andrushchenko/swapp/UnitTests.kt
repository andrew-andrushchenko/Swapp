package ua.andrii.andrushchenko.swapp

import org.junit.Assert.assertEquals
import org.junit.Test
import ua.andrii.andrushchenko.swapp.model.Person

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTests {

    @Test
    fun person_image_path_is_correct() {
        val person = Person(
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "https://swapi.dev/api/people/22/"
        )

        assertEquals("file:///android_asset/people/22.jpg", person.personImagePath)
    }
}