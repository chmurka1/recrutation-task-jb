package org.task.scripteditor.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun runScript(fileName: String, onOutputUpdated: (String) -> Unit, onExit: (Int) -> Unit) {
    onOutputUpdated("Running `kotlinc -script $fileName`...\n")
    val process = withContext(Dispatchers.IO) {
        ProcessBuilder(
            "/bin/bash",
            "kotlinc",
            "-script",
            fileName
        ).redirectErrorStream(true).start()
    }
    val input = process.inputReader()

    var line = ""
    while (withContext(Dispatchers.IO) {
        input.readLine()
    }?.also { line = it } != null) {
            onOutputUpdated(line + '\n')
    }

    var result = 0
    withContext(Dispatchers.IO) {
        result = process.waitFor()
    }
    onExit(result)
}