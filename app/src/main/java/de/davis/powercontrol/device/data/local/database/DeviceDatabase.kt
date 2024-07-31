package de.davis.powercontrol.device.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import de.davis.powercontrol.device.data.local.dao.DeviceDao
import de.davis.powercontrol.device.data.local.entity.DeviceEntity

@Database(version = 1, entities = [DeviceEntity::class])
abstract class DeviceDatabase : RoomDatabase() {

    abstract fun deviceDao(): DeviceDao
}

const val DEVICES_DB_NAME = "devices.db"