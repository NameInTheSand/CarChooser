package com.ukrdroiddev.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.ukrdroiddev.presentation.R
import com.ukrdroiddev.presentation.ui.theme.LocalDim
import com.ukrdroiddev.presentation.ui.theme.LocalFontSize
import com.ukrdroiddev.presentation.ui_elements.list_items.SelectableItemWithName

@Composable
fun ManufacturersScreen(
    onNavigateClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = LocalDim.current.spaceMedium)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = LocalDim.current.spaceMedium),
            verticalArrangement = Arrangement.spacedBy(LocalDim.current.spaceSmall),
            modifier = Modifier
                .padding(vertical = LocalDim.current.spaceSmall)
                .fillMaxWidth()
        ) {
            val items = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")

            items(items) { manufacturer ->
                SelectableItemWithName(
                    modifier = Modifier.fillParentMaxWidth(),
                    name = manufacturer,
                    isSelected = false,
                    onClick = onNavigateClick
                )
            }
        }

        Button(onClick = onNavigateClick) {
            Text(
                text = stringResource(R.string.lbl_select),
                fontSize = LocalFontSize.current.extraLarge.value.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ManufacturersScreenPreview() {
    ManufacturersScreen {}
}