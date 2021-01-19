package ua.andrii.andrushchenko.swapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.andrii.andrushchenko.swapp.source.remote.StarWarsApi
import ua.andrii.andrushchenko.swapp.source.local.StarWarsDb
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(StarWarsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideStarWarsApi(retrofit: Retrofit): StarWarsApi =
        retrofit.create(StarWarsApi::class.java)

    @Provides
    @Singleton
    fun provideStarWarsDatabase(@ApplicationContext app: Context): StarWarsDb =
        Room.databaseBuilder(
            app,
            StarWarsDb::class.java,
            "StarWarsDb"
        ).fallbackToDestructiveMigration().build()
}