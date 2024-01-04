package com.github.goutarouh.notionboost.widget.configuration

import android.appwidget.AppWidgetManager
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.goutarouh.notionboost.usecase.MonthlyWidgetInitialUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonthlyWidgetConfigurationViewModel @Inject constructor(
    private val monthlyWidgetInitialUseCase: MonthlyWidgetInitialUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiModel = MutableStateFlow(MonthlyWidgetConfigurationUiModel(
        appWidgetId = savedStateHandle[AppWidgetManager.EXTRA_APPWIDGET_ID] ?: AppWidgetManager.INVALID_APPWIDGET_ID,
        updateInputDatabaseId = ::updateInputDatabaseId,
        createMonthlyWidget = ::createMonthlyWidget
    ))
    val uiModel = _uiModel.asStateFlow()

    private fun updateInputDatabaseId(inputDatabaseId: String) {
        viewModelScope.launch {
            _uiModel.update { it.copy(inputDatabaseId = inputDatabaseId) }
        }
    }

    private fun createMonthlyWidget(
        appWidgetId: Int,
        databaseId: String,
    ) {
        viewModelScope.launch {
            try {
                monthlyWidgetInitialUseCase.invoke(
                    databaseId = databaseId,
                    appWidgetId = appWidgetId,
                )
                _uiModel.update { it.copy(finishConfiguration = true) }
            } catch (e: Exception) {
                _uiModel.update { it.copy(finishConfiguration = false) }
            }
        }
    }

}