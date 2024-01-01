package com.github.goutarouh.notionboost.widget

import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.github.goutarouh.notionboost.repository.QueryDatabaseModel

@Composable
fun MonthlyWidgetContent(
    monthlyInfo: QueryDatabaseModel,
    modifier: GlanceModifier = GlanceModifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .clickable(
                actionStartActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        "https://www.notion.so/2024-Habit-Tracker-b3ca62aaf06443aa92758511490cf6ce".toUri()
                    )
                )
            ),
    ) {
        val countDays = monthlyInfo.dailyInfo.size
        val countDoneReading = monthlyInfo.dailyInfo.filter { it.doneReading }.size
        val countDoneEnglishLearning = monthlyInfo.dailyInfo.filter { it.doneEnglishLearning }.size

        if (countDays == 0) {
            Text(text = "No Data")
        } else {
            AccomplishmentTrackRow(
                title = "Reading",
                allDayCount = countDays,
                doneCount = countDoneReading,
                modifier = GlanceModifier.padding(vertical = 2.dp)
            )
            AccomplishmentTrackRow(
                title = "English",
                allDayCount = countDays,
                doneCount = countDoneEnglishLearning,
                modifier = GlanceModifier.padding(vertical = 2.dp)
            )
            AccomplishmentTrackRow(
                title = "Workout",
                allDayCount = 15,
                doneCount = 10,
                modifier = GlanceModifier.padding(vertical = 2.dp)
            )
            AccomplishmentTrackRow(
                title = "Bed 24",
                allDayCount = 15,
                doneCount = 2,
                modifier = GlanceModifier.padding(vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun AccomplishmentTrackRow(
    title: String,
    allDayCount: Int,
    doneCount: Int,
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
                textAlign = TextAlign.End
            ),
            maxLines = 1
        )
        Spacer(
            modifier = GlanceModifier.width(8.dp)
        )
        AccomplishmentTrackBar(
            allDayCount = allDayCount,
            doneCount = doneCount,
            modifier = GlanceModifier.defaultWeight()
        )
    }
}


@Composable
private fun AccomplishmentTrackBar(
    allDayCount: Int,
    doneCount: Int,
    modifier: GlanceModifier
) {

   val progress = doneCount.toFloat() / allDayCount.toFloat()
    LinearProgressIndicator(
        progress = progress,
        modifier = modifier,
    )

}



