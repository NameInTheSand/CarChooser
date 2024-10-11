package com.ukrdroiddev.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ukrdroiddev.presentation.R
import com.ukrdroiddev.presentation.ui.theme.LocalDim

@Composable
fun SummaryScreen(
    onConfirmClicked: () -> Unit,
    onLeaveScreen: () -> Unit,
    selectedManufacturer: String,
    selectedModel: String,
    selectedYear: String
) {
    val showDialog = rememberSaveable { mutableStateOf(false) }

    BackHandler {
        showDialog.value = true
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = stringResource(R.string.lbl_exit_confirmation)) },
            text = { Text(text = stringResource(R.string.lbl_exit_confirmation_hint)) },
            confirmButton = {
                Button(
                    onClick = {
                        onLeaveScreen.invoke()
                        showDialog.value = false
                    }
                ) {
                    Text(stringResource(R.string.lbl_yes))
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog.value = false }
                ) {
                    Text(stringResource(R.string.lbl_cancel))
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.fmt_manufacturer, selectedManufacturer),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(R.string.fmt_model, selectedModel),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(R.string.fmt_year, selectedYear),
            style = MaterialTheme.typography.titleLarge
        )

        Button(
            onClick = {
                onConfirmClicked.invoke()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = LocalDim.current.spaceMedium)
        ) {
            Text(stringResource(R.string.lbl_confirm))
        }
    }
}