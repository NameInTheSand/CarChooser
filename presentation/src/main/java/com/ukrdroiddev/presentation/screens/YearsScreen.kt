package com.ukrdroiddev.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.ukrdroiddev.domain.entities.YearUiEntity
import com.ukrdroiddev.presentation.R
import com.ukrdroiddev.presentation.ui.theme.LocalDim
import com.ukrdroiddev.presentation.ui.theme.LocalFontSize
import com.ukrdroiddev.presentation.ui_elements.list_items.ContentLoading
import com.ukrdroiddev.presentation.ui_elements.list_items.SelectableItemWithName
import com.ukrdroiddev.presentation.viewModels.YearsScreenState
import com.ukrdroiddev.presentation.viewModels.YearsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun YearsScreen(
    onNavigateClick: (YearUiEntity) -> Unit,
    onLeaveScreen: () -> Unit,
    prevSelectedItem: YearUiEntity? = null,
) {
    val viewModel = koinViewModel<YearsViewModel>()
    val screenState = viewModel.screenState.collectAsState()

    BackHandler {
        onLeaveScreen.invoke()
    }

    when (screenState.value) {
        YearsScreenState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ContentLoading()
            }
        }

        YearsScreenState.Error -> {
            EmptyScreen {
                viewModel.onRefresh()
            }
        }

        is YearsScreenState.Content -> {
            val years = (screenState.value as YearsScreenState.Content).years
            var selectedItem by rememberSaveable(stateSaver = YearSaver.yearSaver) {
                mutableStateOf(prevSelectedItem)
            }
            val listState = rememberLazyListState()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = LocalDim.current.spaceMedium)
                ) {
                    Row {
                        Card(
                            modifier = Modifier.padding(bottom = LocalDim.current.spaceXXSmall),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
                        ) {
                            Text(
                                text = viewModel.selectedManufacturerName,
                                modifier = Modifier.padding(LocalDim.current.spaceExtraSmall),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Spacer(modifier = Modifier.width(LocalDim.current.spaceSmall))

                        Card(
                            modifier = Modifier.padding(bottom = LocalDim.current.spaceXXSmall),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
                        ) {
                            Text(
                                text = viewModel.selectedModelName,
                                modifier = Modifier.padding(LocalDim.current.spaceExtraSmall),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }


                    Spacer(modifier = Modifier.height(LocalDim.current.spaceExtraSmall))


                    if (years.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                stringResource(R.string.lbl_no_years_found),
                                textAlign = TextAlign.Center
                            )
                        }

                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(
                                bottom = LocalDim.current.spaceMedium,
                                top = LocalDim.current.spaceSmall
                            ),
                            state = listState,
                            verticalArrangement = Arrangement.spacedBy(LocalDim.current.spaceSmall),
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = LocalDim.current.spaceSmall)
                                .fillMaxWidth()
                                .fillMaxHeight()
                        ) {
                            items(count = years.size) { index ->
                                val year = years[index]
                                SelectableItemWithName(
                                    modifier = Modifier.fillParentMaxWidth(),
                                    name = year.year,
                                    isSelected = year == selectedItem,
                                    onClick = { selectedItem = year }
                                )
                            }
                        }

                        Button(
                            onClick = { onNavigateClick.invoke(selectedItem!!) },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = selectedItem?.year.isNullOrEmpty().not()
                        ) {
                            Text(
                                text = stringResource(R.string.lbl_select),
                                fontSize = LocalFontSize.current.extraLarge.value.sp,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }

    }
}

private object YearSaver {
    val yearSaver = Saver<YearUiEntity?, Map<String, String>>(
        save = { manufacturer ->
            manufacturer?.let { mapOf("year" to it.year) }
        },
        restore = { map ->
            YearUiEntity(map["year"] ?: "")
        }
    )
}
