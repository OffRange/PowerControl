package de.davis.powercontrol.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import de.davis.powercontrol.core.prenentation.koin.KoinInitiatedAppTheme
import de.davis.powercontrol.dashboard.presentation.DashboardDestination
import de.davis.powercontrol.dashboard.presentation.dashboardGraph
import de.davis.powercontrol.device.presentation.registerGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoinInitiatedAppTheme {
                Surface {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = DashboardDestination
                    ) {
                        dashboardGraph {
                            navController.navigate(it)
                        }
                        registerGraph {
                            navController.navigate(it)
                        }
                    }
                }
            }
        }
    }
}