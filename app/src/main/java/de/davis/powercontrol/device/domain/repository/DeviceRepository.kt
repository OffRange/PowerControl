package de.davis.powercontrol.device.domain.repository

import de.davis.powercontrol.core.domain.models.Device
import de.davis.powercontrol.core.domain.`typealias`.IpAddress
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {

    fun observeAllDevices(): Flow<List<Device>>
    suspend fun getDeviceByIp(ip: IpAddress): Device?
    suspend fun upsertDevice(device: Device): Boolean
}