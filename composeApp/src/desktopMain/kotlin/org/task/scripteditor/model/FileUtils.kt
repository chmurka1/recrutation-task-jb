package org.task.scripteditor.model

import java.io.File

fun saveScript(fileName: String?, contents: String, onSuccess: (String) -> Unit, onError: (String?) -> Unit) = try {
    File(fileName!!).writeText(contents)
    onSuccess(fileName)
} catch (e: Exception) {
    onError(e.toString())
}

fun readScript(fileName: String?, setContents: (String) -> Unit, onError: (String?) -> Unit) = try {
    setContents(File(fileName!!).readText())
} catch (e: Exception) {
    onError(e.toString())
}