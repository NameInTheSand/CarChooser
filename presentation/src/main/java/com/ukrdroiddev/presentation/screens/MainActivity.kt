package com.ukrdroiddev.presentation.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
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
import com.ukrdroiddev.presentation.Destinations
import com.ukrdroiddev.presentation.R
import com.ukrdroiddev.presentation.ui.theme.CarChooserTheme
import com.ukrdroiddev.presentation.viewModels.MainActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModel()

    @SuppressLint("RestrictedApi")
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
                                    IconButton(onClick = { onBackPressedDispatcher.onBackPressed() }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = null
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
                        startDestination = Destinations.Manufacturers,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<Destinations.Manufacturers> {
                            // Workaround to set title, because current implementation of addOnDestinationChangedListener doesn't support a current destination class
                            appBarTitle = stringResource(R.string.title_manufacturer)
                            ManufacturersScreen(
                                prevSelectedItem = viewModel.selectedManufacturer,
                                onNavigateClick = {
                                    viewModel.onManufacturerSelected(it)
                                    navController.navigate(
                                        Destinations.Models(
                                            chosenManufacturerId = it.id,
                                            chosenManufacturerName = it.name
                                        )
                                    )
                                }
                            )
                        }
                        composable<Destinations.Models> {
                            appBarTitle = stringResource(R.string.title_models)
                            ModelsScreen(
                                prevSelectedItem = viewModel.selectedModel,
                                onNavigateClick = {
                                    viewModel.onModelSelected(it)
                                    navController.navigate(
                                        Destinations.Years(
                                            chosenManufacturerId = viewModel.selectedManufacturer!!.id,
                                            chosenManufacturerName = viewModel.selectedManufacturer!!.name,
                                            chosenModelName = it.name
                                        )
                                    )
                                },
                                onLeaveScreen = {
                                    viewModel.onModelSelected(null)
                                    navController.navigateUp()
                                }
                            )
                        }
                        composable<Destinations.Years> {
                            appBarTitle = stringResource(R.string.title_years)
                            YearsScreen(
                                prevSelectedItem = viewModel.selectedYear,
                                onNavigateClick = {
                                    viewModel.onYearSelected(it)
                                    navController.navigate(Destinations.Summary)
                                },
                                onLeaveScreen = {
                                    viewModel.onYearSelected(null)
                                    navController.navigateUp()
                                }
                            )
                        }

                        composable<Destinations.Summary> {
                            appBarTitle = stringResource(R.string.title_summary)
                            SummaryScreen(
                                selectedManufacturer = viewModel.selectedManufacturer!!.name,
                                selectedModel = viewModel.selectedModel!!.name,
                                selectedYear = viewModel.selectedYear!!.year,
                                onConfirmClicked = {
                                    Toast.makeText(
                                        this@MainActivity,
                                        getString(R.string.lbl_confirmation_placeholder),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onLeaveScreen = {
                                    viewModel.resetSelections()
                                    navController.navigate(Destinations.Manufacturers) {
                                        popUpTo(Destinations.Manufacturers) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            )
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