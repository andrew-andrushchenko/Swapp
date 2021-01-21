package ua.andrii.andrushchenko.swapp.source.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ua.andrii.andrushchenko.swapp.model.Person

@Dao
interface PeopleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(people: List<Person>)

    @Query("SELECT * FROM people_table")
    fun peoplePagingSource(): PagingSource<Int, Person>

    @Query("SELECT * FROM people_table WHERE name LIKE :name")
    fun searchPeople(name: String): List<Person>

    @Query("DELETE FROM people_table")
    suspend fun deleteAll()

}