package com.example.nba_list.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    showSheet: Boolean,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    coroScope: CoroutineScope = rememberCoroutineScope(),
    content: @Composable () -> Unit
) {

    LaunchedEffect(showSheet) {
        coroScope.launch {
            if(showSheet){
                sheetState.expand()
            }
        }
    }

    if(showSheet){
        ModalBottomSheet(
            modifier = modifier,
            sheetState = sheetState,
            onDismissRequest = onDismiss
        ) {
            Column(
                Modifier.padding(16.dp)
            ){
                content()
            }
        }
    }
}
