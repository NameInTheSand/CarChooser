package com.ukrdroiddev.presentation.ui_elements.list_items

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ukrdroiddev.presentation.ui.theme.LocalDim
import com.ukrdroiddev.presentation.ui.theme.LocalFontSize

@Composable
fun SelectableItemWithName(
    modifier: Modifier = Modifier,
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        shadowElevation = LocalDim.current.spaceExtraSmall,
        shape = RoundedCornerShape(LocalDim.current.spaceMedium)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = isSelected, onClick = onClick)
            Spacer(modifier = Modifier.width(LocalDim.current.spaceSmall))
            Text(text = name, fontSize = LocalFontSize.current.medium)
        }
    }
}