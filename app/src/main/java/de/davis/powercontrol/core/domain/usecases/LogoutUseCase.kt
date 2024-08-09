package de.davis.powercontrol.core.domain.usecases

import de.davis.powercontrol.core.domain.LOGOUT_BYTE
import de.davis.powercontrol.core.domain.models.Device
import de.davis.powercontrol.core.domain.network.datagramSocket
import de.davis.powercontrol.core.domain.network.mapper.toInetAddress
import de.davis.powercontrol.core.domain.network.receivePacket
import de.davis.powercontrol.core.domain.network.sendPacket
import de.davis.powercontrol.core.domain.usecases.ShutdownUseCase.Companion.TIMEOUT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.DurationUnit

class LogoutUseCase {

    suspend operator fun invoke(device: Device): Boolean = withContext(Dispatchers.IO) {
        val buffer = ByteArray(1)
        datagramSocket {
            sendPacket(
                data = byteArrayOf(LOGOUT_BYTE, device.shutdownSequence /*TODO*/),
                serverAddress = device.ip.toInetAddress(),
                port = device.port
            )

            val pack = receivePacket(buffer, TIMEOUT.toInt(DurationUnit.MILLISECONDS))
            //TODO receive and send (error) feedback to user
        }
        return@withContext true // TODO
    }
}