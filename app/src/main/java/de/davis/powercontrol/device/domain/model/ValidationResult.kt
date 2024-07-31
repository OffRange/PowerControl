package de.davis.powercontrol.device.domain.model

sealed interface ValidationResult {

    data object NameBlank : ValidationResult
}

sealed interface IpValidationResult : ValidationResult {

    data object IpExists : IpValidationResult
    data object IpInvalid : IpValidationResult

    companion object : IpValidationResult
}

sealed interface MacValidationResult : ValidationResult {
    data object MacInvalid : MacValidationResult

    companion object : MacValidationResult
}

inline infix fun <reified T : ValidationResult> T.`in`(list: Iterable<ValidationResult>): Boolean {
    return list.filterIsInstance(if (T::class.simpleName != "Companion") T::class.java else T::class.java.declaringClass)
        .any()
}