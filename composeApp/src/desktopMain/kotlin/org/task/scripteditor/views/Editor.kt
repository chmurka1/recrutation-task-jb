package org.task.scripteditor.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.task.scripteditor.model.readScript
import org.task.scripteditor.model.saveScript

@Composable
fun EditorView(contents: String, fileName: String?, fileModified: Boolean, onContentsChange: (String) -> Unit) {
    Column(Modifier.padding(16.dp).fillMaxHeight()) {
        Card(Modifier.weight(1.0f)) {
            Column(Modifier.padding(16.dp)) {

                Text(formatFileName(fileName, fileModified))
                BasicTextField(
                    value = "$contents\n",
                    onContentsChange,
                    Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontFamily = FontFamily.Monospace),
                    visualTransformation = SyntaxColoringVisualTransformation(
                        arrayOf(
                            "while",
                            "for",
                            "if",
                            "in",
                            "break",
                            "continue",
                            "val",
                            "var",
                            "true",
                            "false",
                            "as",
                            "class",
                            "fun"
                        )
                    )
                )
            }
        }
    }
}

private fun formatFileName(fileName: String?, modified: Boolean): String {
    if (fileName == null) {
        return ""
    } else if (modified) {
        return "[$fileName*]"
    } else {
        return "[$fileName]"
    }
}

private class SyntaxColoringVisualTransformation(val keywords: Array<String>): VisualTransformation {
    override fun filter(text: AnnotatedString) = TransformedText(
        buildAnnotatedString {
            val formattedText = text.replace(Regex.fromLiteral("\t"), " ")
            append(formattedText)
            for (keyword in keywords) {
                Regex("(^|\\W)(?<word>$keyword)(\\W|$)")
                    .findAll(formattedText)
                    .map { it.groups["word"]!! }
                    .forEach {
                        addStyle(
                            style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Magenta),
                            it.range.first,
                            it.range.last + 1
                        )
                    }
            }
        },
        OffsetMapping.Identity
    )
}