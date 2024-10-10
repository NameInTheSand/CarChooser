package com.ukrdroiddev.presentation.ui_elements.list_items

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ukrdroiddev.presentation.R

@Composable
fun ErrorListItem(
    onRetryClick: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(text = stringResource(R.string.lbl_error), modifier = Modifier.weight(2f))

        Spacer(modifier = Modifier.weight(1f))

        Button(onClick = onRetryClick, modifier = Modifier.weight(1f)) {
            Text(stringResource(R.string.lbl_retry))
        }
    }
}