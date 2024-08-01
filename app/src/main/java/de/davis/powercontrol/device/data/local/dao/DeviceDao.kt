package de.davis.powercontrol.device.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import de.davis.powercontrol.core.domain.`typealias`.IpAddress
import de.davis.powercontrol.device.data.local.entity.DeviceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {

    @Query("SELECT * FROM devices")
    fun observeAll(): Flow<List<DeviceEntity>>

    @Query("SELECT * FROM devices WHERE ip_address = :ip")
    suspend fun getByIpAddress(ip: IpAddress): DeviceEntity?

    @Query("SELECT * FROM devices WHERE id = :id")
    suspend fun getById(id: Long): DeviceEntity?

    @Upsert
    suspend fun create(device: DeviceEntity): Long

    @Delete
    suspend fun delete(device: DeviceEntity)
}