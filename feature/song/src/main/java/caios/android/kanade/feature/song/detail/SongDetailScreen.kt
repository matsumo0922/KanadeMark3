package caios.android.kanade.feature.song.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.model.music.Queue
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.ui.AsyncLoadContents
import caios.android.kanade.core.ui.music.SongHolder
import caios.android.kanade.core.ui.view.KanadeTopAppBar

@Composable
internal fun SongDetailRoute(
    title: String,
    songIds: List<Long>,
    navigateToSongMenu: (Song) -> Unit,
    navigateToAddToPlaylist: (List<Song>) -> Unit,
    terminate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SongDetailViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(songIds) {
        viewModel.fetch(songIds)
    }

    AsyncLoadContents(screenState) {
        if (it != null) {
            SongDetailScreen(
                modifier = modifier,
                title = title,
                songs = it.songs,
                queue = it.queue,
                onClickSongHolder = viewModel::onNewPlay,
                onClickSongMenu = navigateToSongMenu,
                onClickShuffle = viewModel::onShufflePlay,
                onClickMenuAddToQueue = viewModel::addToQueue,
                onClickMenuAddToPlaylist = navigateToAddToPlaylist,
                onTerminate = terminate,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SongDetailScreen(
    title: String,
    songs: List<Song>,
    queue: Queue?,
    onClickSongHolder: (List<Song>, Int) -> Unit,
    onClickSongMenu: (Song) -> Unit,
    onClickShuffle: (List<Song>) -> Unit,
    onClickMenuAddToQueue: (List<Song>, Int?) -> Unit,
    onClickMenuAddToPlaylist: (List<Song>) -> Unit,
    onTerminate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberTopAppBarState()
    val behavior = TopAppBarDefaults.pinnedScrollBehavior(state)
    var isVisibleFAB by remember { mutableStateOf(false) }

    LaunchedEffect(songs) {
        isVisibleFAB = true
    }

    Scaffold(
        modifier = modifier.nestedScroll(behavior.nestedScrollConnection),
        topBar = {
            KanadeTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = title,
                behavior = behavior,
                onClickMenuPlayNext = { onClickMenuAddToQueue.invoke(songs, queue?.index?.plus(1)) },
                onClickMenuAddToQueue = { onClickMenuAddToQueue.invoke(songs, null) },
                onClickMenuAddToPlaylist = { onClickMenuAddToPlaylist.invoke(songs) },
                onTerminate = onTerminate,
            )
        }
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            LazyColumn(Modifier.fillMaxSize()) {
                itemsIndexed(
                    items = songs,
                    key = { _, song -> song.id },
                ) { index, song ->
                    SongHolder(
                        modifier = Modifier.fillMaxWidth(),
                        song = song,
                        onClickHolder = { onClickSongHolder.invoke(songs, index) },
                        onClickMenu = { onClickSongMenu.invoke(song) },
                    )
                }
            }

            AnimatedVisibility(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd),
                visible = isVisibleFAB,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
            ) {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    onClick = { onClickShuffle.invoke(songs) },
                ) {
                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}