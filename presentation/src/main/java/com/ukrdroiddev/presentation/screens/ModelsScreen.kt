package com.ukrdroiddev.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.ukrdroiddev.domain.entities.ModelUiEntity
import com.ukrdroiddev.presentation.R
import com.ukrdroiddev.presentation.ui.theme.LocalDim
import com.ukrdroiddev.presentation.ui.theme.LocalFontSize
import com.ukrdroiddev.presentation.ui_elements.list_items.ContentLoading
import com.ukrdroiddev.presentation.ui_elements.list_items.SelectableItemWithName
import com.ukrdroiddev.presentation.viewModels.ModelsScreenState
import com.ukrdroiddev.presentation.viewModels.ModelsViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun ModelsScreen(
    onNavigateClick: (ModelUiEntity) -> Unit,
    onLeaveScreen: () -> Unit,
    prevSelectedItem: ModelUiEntity? = null,
) {
    val viewModel = koinViewModel<ModelsViewModel>()
    val screenState = viewModel.screenState.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            onLeaveScreen.invoke()
        }
    }

    when (screenState.value) {
        ModelsScreenState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ContentLoading()
            }
        }

        ModelsScreenState.Error -> {
            EmptyScreen {
                viewModel.onRefresh()
            }
        }

        is ModelsScreenState.Content -> {
            val models = (screenState.value as ModelsScreenState.Content).models
            var selectedItem by remember { mutableStateOf(prevSelectedItem) }
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

                    Spacer(modifier = Modifier.height(LocalDim.current.spaceExtraSmall))

                    ClearableTextField(
                        value = viewModel.searchQuery,
                        hint = stringResource(R.string.lbl_model_search_hint),
                        onValueChange = {
                            viewModel.onSearchQueryChanged(it)
                        }
                    )

                    if (models.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                stringResource(R.string.lbl_no_models_found),
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
                            items(count = models.size) { index ->
                                val model = models[index]
                                SelectableItemWithName(
                                    modifier = Modifier.fillParentMaxWidth(),
                                    name = model.name,
                                    isSelected = model == selectedItem,
                                    onClick = { selectedItem = model }
                                )
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
}

@Composable
private fun ClearableTextField(
    value: String,
    hint: String,
    onValueChange: (String) -> Unit,
    label: String = stringResource(R.string.lbl_model_search_label)
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = hint) },
        label = { Text(text = label) },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }
            }
        },
        isError = false,
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black),
        singleLine = true,
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        modifier = Modifier.fillMaxWidth()
    )
}