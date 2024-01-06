package com.github.goutarouh.notionboostrepository.repository.model

import com.github.goutarouh.notionboost.data.glance.monthly.MonthlyWidgetLocalModel
import com.github.goutarouh.notionboostrepository.repository.QueryDatabaseModel
import com.github.goutarouh.notionboostrepository.repository.RetrieveDatabaseModel
import com.github.goutarouh.notionboostrepository.repository.util.DateFormat
import com.github.goutarouh.notionboostrepository.repository.util.getFirstDayOfThisMonth
import com.github.goutarouh.notionboostrepository.repository.util.getLastDayOfThisMonth

class MonthlyWidgetModel(
    val monthName: String,
    val title: String,
    val startDate: String,
    val endDate: String,
    val lastUpdatedTime: String,
    val databaseId: String,
    val url: String,
    val mapProgress: Map<String, Float> = mapOf(),
) {
    fun calculateProgress(progress: Float): Int {
        return (progress * 100).toInt()
    }
}

fun MonthlyWidgetModel.toLocalModel() : MonthlyWidgetLocalModel {
    return MonthlyWidgetLocalModel(
        monthName = this.monthName,
        title = this.title,
        startDate = this.startDate,
        endDate = this.endDate,
        lastUpdatedTime = this.lastUpdatedTime,
        databaseId = this.databaseId,
        url = this.url,
        mapProgress = this.mapProgress
    )
}

fun MonthlyWidgetLocalModel.toModel() : MonthlyWidgetModel {
    return MonthlyWidgetModel(
        monthName = this.monthName,
        title = this.title,
        startDate = this.startDate,
        endDate = this.endDate,
        lastUpdatedTime = this.lastUpdatedTime,
        databaseId = this.databaseId,
        url = this.url,
        mapProgress = this.mapProgress
    )
}

fun createMonthlyWidgetModel(
    retrieveDatabaseModel: RetrieveDatabaseModel,
    queryDatabaseModel: QueryDatabaseModel,
) : MonthlyWidgetModel {

    val now = queryDatabaseModel.now

    val startDate = now.getFirstDayOfThisMonth()
    val endDate = now.getLastDayOfThisMonth()

    val mapProgress = calculateItemProgressMap(
        queryDatabaseModel.dailyInfoList.map { it.isDoneMap }
    )

    return MonthlyWidgetModel(
        monthName = now.format(DateFormat.MONTH_NAME),
        title = retrieveDatabaseModel.title,
        startDate = startDate.format(DateFormat.YYYY_MM_DD),
        endDate = endDate.format(DateFormat.YYYY_MM_DD),
        lastUpdatedTime = now.format(DateFormat.MM_DD_HH_MM),
        databaseId = queryDatabaseModel.databaseId,
        url = retrieveDatabaseModel.url,
        mapProgress = mapProgress
    )
}

private fun calculateItemProgressMap(isDoneMapList: List<Map<String, Boolean>>) : Map<String, Float> {

    if (isDoneMapList.isEmpty()) {
        return mapOf()
    }

    val resultMap = mutableMapOf<String, Int>()
    val totalProperties = isDoneMapList.size.toFloat()

    for (propertyMap in isDoneMapList) {
        for ((property, value) in propertyMap) {
            val currentCount = resultMap.getOrPut(property) { 0 }
            if (value) {
                resultMap[property] = currentCount + 1
            }
        }
    }

    return resultMap.mapValues { (_, count) -> count / totalProperties }
}