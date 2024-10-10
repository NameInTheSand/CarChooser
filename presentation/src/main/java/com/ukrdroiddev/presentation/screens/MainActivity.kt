package com.ukrdroiddev.presentation.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ukrdroiddev.presentation.Manufacturers
import com.ukrdroiddev.presentation.Models
import com.ukrdroiddev.presentation.R
import com.ukrdroiddev.presentation.Years
import com.ukrdroiddev.presentation.ui.theme.CarChooserTheme
import com.ukrdroiddev.presentation.viewModels.MainActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        showSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            BackHandler {
                if (navController.previousBackStackEntry == null) {
                    finish()
                } else {
                    navController.navigateUp()
                }
            }

            CarChooserTheme {
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
                            ManufacturersScreenWrapper(
                                selectedItem = viewModel.selectedManufacturer,
                                onNavigateClick = {
                                    viewModel.onManufacturerSelected(it)
                                    navController.navigate(Models)
                                }
                            )
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

    private fun showSplashScreen() {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isSplashShow.value
            }
        }
    }

}