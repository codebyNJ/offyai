package com.google.mediapipe.examples.llminference

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
internal fun ChatRoute(
    chatViewModel: ChatViewModel = viewModel(
        factory = ChatViewModel.getFactory(LocalContext.current.applicationContext)
    )
) {
    val uiState by chatViewModel.uiState.collectAsStateWithLifecycle()
    val textInputEnabled by chatViewModel.isTextInputEnabled.collectAsStateWithLifecycle()
    ChatScreen(
        uiState,
        textInputEnabled
    ) { message ->
        chatViewModel.sendMessage(message)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    uiState: UiState,
    textInputEnabled: Boolean = true,
    onSendMessage: (String) -> Unit
) {
    var userMessage by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) ,// Use black for dark theme
        verticalArrangement = Arrangement.Bottom
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            reverseLayout = true
        ) {
            items(uiState.messages) { chat ->
                ChatItem(chat)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column { }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = userMessage,
                onValueChange = { userMessage = it },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                ),
                label = {
                    Text(stringResource(R.string.chat_label))
                },
                shape = RoundedCornerShape(24.dp), // Rounded corners
                modifier = Modifier
                    .weight(0.85f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF5FDFB6),
                    unfocusedBorderColor = Color(0xFFFFFFFF).copy(alpha = 0.5f),
                    containerColor = Color(0xFF252525), // Use `containerColor` for background
                    cursorColor = Color.White, // Cursor color
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFFFFFFFF) // Set the text color here
                ),
                enabled = textInputEnabled
            )


            IconButton(
                onClick = {
                    if (userMessage.isNotBlank()) {
                        onSendMessage(userMessage)
                        userMessage = ""
                    }
                },
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth()
                    .weight(0.15f),
                enabled = textInputEnabled
            ) {
                Icon(
                    Icons.AutoMirrored.Default.Send,
                    contentDescription = stringResource(R.string.action_send),
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun ChatItem(
    chatMessage: ChatMessage
) {
    val horizontalAlignment = if (chatMessage.isFromUser) {
        Alignment.End
    } else {
        Alignment.Start
    }

    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
    ) {
        val (author, color) = if (chatMessage.isFromUser) {
            Pair(stringResource(R.string.user_label), androidx.compose.ui.graphics.Color(0xFF1D1D1D))
        } else {
            Pair(stringResource(R.string.model_label), androidx.compose.ui.graphics.Color(0xFF5FDFB6))
        }

        // Author text with specific color
        Text(
            text = author,
            style = MaterialTheme.typography.bodySmall,
            color = color, // Use the specific color for the author
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Row {
            if (chatMessage.isFromUser) {
                // User's message in a bubble with #1d1d1d background
                BoxWithConstraints {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color(0xFF1D1D1D)),
                        shape = RoundedCornerShape(16.dp), // Bubble shape for the user
                        modifier = Modifier.widthIn(0.dp, maxWidth * 0.9f)
                    ) {
                        Text(
                            text = chatMessage.message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = androidx.compose.ui.graphics.Color.White, // Ensures text is readable
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                // AI's response as plain text
                Text(
                    text = chatMessage.message,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(8.dp)
                )
            }
        }
    }
}



