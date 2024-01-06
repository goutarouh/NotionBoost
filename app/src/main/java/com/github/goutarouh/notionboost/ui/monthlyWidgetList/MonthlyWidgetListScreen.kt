package com.github.goutarouh.notionboost.ui.monthlyWidgetList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyWidgetListScreen(
    uiModel: MonthlyWidgetListUiModel,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Monthly widgets list",
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            )
        },
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            when {
                uiModel.isLoading -> {}
                uiModel.widgets.isEmpty() -> {
                    MonthlyWidgetListEmpty(
                        modifier = Modifier,
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = modifier.fillMaxSize(),
                    ) {
                        items(uiModel.widgets) {
                            MonthlyWidgetItem(
                                widget = it,
                                modifier = Modifier,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthlyWidgetListEmpty(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "This screen shows a list of your added app widgets.\n" +
                    "Let's enhance your device's home screen \n by adding 'Habit Tracker (Monthly)' widgets.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 12.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun MonthlyWidgetItem(
    widget: MonthlyWidgetModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.padding(16.dp)
    ) {
        Text(
            text = widget.title,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}