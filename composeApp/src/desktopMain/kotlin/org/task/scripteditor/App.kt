package org.task.scripteditor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.task.scripteditor.model.readScript
import org.task.scripteditor.model.saveScript
import org.task.scripteditor.views.EditorView
import org.task.scripteditor.views.RunnerView

@Composable
@Preview
fun App() {
    MaterialTheme {
        var errorMessage by remember { mutableStateOf(null as String?)}
        var loadedFileName by remember { mutableStateOf(null as String?)}
        var fileModified by remember { mutableStateOf(false) }
        var fileName by remember { mutableStateOf(null as String?) }
        var fileContents by remember { mutableStateOf("") }

        val onSave = {
            errorMessage = null
            fileModified = false
            saveScript(fileName, fileContents, onError = { errorMessage = it })
        }

        val onLoad = {
            errorMessage = null
            fileModified = false
            readScript(fileName,
                setContents = {
                    loadedFileName = fileName
                    fileContents = it
                },
                onError = { errorMessage = it }
            )
        }

        Column {
            Row {
                TextField(value = fileName?:"",
                    onValueChange = {
                        fileName = it.ifEmpty { null }
                    },
                    Modifier.weight(1.0f).padding(4.dp)
                )
                Button(onClick = onSave,
                    Modifier.padding(4.dp)) {
                    Text(text = "Save")
                }
                Button(onClick = onLoad,
                    Modifier.padding(4.dp)) {
                    Text(text = "Load")
                }
            }
            Row(Modifier.weight(1.0f)) {
                Box(modifier = Modifier.fillMaxWidth().weight(1.0f)) {
                    EditorView(
                        fileContents,
                        loadedFileName,
                        fileModified,
                        onContentsChange = {
                            fileModified = true
                            fileContents = it
                        }
                    )
                }
                Box(modifier = Modifier.fillMaxWidth().weight(1.0f)) {
                    RunnerView(
                        loadedFileName,
                        resetError = {
                            errorMessage = null
                        },
                        onError = {
                            errorMessage = it
                        },
                        onSave,
                    )
                }
            }

            Box(Modifier.weight(0.1f).padding(8.dp)) {
                if (errorMessage != null) {
                    Text(text = "Error: $errorMessage\n")
                }
            }
        }
    }
}