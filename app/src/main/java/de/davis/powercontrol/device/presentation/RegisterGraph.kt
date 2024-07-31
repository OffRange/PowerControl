package de.davis.powercontrol.device.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import de.davis.powercontrol.core.prenentation.Destination
import de.davis.powercontrol.dashboard.presentation.DashboardDestination
import kotlinx.serialization.Serializable

@Serializable
data class Register(val ip: String? = null) : Destination

fun NavGraphBuilder.registerGraph(navigate: (Destination) -> Unit) {
    composable<Register> {
        val route = it.toRoute<Register>()
        DeviceScreen(ip = route.ip, navigateBack = { navigate(DashboardDestination) })
    }
}