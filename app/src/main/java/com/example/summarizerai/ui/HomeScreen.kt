package com.example.summarizerai.ui

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.summarizerai.R
import com.example.summarizerai.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    var selectedFileName by remember { mutableStateOf<String?>(null) }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }

    // File picker launcher to open document picker for PDFs or other files.
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            // Extract and set the file name from the URI.
            uri?.let {
                selectedFileName = getFileNameFromUri(context, it)
                selectedFileUri = it
            }
        }
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    // Use a Row to align both title and logo at the center
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // App Logo
                        Image(
                            painter = painterResource(id = R.drawable.summarizer), // Replace with your logo resource.
                            contentDescription = "App Logo",
                            modifier = Modifier.size(32.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp)) // Add some space between logo and title

                        // App Title
                        Text(
                            text = "Summarizer AI",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Top Icon and Title Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.summarizer), // Replace with your icon resource.
                    contentDescription = "ChatPDF Icon",
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Fit,
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Chat with your",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "Documents",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Orange,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "You can ask questions, get summaries, find information, and more.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }

            // File Selection Button
            Button(
                onClick = {
                    filePickerLauncher.launch(
                        arrayOf(
                            "application/pdf",
                            "application/msword",
                            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MainBlue)
            ) {
                Text(
                    text = "Select File",
                    fontSize = 18.sp,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(
                modifier =
                Modifier.height(12.dp)
            )

            // Display selected file URI (if any)
            selectedFileName?.let { fileName ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .background(LightGray, shape = RoundedCornerShape(8.dp))
                        .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Selected File: $fileName",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(
                    modifier =
                    Modifier.height(12.dp)
                )
                Button(
                    onClick = {
                        val encodedFileName = Uri.encode(fileName)
                        val encodedUriString = Uri.encode(selectedFileUri.toString())
                        navController.navigate("summary/$encodedFileName/$encodedUriString")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MainBlue)
                ) {
                    Text(
                        text = "Next",
                        fontSize = 18.sp,
                        color = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

fun getFileNameFromUri(context: Context, uri: Uri): String {
    var fileName: String? = null

    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1) {
                fileName = it.getString(nameIndex)
            }
        }
    }

    return fileName ?: "Unknown"
}