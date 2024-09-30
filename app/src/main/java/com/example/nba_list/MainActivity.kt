package com.example.nba_list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.nba_list.ui.composables.TopBar
import com.example.nba_list.ui.screens.PlayerListScreen
import com.example.nba_list.ui.theme.NBA_ListTheme
import com.example.nba_list.vm.TopBarViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val topBarViewModel: TopBarViewModel = koinViewModel()

            NBA_ListTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopBar(uiState = topBarViewModel.uiState.value)
                    }
                ) { innerPadding ->
                    Box(Modifier.padding(innerPadding)){
                        PlayerListScreen(topBarViewModel = topBarViewModel)
                    }
                }
            }
        }
    }
}