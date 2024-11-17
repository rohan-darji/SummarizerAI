package com.example.summarizerai.ui

import SummaryData
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.summarizerai.ui.theme.MainBlue
import loadSummaryFromAssets
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(navController: NavHostController, fileName: String?, fileUri: String?) {
    val decodedFileName: String? = fileName?.let { Uri.decode(it) }
    val decodedFileUri: String? = fileUri?.let { Uri.decode(it) }

    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
//    val context = LocalContext.current

//    var summaryData by remember { mutableStateOf<SummaryData?>(null) }
    var summaryText by remember { mutableStateOf("") }
    var isSummarySelected by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val shortSummary =
        "Here's a summary of the text in 50-100 words:\\n\\nThe Z515: Information Architecture course at IU Bloomington's Luddy School of Informatics, Computing, and Engineering explores the principles and theories behind the design of information systems. The course covers topics including classification schemes, ontologies, controlled vocabularies, and thesauri, as well as research in information science, cognitive science, semiotics, and computer science. Students will learn to identify and analyze information architecture problems and suggest effective solutions. The course is expected to cover various aspects of information architecture through lectures, discussions, group activities, and a required textbook."
    val mediumSummary =
        "The course \\\"Information Architecture\\\" at IU Bloomington's Luddy School of Informatics, Computing, and Engineering is a 15-week course that explores the principles and practices of information architecture in digital ecosystems. The course, taught by Kate Wehner, aims to equip students with the skills to design effective information systems that enable users to navigate and discover content with ease.\\n\\nThe course objectives include understanding the objectives, functions, and applications of information architectures, becoming familiar with structured models of information representation and organization, and being able to analyze and suggest improved information architectures based on representation and organization frameworks.\\n\\nThe course will cover topics such as classification schemes, ontologies, controlled vocabularies, thesauri, user experience, mental models, research, evaluation, and design, site maps, flow charts, wireframes, and metadata and tagging. Students will engage in class discussions, group work, and in-class activities to illustrate the principles and theories presented in readings and lectures.\\n\\nAssessment will be based on a combination of multiple performance measures, including examinations, class participation, written assignments and exercises, research papers, term projects, in-class small-group exercises, oral presentations, field-based projects and field experiences, and case study presentations.\\n\\nGrading will be based on a letter system, with A grades representing outstanding achievement, and D and F grades indicating unsatisfactory or failing work. No course with a grade lower than C will be counted toward the ILS degree, and any required course with a grade lower than B- must be repeated.\\n\\nIn addition to academic requirements, the course emphasizes the importance of academic integrity, including avoiding plagiarism and academic dishonesty. Students with disabilities are encouraged to contact IU Disability Services for reasonable accommodations.\\n\\nThe course outline lists the topics to be covered, including readings from the required textbook, \\\"The Elements of User Experience\\\" by Jesse James Garrett, and additional readings available on Canvas."
    val longSummary =
        "This document is the syllabus for the course \\\"Information Architecture\\\" (Z515) offered by the Luddy School of Informatics, Computing, and Engineering at Indiana University Bloomington during the Fall 2024 semester.\\n\\n**Course Description:**\\n\\nThe course focuses on the principles of information architecture and its application in digital ecosystems to enable users to navigate and understand categories of information. It will cover traditional systematic structures, such as classification schemes, ontologies, controlled vocabularies, and thesauri. The course will also review research in information science, cognitive science, semiotics, and computer science to inform the design of more effective information systems.\\n\\n**Course Objectives:**\\n\\nBy the end of the course, students will:\\n\\n1. Understand the objectives, functions, and applications of information architectures in various environments.\\n2. Be familiar with a range of structured models of information representation and organization and discern the implications for effective retrieval and/or navigation.\\n3. Be able to identify and analyze the problems of a given information architecture and suggest a more effective and efficient architecture based on the framework of representation and organization.\\n\\n**Course Outline:**\\n\\nThe course will consist of 15 sessions, with each session covering a specific topic, including:\\n\\n1. Introduction to the course\\n2. Information Architecture (IA) and user experience\\n3. Representation and mental models\\n4. IA Research, evaluation & design\\n5. IA Research, evaluation & design (Part 2)\\n6. Order versus organization\\n7. Order and organization for navigation\\n8. Metadata and tagging\\n9. Controlled vocabularies, facets, and labeling systems\\n10. Site maps and flow charts\\n11. Wireframes\\n12. Wireframes (continued)\\n13. Working session\\n14. Wireframe critiques\\n15. Wireframe critiques\\n\\n**Required Textbook:**\\n\\nThe Elements of User Experience by Jesse James Garrett, 2nd ed.\\n\\n**Required Readings:**\\n\\nThe course outline lists topics and required readings, which will be available on Canvas.\\n\\n**Assignments:**\\n\\nAll assignment details will be available on Canvas.\\n\\n**Grading:**\\n\\nThe grading will be based on a combination of student performance measures, including:\\n\\n1. Class participation\\n2. Assignments\\n3. written assignments and exercises\\n4. research papers\\n5. term projects\\n\\n**Grading Scale:**\\n\\nThe grading scale will be as follows:\\n\\n* A: 4.0, Outstanding achievement\\n* A-: 3.7, Excellent achievement\\n* B+: 3.3, Very good work\\n* B: 3.0, Good work\\n* B-: 2.7, Marginal work\\n* C+: 2.3, Unsatisfactory work\\n* C: 2.0, Unsatisfactory work\\n* C-: 1.7, Unacceptable work\\n* D+: 1.3, Unacceptable work\\n* D: 1.0, Unacceptable work\\n* F: 0.0, Failing\\n\\n**Academic Integrity:**\\n\\nPlagiarism and other forms of academic dishonesty will not be tolerated. Students found to be engaging in plagiarism, cheating, and other types of dishonesty will receive an F for the assignment and additional penalties will be applied at the discretion of the instructor.\\n\\n**Accessibility:**\\n\\nThe course will make reasonable accommodations for students with disabilities. Students with disabilities should contact IU Disability Services for Students.\\n\\n**COVID-19 Information:**\\n\\nIf students need to miss class in-person due to illness, they should notify the instructor in advance, and arrangements will be made to record the class session."

    // Load summary data from assets when composable is first launched.
//    LaunchedEffect(Unit) {
//        summaryData = loadSummaryFromAssets(context)
//    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Summary Screen",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
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

            // File Selection Button
            Button(
                onClick = {
                    coroutineScope.launch {
                        delay(2000) // Delay for 2 seconds before showing summary.
                        summaryText = shortSummary
                        isSummarySelected = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MainBlue)
            ) {
                Text(
                    text = "Short Summary",
                    fontSize = 18.sp,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        delay(2000) // Delay for 2 seconds before showing summary.
                        summaryText = mediumSummary
                        isSummarySelected = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MainBlue)
            ) {
                Text(
                    text = "Medium Summary",
                    fontSize = 18.sp,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        delay(2000) // Delay for 2 seconds before showing summary.
                        summaryText = longSummary
                        isSummarySelected = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MainBlue)
            ) {
                Text(
                    text = "Long Summary",
                    fontSize = 18.sp,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Take up remaining space in the column
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .background(Color.LightGray.copy(alpha = 0.2f))
                    .padding(16.dp)
            ) {
                Text(
                    text = if (summaryText.isNotEmpty()) summaryText else "Select a button to view the summary.",
                    fontSize = 16.sp
                )
            }
            if (isSummarySelected) {
                Button(
                    onClick = {
                        val encodedFileName = Uri.encode(fileName)
                        val encodedUriString = Uri.encode(selectedFileUri.toString())
                        navController.navigate("question_screen/$encodedFileName/$encodedUriString") // Navigate to QuestionScreen when clicked.
                    },
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MainBlue),
                ) {
                    Text(text = "Ask Questions", fontSize = 18.sp, color = Color.White)
                }
            }
        }
    }
}