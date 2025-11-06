package com.example.lab4

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.lab4.screens.HomeScreen
import com.example.lab4.screens.Task1Screen
import com.example.lab4.screens.Task2Screen
import com.example.lab4.screens.Task3Screen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(onNavigate = { route -> navController.navigate(route) }) }
        composable("task1") { Task1Screen(onBack = { navController.popBackStack() }) }
        composable("task2") { Task2Screen(onBack = { navController.popBackStack() }) }
        composable("task3") { Task3Screen(onBack = { navController.popBackStack() }) }
    }
}
