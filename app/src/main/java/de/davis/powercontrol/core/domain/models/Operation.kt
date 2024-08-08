package de.davis.powercontrol.core.domain.models

sealed interface Operation {
    data object ShutdownOrBoot : Operation
}

enum class PowerOperation : Operation {
    Shutdown,
    Boot,
    Restart,
    Logout,
}