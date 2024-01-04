package com.github.goutarouh.notionboost.widget

import com.github.goutarouh.notionboost.repository.QueryDatabaseModel
import com.github.goutarouh.notionboost.util.DateFormat
import com.github.goutarouh.notionboost.util.getFirstDayOfThisMonth
import com.github.goutarouh.notionboost.util.getLastDayOfThisMonth

data class MonthlyReportModel(
    val monthlyReport: MonthlyReport
)

data class MonthlyReport (
    val monthName: String,
    val startDate: String,
    val endDate: String,
    val lastUpdatedTime: String,
    val mapProgress: Map<String, Float> = mapOf(),
) {
    fun calculateProgress(progress: Float): Int {
        return (progress * 100).toInt()
    }
}

fun QueryDatabaseModel.toMonthlyReportModel(): MonthlyReportModel {

    val startDate = now.getFirstDayOfThisMonth()
    val endDate = now.getLastDayOfThisMonth()

    return if (dailyInfoList.isEmpty()) {
        val monthlyReport = MonthlyReport(
            monthName = now.format(DateFormat.MONTH_NAME),
            startDate = startDate.format(DateFormat.YYYY_MM_DD),
            endDate = endDate.format(DateFormat.YYYY_MM_DD),
            lastUpdatedTime = now.format(DateFormat.MM_DD_HH_MM),
        )
        MonthlyReportModel(monthlyReport)
    } else {
        val mapProgress = calculateItemProgressMap(
            dailyInfoList.map { it.isDoneMap }
        )
        val monthlyReport = MonthlyReport(
            monthName = now.format(DateFormat.MONTH_NAME),
            startDate = startDate.format(DateFormat.YYYY_MM_DD),
            endDate = endDate.format(DateFormat.YYYY_MM_DD),
            lastUpdatedTime = now.format(DateFormat.MM_DD_HH_MM),
            mapProgress = mapProgress
        )
        MonthlyReportModel(monthlyReport)
    }
}

private fun calculateItemProgressMap(isDoneMapList: List<Map<String, Boolean>>) : Map<String, Float> {
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