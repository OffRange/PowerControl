package de.davis.powercontrol.core.domain.usecases

import de.davis.powercontrol.core.domain.DEFAULT_PORT
import de.davis.powercontrol.core.domain.HEARTBEAT_BYTE
import de.davis.powercontrol.core.domain.models.Device
import de.davis.powercontrol.core.domain.models.DeviceStatus
import de.davis.powercontrol.core.domain.network.datagramSocket
import de.davis.powercontrol.core.domain.network.mapper.toInetAddress
import de.davis.powercontrol.core.domain.network.receivePacket
import de.davis.powercontrol.core.domain.network.sendPacket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

class GetDeviceStatusUseCase {

    suspend operator fun invoke(device: Device): DeviceStatus = withContext(Dispatchers.IO) {

        val data = byteArrayOf(HEARTBEAT_BYTE)
        val buffer = ByteArray(1)
        datagramSocket {
            sendPacket(data, serverAddress = device.ip.toInetAddress(), port = DEFAULT_PORT)

            val result = receivePacket(buffer, 1.seconds.toInt(DurationUnit.MILLISECONDS))

            result.fold(
                onSuccess = { DeviceStatus.Online },
                onFailure = { DeviceStatus.Offline }
            )
        }
    }
}