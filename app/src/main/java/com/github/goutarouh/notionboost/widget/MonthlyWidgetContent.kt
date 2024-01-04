package com.github.goutarouh.notionboost.widget

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

@Composable
fun MonthlyWidgetContent(
    monthlyWidgetUiState: MonthlyWidgetUiState,
    modifier: GlanceModifier = GlanceModifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WidgetColor.widgetBackground)
            .clickable(
                actionStartActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        "https://www.notion.so/2024-Habit-Tracker-b3ca62aaf06443aa92758511490cf6ce".toUri()
                    )
                )
            ),
    ) {

        when (monthlyWidgetUiState) {
            is MonthlyWidgetUiState.Loading -> {
                NoData()
            }
            is MonthlyWidgetUiState.NoData -> {
                NoData()
            }
            is MonthlyWidgetUiState.Success -> {
                Success(
                    monthlyReport = monthlyWidgetUiState.monthlyReport,
                    modifier = GlanceModifier
                )
            }
        }
    }
}

@Composable
private fun NoData() {
    Box(
        modifier = GlanceModifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "No Data")
    }
}

@Composable
private fun Success(
    monthlyReport: MonthlyReport,
    modifier: GlanceModifier = GlanceModifier,
) {

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(
            modifier = GlanceModifier.height(8.dp),
        )
        Text(
            text = "${monthlyReport.monthName} Habit Tracker",
            style = TextStyle(
                fontSize = WidgetText.LargeTextSize,
                fontWeight = FontWeight.Bold,
            ),
        )
        Spacer(
            modifier = GlanceModifier.height(2.dp),
        )
        Text(
            text = "${monthlyReport.startDate} - ${monthlyReport.endDate}",
            style = TextStyle(
                fontSize = WidgetText.SmallTextSize,
            ),
        )
        Spacer(
            modifier = GlanceModifier.height(8.dp),
        )
        Column(
            modifier = GlanceModifier
                .fillMaxWidth()
                .defaultWeight(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            monthlyReport.mapProgress.forEach { mapProgress ->
                AccomplishmentTrackRow(
                    title = mapProgress.key,
                    progress = mapProgress.value,
                    progressPercent = monthlyReport.calculateProgress(mapProgress.value),
                    modifier = GlanceModifier.padding(vertical = 3.dp),
                )
            }
        }
        Spacer(
            modifier = GlanceModifier.height(8.dp),
        )
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = GlanceModifier.defaultWeight())
            Text(
                text = "Last Updated at ${monthlyReport.lastUpdatedTime}",
                style = TextStyle(
                    fontSize = WidgetText.SmallTextSize,
                ),
            )
            Spacer(modifier = GlanceModifier.width(8.dp))
        }
    }
}

@Composable
private fun AccomplishmentTrackRow(
    title: String,
    progress: Float,
    progressPercent: Int,
    modifier: GlanceModifier = GlanceModifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            modifier = GlanceModifier.width(80.dp),
            style = TextStyle(
                textAlign = TextAlign.End,
                fontSize = WidgetText.MediumTextSize,
            ),
            maxLines = 1,
        )
        Spacer(
            modifier = GlanceModifier.width(12.dp),
        )
        Box(
            modifier = GlanceModifier.defaultWeight(),
            contentAlignment = Alignment.Center
        ) {
            AccomplishmentTrackBar(
                progress = progress,
                modifier = GlanceModifier.fillMaxWidth(),
            )

            Text(
                text = "$progressPercent%",
                modifier = GlanceModifier.fillMaxWidth().padding(end = 8.dp),
                style = TextStyle(
                    textAlign = TextAlign.End,
                    fontSize = WidgetText.SmallTextSize,
                ),
            )
        }
    }
}


@Composable
private fun AccomplishmentTrackBar(
    progress: Float,
    modifier: GlanceModifier,
) {

    val color = when {
        (progress < 0.4) -> WidgetColor.progressLow
        (progress < 0.8) -> WidgetColor.progressMedium
        else -> WidgetColor.progressHigh
    }
    LinearProgressIndicator(
        progress = progress,
        modifier = modifier,
        color = ColorProvider(color),
        backgroundColor = ColorProvider(WidgetColor.progressBackground)
    )

}


