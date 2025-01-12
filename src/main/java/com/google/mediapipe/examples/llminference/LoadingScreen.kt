package com.google.mediapipe.examples.llminference

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException

@SuppressLint("Range")
@Composable
internal fun LoadingRoute(
    onModelLoaded: () -> Unit = { }
) {
    val context = LocalContext.current.applicationContext
    var progressText by remember { mutableStateOf("Starting...") }
    var errorMessage by remember { mutableStateOf("") }

    if (errorMessage.isNotEmpty()) {
        ErrorMessage(errorMessage)
    } else {
        LoadingIndicator(progressText)
    }

    LaunchedEffect(Unit, block = {
        withContext(Dispatchers.IO) {
            try {
                val modelFileName = "gemma-2b-it-gpu-int4.bin"
                val modelUri = "https://www.dropbox.com/scl/fi/5nnfrto6mrk5z907k1pgl/gemma-2b-it-gpu-int4.bin?rlkey=jzdluvw78ox6tcwl9htgrnk5q&st=nrmpb27k&dl=1"
                val internalFile = File(context.filesDir, modelFileName)

                if (internalFile.exists() && internalFile.length() > 0) {
                    progressText = "Model already exists. Preparing to load..."
                } else {
                    progressText = "Downloading model..."
                    val client = OkHttpClient()
                    val request = Request.Builder().url(modelUri).build()

                    client.newCall(request).execute().use { response ->
                        if (!response.isSuccessful) {
                            throw IOException("Unexpected response code: ${response.code}")
                        }

                        progressText = "Saving model to internal storage..."
                        internalFile.outputStream().use { output ->
                            response.body?.byteStream()?.copyTo(output)
                        }
                    }
                }

                progressText = "Model ready."
                withContext(Dispatchers.Main) {
                    onModelLoaded()
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "An error occurred while preparing the model."
            }
        }
    })
}

@Composable
fun LoadingIndicator(progressText: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = progressText,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorMessage(
    errorMessage: String
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
    }
}
