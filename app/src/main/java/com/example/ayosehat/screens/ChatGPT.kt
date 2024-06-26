package com.example.ayosehat.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ayosehat.navigation.Routes
import com.example.ayosehat.viewmodel.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatGPT(viewModel: ChatViewModel, navController: NavController) {
    var inputText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chatbot") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(
                        onClick = { navController.navigate(Routes.BottomNav.routes) }
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Checkmark")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    reverseLayout = true
                ) {
                    items(viewModel.messages.reversed()) { message ->
                        if (message.isUser) {
                            MessageBubble(message.content, Alignment.End)
                        } else {
                            MessageBubble(message.content, Alignment.Start)
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    TextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface
                        ),
                        label = { Text("Type a message") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(onSend = {
                            viewModel.sendMessage(inputText)
                            inputText = ""
                        })
                    )

                    IconButton(
                        onClick = {
                            viewModel.sendMessage(inputText)
                            inputText = ""
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.Send,
                            contentDescription = "Send message",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun MessageBubble(text: String, alignment: Alignment.Horizontal) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primary, // Light tint of primary color
        tonalElevation = 4.dp,
        modifier = Modifier
            .padding(vertical = 4.dp)
            .wrapContentWidth(alignment)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimary // Text color contrasting with primary
        )
    }
}