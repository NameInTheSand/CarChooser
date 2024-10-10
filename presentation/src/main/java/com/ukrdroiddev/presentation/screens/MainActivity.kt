package com.ukrdroiddev.presentation.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
            var appBarTitle by remember { mutableStateOf("") }

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
                        TopAppBar(
                            title = { Text(appBarTitle) },
                            navigationIcon = {
                                if (appBarTitle == stringResource(R.string.title_manufacturer)) {
                                    null
                                } else {
                                    IconButton(onClick = { navController.navigateUp() }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Back"
                                        )
                                    }
                                }

                            }
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = Manufacturers,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<Manufacturers> {
                            // Workaround to set title, because current implementation of addOnDestinationChangedListener doesn't support a current destination class
                            appBarTitle = stringResource(R.string.title_manufacturer)
                            ManufacturersScreen(
                                prevSelectedItem = viewModel.selectedManufacturer,
                                onNavigateClick = {
                                    viewModel.onManufacturerSelected(it)
                                    navController.navigate(
                                        Models(
                                            chosenManufacturerId = it.id,
                                            chosenManufacturerName = it.name
                                        )
                                    )
                                }
                            )
                        }
                        composable<Models> {
                            appBarTitle = stringResource(R.string.title_models)
                            ModelsScreen(
                                prevSelectedItem = viewModel.selectedModel,
                                onNavigateClick = {
                                    viewModel.onModelSelected(it)
                                    navController.navigate(
                                        Years(
                                            chosenManufacturerId = viewModel.selectedManufacturer!!.id,
                                            chosenManufacturerName = viewModel.selectedManufacturer!!.name,
                                            chosenModelName = it.name
                                        )
                                    )
                                },
                                onLeaveScreen = {
                                    viewModel.onModelSelected(null)
                                }
                            )
                        }
                        composable<Years> {
                            appBarTitle = stringResource(R.string.title_years)
                        }
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