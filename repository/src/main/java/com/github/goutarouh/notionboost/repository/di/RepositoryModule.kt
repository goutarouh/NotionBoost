package com.github.goutarouh.notionboost.repository.di

import com.github.goutarouh.notionboost.data.api.NotionRemoteApi
import com.github.goutarouh.notionboost.data.datastore.DataStoreApi
import com.github.goutarouh.notionboost.data.glance.GlanceApi
import com.github.goutarouh.notionboost.repository.repository.GlanceRepository
import com.github.goutarouh.notionboost.repository.repository.NotionDatabaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNotionDatabaseRepository(
        notionRemoteApi: NotionRemoteApi,
        dataStoreApi: DataStoreApi,
    ): NotionDatabaseRepository {
        return NotionDatabaseRepository(
            notionRemoteApi = notionRemoteApi,
            dataStoreApi = dataStoreApi,
        )
    }

    @Provides
    @Singleton
    fun provideGlanceRepository(
        glanceApi: GlanceApi,
    ): GlanceRepository {
        return GlanceRepository(
            glanceApi = glanceApi,
        )
    }

}