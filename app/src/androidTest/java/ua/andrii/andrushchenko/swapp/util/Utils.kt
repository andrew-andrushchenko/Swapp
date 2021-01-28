package ua.andrii.andrushchenko.swapp.util

import ua.andrii.andrushchenko.swapp.model.Person

object Utils {

    fun generatePeople(amount: Int): List<Person> {
        val list = mutableListOf<Person>()
        for (i in 0..amount) {
            list += Person(
                "Person $i",
                "",
                "",
                "",
                "",
                "",
                "",
                "male",
                "",
                "https://swapi.dev/api/people/$i/"
            )
        }
        return list
    }
}