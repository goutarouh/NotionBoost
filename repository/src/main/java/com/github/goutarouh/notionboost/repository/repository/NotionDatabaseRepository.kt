package com.github.goutarouh.notionboost.repository.repository

import com.github.goutarouh.notionboost.data.api.NotionRemoteApi
import com.github.goutarouh.notionboost.data.api.queryDatabase.QueryDatabaseApiAndRequestModel
import com.github.goutarouh.notionboost.data.api.queryDatabase.QueryDatabaseApiAndRequestModel.Filter
import com.github.goutarouh.notionboost.data.api.queryDatabase.QueryDatabaseApiAndRequestModel.And
import com.github.goutarouh.notionboost.data.api.queryDatabase.QueryDatabaseApiAndRequestModel.Date
import com.github.goutarouh.notionboost.data.datastore.DataStoreApi
import com.github.goutarouh.notionboost.repository.model.QueryDatabaseModel
import com.github.goutarouh.notionboost.repository.model.RetrieveDatabaseModel
import com.github.goutarouh.notionboost.repository.getDataStoreCall
import com.github.goutarouh.notionboost.repository.safeApiCall
import com.github.goutarouh.notionboost.repository.model.toModel
import java.time.LocalDateTime


class NotionDatabaseRepository(
    private val notionRemoteApi: NotionRemoteApi,
    private val dataStoreApi: DataStoreApi,
) {

    suspend fun saveNotionApiKey(apiKey: String) {
        dataStoreApi.setNotionApiKey(apiKey)
    }

    suspend fun getNotionApiKey(): String {
        return getDataStoreCall {
            dataStoreApi.getNotionApiKey()
        }
    }

    suspend fun saveMonthlyWidgetConfiguration(config: Map<Int, String>) {
        dataStoreApi.saveMonthlyWidgetConfiguration(config)
    }

    suspend fun getMonthlyWidgetConfiguration(): Map<Int, String> {
        return getDataStoreCall {
            dataStoreApi.getMonthlyWidgetConfiguration()
        }
    }

    suspend fun removeMonthlyWidgetConfiguration(appWidgetIds: List<Int>) {
        dataStoreApi.removeMonthlyWidgetConfiguration(appWidgetIds)
    }

    fun monthlyWidgetConfigurationFlow() = dataStoreApi.monthlyWidgetConfigurationFlow()

    suspend fun queryDatabase(
        databaseId: String,
        now: LocalDateTime,
        inclusiveStartDate: LocalDateTime,
        inclusiveEndDate: LocalDateTime
    ) : QueryDatabaseModel {

        val queryDatabaseApiAndRequestModel = QueryDatabaseApiAndRequestModel(
            filter = Filter(
                and = listOf(
                    And(date = Date.OnOrAfter(onOrAfter = inclusiveStartDate.toString())),
                    And(date = Date.OnOrBefore(onOrBefore = inclusiveEndDate.toString())),
                )
            )
        )

        val authorization = "Bearer ${getNotionApiKey()}"

        return safeApiCall {
            notionRemoteApi.queryDatabase(
                authorization = authorization,
                databaseId = databaseId,
                queryDatabaseApiAndRequestModel = queryDatabaseApiAndRequestModel,
            )
        }.toModel(
            now = now,
            databaseId = databaseId
        )
    }

    suspend fun retrieveDatabase(
        databaseId: String,
    ) : RetrieveDatabaseModel {
        val authorization = "Bearer ${getNotionApiKey()}"
        return safeApiCall {
            notionRemoteApi.retrieveDatabase(
                authorization = authorization,
                databaseId = databaseId,
            )
        }.toModel()
    }
}



