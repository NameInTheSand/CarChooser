package com.ukrdroiddev.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ukrdroiddev.presentation.R
import com.ukrdroiddev.presentation.ui.theme.LocalDim

@Composable
fun EmptyScreen(
    onRefresh: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(LocalDim.current.spaceMedium),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.lbl_error), //TODO add error type handling
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = LocalDim.current.spaceMedium)
            )
            Button(onClick = onRefresh) {
                Text(stringResource(R.string.lbl_retry))
            }
        }
    }
}