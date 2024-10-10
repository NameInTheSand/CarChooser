package com.ukrdroiddev.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.ukrdroiddev.domain.entities.ManufacturerUiEntity
import com.ukrdroiddev.presentation.R
import com.ukrdroiddev.presentation.ui.theme.LocalDim
import com.ukrdroiddev.presentation.ui.theme.LocalFontSize
import com.ukrdroiddev.presentation.ui_elements.list_items.ContentLoading
import com.ukrdroiddev.presentation.ui_elements.list_items.ErrorListItem
import com.ukrdroiddev.presentation.ui_elements.list_items.SelectableItemWithName
import com.ukrdroiddev.presentation.viewModels.ManufacturersViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun ManufacturersScreen(
    onNavigateClick: (ManufacturerUiEntity) -> Unit,
    prevSelectedItem: ManufacturerUiEntity? = null,
) {

    val viewModel = koinViewModel<ManufacturersViewModel>()

    val manufacturers = viewModel.manufacturersFlow.collectAsLazyPagingItems()
    var selectedItem by remember { mutableStateOf(prevSelectedItem) }

    when {
        manufacturers.itemSnapshotList.isEmpty() && manufacturers.loadState.refresh is LoadState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ContentLoading()
            }
        }

        manufacturers.itemSnapshotList.isEmpty() -> {
            EmptyScreen { manufacturers.retry() }
        }

        else -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = LocalDim.current.spaceMedium)
                ) {
                    //Won't save position after screen rotation according to https://issuetracker.google.com/issues/177245496
                    LazyColumn(
                        contentPadding = PaddingValues(
                            bottom = LocalDim.current.spaceMedium,
                            top = LocalDim.current.spaceSmall
                        ),
                        verticalArrangement = Arrangement.spacedBy(LocalDim.current.spaceSmall),
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = LocalDim.current.spaceSmall)
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        items(count = manufacturers.itemCount) { index ->
                            val manufacturer = manufacturers[index]
                            manufacturer?.let {
                                SelectableItemWithName(
                                    modifier = Modifier.fillParentMaxWidth(),
                                    name = it.name,
                                    isSelected = it == selectedItem,
                                    onClick = { selectedItem = it }
                                )
                            }
                        }

                        manufacturers.apply {
                            when {
                                loadState.refresh is LoadState.Loading -> {
                                    item { ContentLoading() }
                                }

                                loadState.append is LoadState.Loading -> {
                                    item { ContentLoading() }
                                }

                                loadState.hasError -> {
                                    item {
                                        ErrorListItem {
                                            manufacturers.retry()
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Button(
                        onClick = { onNavigateClick.invoke(selectedItem!!) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = selectedItem != null
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