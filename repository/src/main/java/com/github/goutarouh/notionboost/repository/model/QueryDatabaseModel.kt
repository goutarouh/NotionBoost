package com.github.goutarouh.notionboost.repository.model

import com.github.goutarouh.notionboost.data.api.queryDatabase.QueryDatabaseApiResponseModel
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

data class QueryDatabaseModel(
    val now: LocalDateTime,
    val databaseId: String,
    val dailyInfoList: List<DailyInfo>
) {

    fun convertToUserZone(
        userZoneId: ZoneId = ZoneId.systemDefault()
    ) : QueryDatabaseModel {
        return this.copy(
            dailyInfoList = this.dailyInfoList.map {
                it.copy(
                    createdTime = it.createdTime.withZoneSameInstant(userZoneId)
                )
            }
        )
    }

    /**
     * (ex) JANUARY -> 1
     * (ex) DECEMBER -> 12
     */
    fun filterByMonth(monthValue: Int) : QueryDatabaseModel {
        return this.copy(
            dailyInfoList = this.dailyInfoList.filter {
                it.createdTime.monthValue == monthValue
            }
        )
    }
}

data class DailyInfo(
    val createdTime: ZonedDateTime,
    val isDoneMap: Map<String, Boolean>,
)

fun QueryDatabaseApiResponseModel.toModel(
    now: LocalDateTime,
    databaseId: String,
): QueryDatabaseModel {
    return QueryDatabaseModel(
        now = now,
        databaseId = databaseId,
        dailyInfoList = this.results.map { result ->
            DailyInfo(
                createdTime = result.properties.createdTime.createdTime,
                isDoneMap = result.properties.checkBoxProperties.mapValues { it.value.checkbox }
            )
        }
    )
}