package com.example.summarizerai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.summarizerai.ui.HomeScreen
import com.example.summarizerai.ui.QuestionScreen
import com.example.summarizerai.ui.SummaryScreen
import com.example.summarizerai.ui.theme.SummarizerAITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SummarizerAITheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("summary/{fileName}/{fileUri}") { backStackEntry ->
            val fileName = backStackEntry.arguments?.getString("fileName")
            val fileUri = backStackEntry.arguments?.getString("fileUri")
            SummaryScreen(navController, fileName, fileUri)
        }
        composable("question_screen/{fileName}/{fileUri}") { backStackEntry ->
            val fileName = backStackEntry.arguments?.getString("fileName")
            val fileUri = backStackEntry.arguments?.getString("fileUri")
            QuestionScreen(navController, fileName, fileUri)
        }
    }
}
