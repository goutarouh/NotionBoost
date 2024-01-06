package com.github.goutarouh.notionboostrepository.repository

import com.github.goutarouh.notionboost.data.datastore.DataStoreKey
import com.github.goutarouh.notionboost.data.glance.GlanceApi
import com.github.goutarouh.notionboostrepository.repository.model.MonthlyWidgetModel
import com.github.goutarouh.notionboostrepository.repository.model.toLocalModel
import com.github.goutarouh.notionboostrepository.repository.model.toModel
import javax.inject.Inject


class GlanceRepository @Inject constructor(
    private val glanceApi: GlanceApi,
) {

    suspend fun getMonthlyWidgetModel(appWidgetId: Int) : MonthlyWidgetModel? {
        return glanceApi.getMonthlyWidgetStateByAppWidgetId(appWidgetId)?.toModel()
    }

    suspend fun updateMonthlyWidgetByWidgetIds(
        appWidgetIds: List<Int>,
        monthlyWidgetModel: MonthlyWidgetModel,
        afterStateUpdate: suspend (appWidgetId: Int) -> Unit,
    ) {
        val monthlyWidgetLocalModel = monthlyWidgetModel.toLocalModel()
        glanceApi.updateMonthlyWidgetsStateByWidgetIds(appWidgetIds, monthlyWidgetLocalModel, afterStateUpdate)
    }

}