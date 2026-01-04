package com.example.noteworthy

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.noteworthy.ui.notes.NotesViewModel
import com.yourpackage.noteworthy.ui.theme.NoteWorthyTheme
import org.koin.androidx.compose.getViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/*

will be cleaning up the imports and removing wildcards etc

 */


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteWorthyTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    NotesScreen()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotesScreen(viewModel: NotesViewModel = getViewModel()) {
    val notes by viewModel.notes.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
        .withZone(ZoneId.systemDefault())
    val currentDateTime = formatter.format(Instant.now())
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Text(
                text = currentDateTime,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(notes, key = { it.timestamp }) { note ->
                    val dismissState = rememberDismissState()

                    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                        LaunchedEffect(note) {
                            viewModel.deleteNote(note)
                            val result = snackbarHostState.showSnackbar(
                                message = "Note deleted",
                                actionLabel = "Undo"
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                viewModel.addNote(note.title, note.content)
                            }
                        }
                    }

                    SwipeToDismiss(
                        state = dismissState,
                        directions = setOf(DismissDirection.EndToStart),
                        background = {
                            val colour = when (dismissState.dismissDirection) {
                                DismissDirection.EndToStart -> MaterialTheme.colorScheme.error
                                else -> Color.Transparent
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(colour)
                                    .padding(16.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color.White
                                )
                            }
                        },
                        dismissContent = {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(text = note.title, style = MaterialTheme.typography.titleMedium)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = formatter.format(Instant.ofEpochMilli(note.timestamp)),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    )
                }
            }
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Add New Note") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = title,
                                onValueChange = { title = it },
                                label = { Text("Title") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = content,
                                onValueChange = { content = it },
                                label = { Text("Content") }
                            )
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            viewModel.addNote(title, content)
                            showDialog = false
                            title = ""
                            content = ""
                        }) {
                            Text("Save")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}
