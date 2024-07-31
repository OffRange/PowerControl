package de.davis.powercontrol.device.mapper

import de.davis.powercontrol.core.domain.models.Device
import de.davis.powercontrol.device.data.local.entity.DeviceEntity

fun DeviceEntity.toDomain(): Device = Device(
    id = id,
    name = name,
    ip = ipAddress,
    mac = macAddress,
    port = port,
    password = password,
    shutdownSequence = shutdownSequence
)

fun Device.toEntity(): DeviceEntity = DeviceEntity(
    id = id,
    name = name,
    ipAddress = ip,
    macAddress = mac,
    port = port,
    password = password,
    shutdownSequence = shutdownSequence
)