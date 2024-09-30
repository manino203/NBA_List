package com.example.nba_list.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nba_list.R
import com.example.nba_list.data.models.Player
import com.example.nba_list.data.models.Team
import com.example.nba_list.ui.composables.PlayerSheet
import com.example.nba_list.vm.PlayerListUiState
import com.example.nba_list.vm.PlayerListViewModel
import com.example.nba_list.vm.TopBarViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlayerListScreen(
    topBarViewModel: TopBarViewModel,
) {
    val vm: PlayerListViewModel = koinViewModel()
    val uiState = vm.uiState.collectAsState()
    val title = stringResource(id = R.string.app_name)
    LaunchedEffect(title) {
        topBarViewModel.setTitle(title)
    }

    LaunchedEffect(uiState.value.loading) {
        topBarViewModel.setLoading(vm.uiState.value.loading)
    }

    LaunchedEffect(Unit) {
        vm.collectChanges()
        Log.d("loadPlayersVm", "wat?")
        vm.loadPlayers()
    }

    PlayerListScreenContent(
        uiState = uiState.value,
        {
            vm.clearError()
        }
    ) {
        vm.loadPlayers()
    }

}

@Composable
private fun PlayerListScreenContent(
    uiState: PlayerListUiState,
    clearError: () -> Unit,
    actionLoadMore: () -> Unit
){
    val listState = rememberLazyListState()

    val snackbarHostState = remember{ SnackbarHostState() }

    val errorMessage = uiState.error?.let { stringResource(id = it) }
    val snackbarActionLabel = stringResource(id = R.string.retry)

    LaunchedEffect(uiState.error) {
        uiState.error?.let{
            val result = snackbarHostState.showSnackbar(
                errorMessage!!,
                snackbarActionLabel,
                duration = SnackbarDuration.Long
            )
            when(result){
                SnackbarResult.ActionPerformed -> {
                    actionLoadMore()
                    clearError()
                }
                SnackbarResult.Dismissed -> clearError()
            }
        }
    }

    LaunchedEffect(uiState.loading) {
        Log.d("loading", "${uiState.loading}")
    }

    var canLoadMore by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(uiState.loading) {
        snapshotFlow { uiState.loading }
            .filter { !it }
            .take(1)
            .collect {
                canLoadMore = true
            }
    }

    LaunchedEffect(listState) {
        snapshotFlow{ listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if ((lastVisibleIndex == listState.layoutInfo.totalItemsCount - 1) && canLoadMore) {
                    canLoadMore = false
                    Log.d("loadMore", "${uiState.loading}")
                    actionLoadMore()

                }
            }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ){ padding ->
        if(uiState.players.isEmpty() && !uiState.loading) {
            Box(
                Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = stringResource(id = R.string.generic_error))
                    Button(onClick = {
                        actionLoadMore()
                        clearError()
                    }) {
                        Text(text = stringResource(id = R.string.retry))
                    }
                }
            }
        }else{
            LazyColumn(
                modifier = Modifier.padding(padding),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(uiState.players) {
                    PlayerListItem(
                        it
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerListItem(
    player: Player
){

    var sheetOpen by remember {
        mutableStateOf(false)
    }
    PlayerSheet(player = player, sheetOpen = sheetOpen) {
        sheetOpen = false
    }

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        onClick = { sheetOpen = true }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.player_img),
                contentDescription = stringResource(R.string.player_image),
                modifier = Modifier
                    .weight(0.3f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(0.7f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "${player.firstName} ${player.lastName}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${stringResource(id = R.string.position)}:",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                        Text(
                            text = player.position,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "${stringResource(id = R.string.team)}:",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                        Text(
                            text = player.team.name,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPlayerListScreenContent() {
    val players = listOf(
        Player(
            id = 1,
            firstName = "Stephen",
            lastName = "Curry",
            position = "G",
            height = "6-2",
            weight = "185",
            jerseyNumber = "30",
            college = "Davidson",
            country = "USA",
            draftYear = 2009,
            draftRound = 1,
            draftNumber = 7,
            team = Team(
                id = 1,
                name = "Golden State Warriors",
                city = "San Francisco",
                conference = "Western",
                division = "Pacific",
                fullName = "Golden State Warriors",
                abbreviation = "GSW"
            )
        ),
        Player(
            id = 1,
            firstName = "LeBron",
            lastName = "James",
            position = "F",
            height = "6-9",
            weight = "250",
            jerseyNumber = "6",
            college = "St. Vincent-St. Mary",
            country = "USA",
            draftYear = 2003,
            draftRound = 1,
            draftNumber = 1,
            team = Team(
                id = 1,
                name = "Los Angeles Lakers",
                city = "Los Angeles",
                conference = "Western",
                division = "Pacific",
                fullName = "Los Angeles Lakers",
                abbreviation = "LAL"
            )
        )
    )

    PlayerListScreenContent(
        PlayerListUiState(
            players = players,
            loading = false
        ),
        {}
    ){

    }
}