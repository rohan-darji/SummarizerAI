package com.example.summarizerai.ui

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.summarizerai.ui.theme.MainBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(navController: NavHostController, fileName: String?, fileUri: String?) {
    val decodedFileName: String? = fileName?.let { Uri.decode(it) }
    val decodedFileUri: String? = fileUri?.let { Uri.decode(it) }

    var userInput by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }

    val qaMap = mapOf(
        "How long is the course?" to "According to the syllabus, the course outline lists up to Session 15, which is December 10th. There does not appear to be any other sessions scheduled beyond December 10th for the course, Z515: Information Architecture.",
        "What are the prerequisites?" to "There are no prerequisites explicitly listed in the syllabus.",
        "Give me the course outline in short" to "Session 1-2 (Aug 27, Sep 3): Introduction, IA and user experience\n" +
                "Session 3-4 (Sep 10, 17): Representation, mental models, IA research\n" +
                "Session 5-6 (Sep 24, Oct 1): IA research, evaluation and design\n" +
                "Session 7-8 (Oct 8, 15): Order and organization, metadata and tagging\n" +
                "Session 9-10 (Oct 22, 29): Controlled vocabularies, site maps, and flow charts\n" +
                "Session 11-12 (Nov 5, 12): Wireframes\n" +
                "Session 13-15 (Nov 19, Dec 3, 10): Working session and wireframe critiques"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Questions & Answers",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )

        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Selected File Name:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = decodedFileName ?: "No file selected",
                color = Color.Gray,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = userInput,
                onValueChange = { userInput = it },
                label = { Text("Ask a question") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Button to submit question
            Button(
                onClick = {
                    // Check if the user's question matches any predefined questions
                    answer = qaMap[userInput] ?: "Sorry, I don't have an answer for that question."
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MainBlue),
            ) {
                Text(text = "Submit")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Display the answer if available
            if (answer.isNotEmpty()) {
                Text(
                    text = "Answer:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = answer,
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
        }
    }
}