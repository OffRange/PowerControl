package de.davis.powercontrol.dashboard.di

import de.davis.powercontrol.dashboard.presentation.DashboardViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val dashboardKoinModule = module {
    viewModelOf(::DashboardViewModel)
}