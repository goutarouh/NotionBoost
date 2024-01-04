package com.github.goutarouh.notionboost.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit

interface DataStoreApi {

    suspend fun setNotionApiKey(notionApiKey: String)

    suspend fun getNotionApiKey() : String

}


class DataStoreApiImpl(
    private val dataStore: DataStore<Preferences>
) : DataStoreApi {

    private val prefFlow = dataStore.data

    override suspend fun setNotionApiKey(notionApiKey: String) {
        dataStore.edit { settings ->
            settings[DataStoreKey.NOTION_API_KEY] = notionApiKey
        }
    }

    override suspend fun getNotionApiKey(): String {
        return prefFlow.getDataStoreValue(DataStoreKey.NOTION_API_KEY)
    }


}