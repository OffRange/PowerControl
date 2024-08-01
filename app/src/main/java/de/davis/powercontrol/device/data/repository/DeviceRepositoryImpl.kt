package de.davis.powercontrol.device.data.repository

import de.davis.powercontrol.core.domain.models.Device
import de.davis.powercontrol.core.domain.`typealias`.IpAddress
import de.davis.powercontrol.device.data.local.dao.DeviceDao
import de.davis.powercontrol.device.domain.repository.DeviceRepository
import de.davis.powercontrol.device.mapper.toDomain
import de.davis.powercontrol.device.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DeviceRepositoryImpl(private val deviceDao: DeviceDao) : DeviceRepository {

    override fun observeAllDevices(): Flow<List<Device>> =
        deviceDao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getDeviceByIp(ip: IpAddress) = deviceDao.getByIpAddress(ip)?.toDomain()

    override suspend fun upsertDevice(device: Device): Boolean {
        return deviceDao.create(device.toEntity()) > 0
    }

    override suspend fun deleteDevice(ip: IpAddress) {
        deviceDao.getByIpAddress(ip)?.let {
            deviceDao.delete(it)
        }
    }
}