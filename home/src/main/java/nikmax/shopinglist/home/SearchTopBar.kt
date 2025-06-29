package nikmax.shopinglist.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopSearchBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nikmax.shopinglist.core.Currency
import nikmax.shopinglist.core.ui.ListUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchTopBar(
    results: List<ListUi>,
    onSearch: (String) -> Unit,
    onListOpen: (ListUi) -> Unit,
    scrollBehavior: SearchBarScrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior(),
    searchCallDelay: Long = SearchTopBaDefaults.searchCallDelay
) {
    val searchBarState = rememberSearchBarState()
    val textFieldState = rememberTextFieldState()
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    
    LaunchedEffect(textFieldState.text) {
        delay(searchCallDelay)
        onSearch(textFieldState.text.toString())
    }
    
    val inputField =
        @Composable {
            SearchBarDefaults.InputField(
                searchBarState = searchBarState,
                textFieldState = textFieldState,
                onSearch = { query ->
                    keyboardController?.hide()
                    onSearch(query)
                },
                placeholder = { Text(stringResource(R.string.search_placeholder)) },
                leadingIcon = {
                    if (searchBarState.currentValue == SearchBarValue.Expanded) {
                        IconButton(
                            onClick = {
                                textFieldState.clearText()
                                scope.launch { searchBarState.animateToCollapsed() }
                            }
                        ) {
                            Icon(Icons.AutoMirrored.Default.ArrowBack, stringResource(R.string.back))
                        }
                    }
                    else {
                        Icon(Icons.Default.Search, stringResource(R.string.icon))
                    }
                }
            )
        }
    
    TopSearchBar(
        state = searchBarState,
        inputField = inputField,
        scrollBehavior = scrollBehavior
    )
    ExpandedFullScreenSearchBar(
        state = searchBarState,
        inputField = inputField
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(results.isEmpty()) { isEmpty ->
                when (isEmpty) {
                    true -> NothingFound()
                    false -> LazyColumn(
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(results) { result ->
                            ListCard(
                                list = result,
                                onClick = { onListOpen(result) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(device = "id:pixel_5", showSystemUi = false, showBackground = false)
@Composable
private fun Preview() {
    val lists = listOf(
        ListUi(
            name = "name",
            creationDate = 0,
            id = 0,
            items = 0,
            completedItems = 0,
            totalPrice = 0f,
            completedPrice = 0f,
            currency = Currency.USD
        ),
        ListUi(
            name = "list name",
            creationDate = 0,
            id = 0,
            items = 0,
            completedItems = 0,
            totalPrice = 0f,
            completedPrice = 0f,
            currency = Currency.USD
        )
    )
    Surface {
        var results by remember { mutableStateOf(emptyList<ListUi>()) }
        Scaffold(
            topBar = {
                SearchTopBar(
                    results = results,
                    onSearch = { query ->
                        results = lists.filter { it.name.contains(query) }
                    },
                    onListOpen = {}
                )
            }
        ) { }
    }
}

internal object SearchTopBaDefaults {
    val searchCallDelay = 300L
}
