package de.davis.powercontrol.core.domain.usecases

import de.davis.powercontrol.core.domain.SHUTDOWN_BYTE
import de.davis.powercontrol.core.domain.models.Device
import de.davis.powercontrol.core.domain.network.datagramSocket
import de.davis.powercontrol.core.domain.network.mapper.toInetAddress
import de.davis.powercontrol.core.domain.network.receivePacket
import de.davis.powercontrol.core.domain.network.sendPacket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

class ShutdownUseCase {

    suspend operator fun invoke(device: Device): Boolean = withContext(Dispatchers.IO) {
        val buffer = ByteArray(1)
        datagramSocket {
            sendPacket(
                data = byteArrayOf(SHUTDOWN_BYTE, device.shutdownSequence),
                serverAddress = device.ip.toInetAddress(),
                port = device.port
            )

            val pack = receivePacket(buffer, TIMEOUT.toInt(DurationUnit.MILLISECONDS))
            //TODO receive and send feedback to user
        }
        return@withContext true // TODO
    }

    companion object {
        val TIMEOUT: Duration = 5.seconds
    }
}