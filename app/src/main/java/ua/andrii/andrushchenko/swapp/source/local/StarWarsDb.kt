package ua.andrii.andrushchenko.swapp.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.andrii.andrushchenko.swapp.model.Person
import ua.andrii.andrushchenko.swapp.model.RemoteKey

@Database(
    entities = [Person::class, RemoteKey::class],
    version = 1,
    exportSchema = false
)
abstract class StarWarsDb : RoomDatabase() {

    abstract fun peopleDao(): PeopleDao
    abstract fun remoteKeyDao(): RemoteKeyDao

}