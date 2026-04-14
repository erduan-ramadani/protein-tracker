package com.ercoding.proteintracker.presentation.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ercoding.proteintracker.domain.ProteinEntry
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ProteinDayPager(
    pagerState: PagerState,
    dailyEntriesByDate: Map<LocalDate, List<ProteinEntry>>,
    last7Days: List<LocalDate>,
    onDismiss: (ProteinEntry) -> Unit
) {
    val listState = rememberLazyListState()

    HorizontalPager(pagerState) { page ->
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val dateKey = last7Days[page]
            val today = LocalDate.now()
            val yesterday = today.minusDays(1)
            val dateLabel = when (dateKey) {
                today -> "Heute"
                yesterday -> "Gestern"
                else -> dateKey.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = dateLabel)

            if (last7Days.isEmpty()) {
                // leere Ansicht anzeigen
            } else {
                val entriesForDay = dailyEntriesByDate[dateKey] ?: emptyList()
                LazyColumn(
                    state = listState,
                ) {
                    items(entriesForDay, key = { it.id }) { entry ->
                        ProteinEntryItem(
                            entry,
                            onDismiss = { onDismiss(entry) }
                        )
                    }
                }
            }
        }
    }
}