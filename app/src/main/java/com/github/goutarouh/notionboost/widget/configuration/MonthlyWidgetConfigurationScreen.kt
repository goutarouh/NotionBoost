package com.github.goutarouh.notionboost.widget.configuration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.github.goutarouh.notionboost.widget.configuration.MonthlyWidgetConfigurationUiModel.ConfigurationResult

const val MONTHLY_WIDGET_CONFIGURATION_SCREEN_TAG = "MonthlyWidgetConfigurationScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyWidgetConfigurationScreen(
    uiModel: MonthlyWidgetConfigurationUiModel,
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState) },
        modifier = modifier.testTag(MONTHLY_WIDGET_CONFIGURATION_SCREEN_TAG)
    ) { paddingValues ->
        Box(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            MonthlyWidgetConfigurationContent(
                uiModel = uiModel,
                modifier = Modifier,
            )
            when (uiModel.configurationResult) {
                is ConfigurationResult.Loading -> Loading()
                is ConfigurationResult.Failure -> {}
                else -> {}
            }
        }
    }
}

@Composable
private fun Loading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun MonthlyWidgetConfigurationContent(
    uiModel: MonthlyWidgetConfigurationUiModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Please enter the Notion Database ID.",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 12.dp),
        )
        Text(
            text = "Database ID is a 32-character alphanumeric string that can be found in the URL of the database.",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(vertical = 12.dp),
        )
        OutlinedTextField(
            value = uiModel.inputDatabaseId,
            onValueChange = uiModel.updateInputDatabaseId,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            singleLine = true,
            placeholder = { Text(text = "Database ID") },
        )
        val keyboardController = LocalSoftwareKeyboardController.current
        OutlinedButton(
            onClick = {
                keyboardController?.hide()
                uiModel.createMonthlyWidget(uiModel.appWidgetId, uiModel.inputDatabaseId)
            },
            enabled = uiModel.saveButtonEnabled,
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Text(text = "Create Widget")
        }
    }
}