package de.davis.powercontrol.device.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import de.davis.powercontrol.core.domain.`typealias`.HexString
import de.davis.powercontrol.core.domain.`typealias`.IpAddress
import de.davis.powercontrol.core.domain.`typealias`.MacAddress

@Entity(tableName = "devices", indices = [Index(value = ["ip_address"], unique = true)])
data class DeviceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val password: HexString?,
    val port: Int,

    @ColumnInfo(name = "ip_address") val ipAddress: IpAddress,
    @ColumnInfo(name = "mac_address") val macAddress: MacAddress?,
    @ColumnInfo(name = "shutdown_sequence") val shutdownSequence: Byte
)