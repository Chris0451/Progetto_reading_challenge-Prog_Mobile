package com.project.reading_challenge.ui.screens.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.reading_challenge.data.remote.ImageLinks
import com.project.reading_challenge.data.remote.VolumeInfo
import com.project.reading_challenge.data.remote.VolumeItem

@Composable
fun CatalogRoute(
    vm: CatalogViewModel = hiltViewModel(),
    onOpenBook: (String) -> Unit
) {
    val state by vm.state.collectAsState()
    LaunchedEffect(Unit) { vm.load() }
    CatalogScreen(state, onOpenBook)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    state: CatalogUiState,
    onOpenBook: (String) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Catalogo consigliato") }) }
    ) { padding ->
        when {
            state.isLoading -> Box(Modifier.fillMaxSize().padding(padding)) {
                LinearProgressIndicator(Modifier.fillMaxWidth().padding(16.dp))
            }
            state.error != null -> Box(Modifier.fillMaxSize().padding(padding)) {
                Text("Errore: ${state.error}", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
            }
            else -> LazyColumn(Modifier.padding(padding)) {
                items(state.items, key = { it.id }) { item ->
                    CatalogRow(item = item, onClick = { onOpenBook(item.id) })
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                }
            }
        }
    }
}

@Composable
private fun CatalogRow(item: VolumeItem, onClick: () -> Unit) {
    ListItem(
        leadingContent = {
            val thumb = item.volumeInfo?.imageLinks?.thumbnail?.replace("http://", "https://")
            if (!thumb.isNullOrBlank()) {
                Image(
                    painter = rememberAsyncImagePainter(thumb),
                    contentDescription = null,
                    modifier = Modifier.size(56.dp)
                )
            }
        },
        headlineContent = { Text(item.volumeInfo?.title.orEmpty()) },
        supportingContent = { Text(item.volumeInfo?.authors?.joinToString().orEmpty()) },
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    )
}

@Preview
@Composable
private fun CatalogPreview() {
    val demo = CatalogUiState(
        items = listOf(
            VolumeItem("id1", VolumeInfo("Dune", listOf("Frank Herbert"), imageLinks = ImageLinks(thumbnail = ""))),
            VolumeItem("id2", VolumeInfo("Neuromante", listOf("William Gibson"), imageLinks = ImageLinks(thumbnail = "")))
        )
    )
    MaterialTheme { CatalogScreen(demo) { } }
}
