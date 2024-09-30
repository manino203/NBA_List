package com.example.nba_list.ui.composables

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nba_list.R
import com.example.nba_list.data.models.Player
import com.example.nba_list.data.models.Team
import kotlinx.coroutines.launch

@SuppressLint("ComposeModifierMissing")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerSheet(
    player: Player,
    sheetOpen: Boolean,
    onDismiss: () -> Unit
){

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val coroScope = rememberCoroutineScope()

    BottomSheet(
        showSheet = sheetOpen,
        sheetState = sheetState,
        onDismiss = {
            coroScope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                onDismiss()
            }
        }
    ) {
        PlayerSheetContent(player = player)
    }
}


@Composable
private fun PlayerSheetContent(
    player: Player,
) {
    var teamInfoVisible by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {

        Header("${player.firstName} ${player.lastName}")

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoItem(label = stringResource(id = R.string.position), value = player.position)
            InfoItem(label = stringResource(id = R.string.height), value = player.height)
            InfoItem(label = stringResource(id = R.string.weight), value = player.weight)
            InfoItem(label = stringResource(id = R.string.jersey_number), value = player.jerseyNumber)
            InfoItem(label = stringResource(id = R.string.college), value = player.college)
            InfoItem(label = stringResource(id = R.string.country), value = player.country)
            InfoItem(label = stringResource(id = R.string.draft_year), value = player.draftYear.toString())
            InfoItem(label = stringResource(id = R.string.draft_round), value = player.draftRound.toString())
            InfoItem(label = stringResource(id = R.string.draft_number), value = player.draftNumber.toString())
        }


        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { teamInfoVisible = !teamInfoVisible }
                .padding(vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${stringResource(id = R.string.team)}: ${player.team.name}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = stringResource(id = R.string.expand_collapse),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .rotate(if (teamInfoVisible) 180f else 0f)
                        .size(24.dp)
                )
            }
        }

        AnimatedVisibility(visible = teamInfoVisible) {
            Column {
                HorizontalDivider(modifier = Modifier
                    .padding(vertical = 8.dp)
                    .alpha(.5f))
                TeamInfo(team = player.team)
            }
        }
    }
}



@Composable
private fun TeamInfo(
    team: Team,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {

        Header("${team.name} (${team.city})",)

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoItem(label = stringResource(id = R.string.conference), value = team.conference)
            InfoItem(label = stringResource(id = R.string.division), value = team.division)
            InfoItem(label = stringResource(id = R.string.full_name), value = team.fullName)
            InfoItem(label = stringResource(id = R.string.abbreviation), value = team.abbreviation)
        }
    }
}

@SuppressLint("ComposeModifierMissing")
@Composable
private fun Header(text: String){
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}


@SuppressLint("ComposeModifierMissing")
@Composable
private fun InfoItem(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewPlayerSheetContent() {
    val player = Player(
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
    )
    PlayerSheetContent(player = player)
}
