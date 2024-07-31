package de.davis.powercontrol.dashboard.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import de.davis.powercontrol.core.prenentation.Destination
import kotlinx.serialization.Serializable

@Serializable
object DashboardDestination : Destination

fun NavGraphBuilder.dashboardGraph(navigate: (Destination) -> Unit) {
    composable<DashboardDestination> {
        DashboardScreen(
            onNavigateEvent = navigate
        )
    }
}