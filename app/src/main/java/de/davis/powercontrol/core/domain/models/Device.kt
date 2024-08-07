package de.davis.powercontrol.core.domain.models

import de.davis.powercontrol.core.domain.DEFAULT_PORT
import de.davis.powercontrol.core.domain.DEFAULT_SHUTDOWN_SEQUENCE
import de.davis.powercontrol.core.domain.`typealias`.HexString
import de.davis.powercontrol.core.domain.`typealias`.IpAddress
import de.davis.powercontrol.core.domain.`typealias`.MacAddress

data class Device(
    val id: Long = 0,
    val name: String = "",
    val ip: IpAddress = "",
    val mac: MacAddress = "",
    val port: Int = DEFAULT_PORT,
    val password: HexString? = null,
    val shutdownSequence: Byte = DEFAULT_SHUTDOWN_SEQUENCE,
    val status: DeviceStatus = DeviceStatus.Pending,
    val scheduledOperation: ScheduledOperation = ScheduledOperation.None
)

val Device.uShutdownSequence get() = shutdownSequence.toUByte()

val Device.couldAcceptOperations
    get() = status == DeviceStatus.Online || (status == DeviceStatus.Offline && !mac.isNullOrBlank())
