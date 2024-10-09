package com.ukrdroiddev.presentation.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ukrdroiddev.presentation.Manufacturers
import com.ukrdroiddev.presentation.Models
import com.ukrdroiddev.presentation.R
import com.ukrdroiddev.presentation.Years
import com.ukrdroiddev.presentation.ui.theme.CarChooserTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarChooserTheme {
                val navController = rememberNavController()
                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text(stringResource(id = R.string.title_manufacturer)) })
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = Manufacturers,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<Manufacturers> {
                            ManufacturersScreen {
                                navController.navigate(Models)
                            }
                        }
                        composable<Models> {
                            Text("TEST")
                        }
                        composable<Years> {}
                    }

                }
            }
        }
    }

}