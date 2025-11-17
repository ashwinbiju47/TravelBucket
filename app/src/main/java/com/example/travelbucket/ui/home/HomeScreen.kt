package com.example.travelbucket.ui.home

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

fun formatCompact(number: Long): String {
    return when {
        number >= 1_000_000_000 -> String.format("%.1fB", number / 1_000_000_000.0)
        number >= 1_000_000 -> String.format("%.1fM", number / 1_000_000.0)
        number >= 1_000 -> String.format("%.1fK", number / 1_000.0)
        else -> number.toString()
    }
}

@Composable
fun HomeScreen(
    viewModel: CountryViewModel,
    email: String,
    onLogout: () -> Unit
) {
    val query by viewModel.query.collectAsState()
    val result by viewModel.countryInfo.collectAsState()
    val history by viewModel.searchHistory.collectAsState(initial = emptyList())
    // ADD THIS
    val errorMessage by viewModel.errorMessage.collectAsState()
    val context = LocalContext.current

    // ADD THIS BLOCK
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    var menuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 52.dp)

    ) {

        // Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Hi,", style = MaterialTheme.typography.titleMedium)
                Text(text = email, style = MaterialTheme.typography.bodyMedium)
            }

            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Logout") },
                        onClick = {
                            menuExpanded = false
                            onLogout()
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Search Bar
        OutlinedTextField(
            value = query,
            onValueChange = { viewModel.updateQuery(it) },
            placeholder = { Text("Search country") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Button(onClick = { viewModel.search(query) }) {
            Text("Search")
        }

        Spacer(Modifier.height(20.dp))

        // Search Result
//        result?.let { country ->
//            Text("Population: ${country.population}")
//            Text("Currency: ${country.currency}")
//        }

        Spacer(Modifier.height(20.dp))

        Text(
            "Search History",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        // TABLE HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Country", modifier = Modifier.weight(1f))
            Text("Population", modifier = Modifier.weight(1f))
            Text("Currency", modifier = Modifier.weight(1f))
        }

        Divider()

        // TABLE BODY
        LazyColumn {
            items(history) { entry ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(entry.countryName, modifier = Modifier.weight(1f))
                    Text(
                        text = formatCompact(entry.population.toLong()),
                        modifier = Modifier.weight(1f)
                    )
                    Text(entry.currency, modifier = Modifier.weight(1f))
                }
                Divider()
            }
        }
    }
}
