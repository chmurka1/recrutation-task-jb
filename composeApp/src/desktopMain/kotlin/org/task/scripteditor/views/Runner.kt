package org.task.scripteditor.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.task.scripteditor.model.runScript

@Composable
fun RunnerView(fileName: String?, resetError: () -> Unit, onError: (String) -> Unit, onSave: () -> Unit) {
    val scope = rememberCoroutineScope()

    var exitCode by remember { mutableStateOf(null as Int?) }
    var running by remember { mutableStateOf(false) }
    var scriptOutput by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {
        Card(
            backgroundColor = Color.Black,
            modifier = Modifier.fillMaxWidth().weight(1.0f)
        ) {
            Text(
                text = scriptOutput,
                modifier = Modifier.padding(16.dp),
                color = Color.White,
                fontFamily = FontFamily.Monospace
            )
        }
        Row {
            Button(onClick = {
                resetError()
                if (fileName != null) {
                    if (!running) {
                        running = true
                        scope.launch {
                            onSave()
                            scriptOutput = ""
                            exitCode = null
                            runScript(
                                fileName,
                                onOutputUpdated = { scriptOutput += it },
                                onExit = { exitCode = it }
                            )
                            running = false
                        }
                    }
                } else {
                    onError("No file loaded")
                }
            }) {
                Text("Run")
            }
            if (running) {
                Text("Running...", Modifier.padding(8.dp))
            }
            if (exitCode != null && exitCode != 0) {
                Text( "Process stopped with error code: $exitCode", Modifier.padding(8.dp))
            }
        }
    }
}